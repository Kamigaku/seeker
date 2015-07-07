package com.ledouxaurelien.seeker.level.impl;

import java.util.Map.Entry;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ledouxaurelien.seeker.constants.Constants;
import com.ledouxaurelien.seeker.entity.IEntity;
import com.ledouxaurelien.seeker.entity.Player;
import com.ledouxaurelien.seeker.hud.HUD;
import com.ledouxaurelien.seeker.level.ILevel;
import com.ledouxaurelien.seeker.singleton.LevelManager;
import com.ledouxaurelien.seeker.singleton.ServerManager;

public class GameLevel implements ILevel {
	
	private World world;
	private TiledMap map;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer mapRenderer;
	private AssetManager assetManager;
	private Box2DDebugRenderer debugRenderer;
	private HUD hud;
	private InputMultiplexer multiplexer;
	
	
	public GameLevel(String pathMap) {
		this.world = new World(new Vector2(0, 0), true);
		this.multiplexer = new InputMultiplexer();
		this.map = new TmxMapLoader().load("prefab/maps/" + pathMap);
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		this.camera.update();
		new Box2DMapObjectParser().load(this.world, this.map);
		this.mapRenderer = new OrthogonalTiledMapRenderer(this.map);
		this.debugRenderer = new Box2DDebugRenderer();
		this.assetManager = new AssetManager();
		this.assetManager.load("sprites/player.png", Texture.class);
		this.assetManager.finishLoading();
		this.hud = new HUD();
		multiplexer.addProcessor(this.hud.getStage());
		ServerManager.getServerManager().sendMessage("mapReady");
	}

	@Override
	public Body addBody(BodyDef bodyDef) {
		return this.world.createBody(bodyDef);
	}

	@Override
	public void removeBody(IEntity entity) {
		this.world.destroyBody(entity.getBody());		
	}

	@Override
	public void render(SpriteBatch batch) {
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.setProjectionMatrix(this.camera.combined);
        world.step(1/60f, 6, 2);
        batch.end();
        this.hud.draw();
        this.debugRenderer.render(this.world, this.camera.combined);
        batch.begin();
        for(Entry<Integer, Player> player : LevelManager.getLevelManager().getPlayers().entrySet()) {
        	player.getValue().update(batch);
        }
        if(LevelManager.getLevelManager().mainPlayer != null)
        this.camera.translate(LevelManager.getLevelManager().mainPlayer.getBody().getPosition().x - this.camera.position.x,
        					  LevelManager.getLevelManager().mainPlayer.getBody().getPosition().y - this.camera.position.y);
        this.camera.update();
	}

	@Override
	public void dispose() {
		this.assetManager.dispose();
		this.world.dispose();
		this.map.dispose();
	}

	@Override
	public AssetManager getAssetManager() {
		return this.assetManager;
	}
	
	@Override
	public OrthographicCamera getCamera() {
		return this.camera;
	}

	@Override
	public void addInputProcessor(InputProcessor ip) {
		this.multiplexer.addProcessor(ip);
		Gdx.input.setInputProcessor(multiplexer);
	}

}
