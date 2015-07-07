package com.ledouxaurelien.seeker.singleton;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ledouxaurelien.seeker.entity.Player;
import com.ledouxaurelien.seeker.level.ILevel;

public class LevelManager {

	private static LevelManager levelManager;
	
	private ILevel currentLevel;
	private HashMap<Integer, Player> players;
	public Player mainPlayer;
	private AssetManager assetManager;
	private boolean gameOver = false;
	
	public static LevelManager getLevelManager() {
		if(levelManager == null)
			levelManager = new LevelManager();
		return levelManager;
	}
	
	public LevelManager() {
		this.players = new HashMap<>();
		this.assetManager = new AssetManager();
		this.assetManager.load("sprites/player.png", Texture.class);
		this.assetManager.finishLoading();
	}
	
	public void addPlayer(int id, String pseudo, String role, float x, float y) {
		Player p = new Player(pseudo);
		p.assignRole(role);
		currentLevel.addBody(p.createBody(x, y, 0));
		if(id == ServerManager.getServerManager().client.getID()) {
			this.currentLevel.addInputProcessor(p.preGameStuff());
			this.mainPlayer = p;
		}
		players.put(new Integer(id), p);
	}
	
	public Player getPlayer(Integer id) {
		return this.players.get(id);
	}
	
	public HashMap<Integer, Player> getPlayers() {
		return this.players;
	}

	public void clearPlayers() {
		this.players.clear();
	}
	
	public void setMainPlayer(Player player) {
		this.mainPlayer = player;
	}
	
	public Player getMainPlayer() {
		return this.mainPlayer;
	}
	
	public void setLevel(ILevel level) {
		this.currentLevel = level;
	}
	
	public ILevel getLevel() {
		return this.currentLevel;
	}
	
	public AssetManager getAssetManager() {
		return this.assetManager;
	}
	
	public void render(SpriteBatch batch) {
		if(!this.gameOver)
			this.currentLevel.render(batch);
		else
			System.out.println("Game is Over !");
	}
	
	public void gameIsOver() {
		for(Entry<Integer, Player> player : players.entrySet()) {
			if(player.getValue().getRole().haveWin())
				this.gameOver = true;
		}
	}

	public void dispose() {
		this.currentLevel.dispose();
		LevelManager.levelManager = null;
	}

	public void removePlayer(Integer idPlayer) {
		this.currentLevel.removeBody(this.players.get(idPlayer));
		this.players.remove(idPlayer);
	}
	
	public OrthographicCamera getCamera() {
		return this.currentLevel.getCamera();
	}
}
