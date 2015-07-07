package com.ledouxaurelien.seeker.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.esotericsoftware.kryonet.Client;
import com.ledouxaurelien.seeker.entity.role.IRole;
import com.ledouxaurelien.seeker.entity.role.impl.Detective;
import com.ledouxaurelien.seeker.entity.role.impl.GangMember;
import com.ledouxaurelien.seeker.entity.role.impl.Innocent;
import com.ledouxaurelien.seeker.entity.role.impl.Leader;
import com.ledouxaurelien.seeker.entity.role.impl.Murderer;
import com.ledouxaurelien.seeker.entity.role.impl.Policeman;
import com.ledouxaurelien.seeker.entity.role.impl.Voyant;
import com.ledouxaurelien.seeker.singleton.LevelManager;
import com.ledouxaurelien.seeker.singleton.ServerManager;

public class Player implements IEntity {
	
	// Network Connection
	private Client client;
	public boolean isConnected = false;
	public String[] pseudos;
	
	// Player datas
	private String pseudo;
	private IRole role;
	private String inputsKeys = "";
	private boolean toUpdate = false;
	private Body body;
		
	public Player(String pseudo) {
		this.pseudo = pseudo;
	}
	
	@Override
	public BodyDef createBody(float x, float y, float angle) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		this.body = LevelManager.getLevelManager().getLevel().addBody(bodyDef);
		PolygonShape collider = new PolygonShape();  
		collider.setAsBox(16, 16);
		FixtureDef fDef = new FixtureDef();
		fDef.density = 0f;
		fDef.friction = 0f;
		fDef.restitution = 0f;
		fDef.shape = collider;
		body.createFixture(fDef);
		collider.dispose();
		return bodyDef;
	}

	public void disconnectFromCurrentServer() {
		this.client.stop();
		this.client.close();
		this.client = null;
	}
	
	@Override
	public void update(SpriteBatch batch) {
		if(toUpdate) {
			if(this.inputsKeys != null) {
				ServerManager.getServerManager().sendMessage("datas|" + this.inputsKeys);
				this.toUpdate = false;
			}
			if(this.inputsKeys.contains("" + Keys.E)) { // Bullet, ne fonctionne pas très bien, revoir plus tard
				Vector2 centerPosition = new Vector2(this.body.getPosition().x, this.body.getPosition().y);
			    Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			    LevelManager.getLevelManager().getCamera().unproject(worldCoordinates);
			    Vector2 mouseCursor = new Vector2(worldCoordinates.x, worldCoordinates.y);
			    mouseCursor.sub(centerPosition);
				double norme = Math.sqrt(Math.pow(mouseCursor.y, 2) + Math.pow(mouseCursor.x, 2));
				double yCursor = (float) (mouseCursor.y / norme);
				double xCursor = (float) (mouseCursor.x / norme);
				double angle = Math.atan2(yCursor, xCursor);
				ServerManager.getServerManager().sendMessage("projectile|" + xCursor + "|" + yCursor + "|" + angle);
			}
		}
		batch.draw(this.role.getSprite(), this.body.getPosition().x - 16, this.body.getPosition().y - 16);
	}

	public void updatePosition(float forceX, float forceY, float x, float y) {
		this.body.setTransform(x, y, 0);
		this.body.setLinearVelocity(forceX, forceY);
	}

	public void assignRole(String role) {
		switch(role) {
			case "Policier":
				this.role = new Policeman();
				break;
			case "Detective":
				this.role = new Detective();
				break;
			case "Leader":
				this.role = new Leader();
				break;
			case "Gang":
				this.role = new GangMember("toto");
				break;
			case "Voyant":
				this.role = new Voyant();
				break;
			case "Murderer":
				this.role = new Murderer();
				break;
			case "Innocent":
				this.role = new Innocent();
				break;
		}
		System.out.println("Vous êtes désormais un " + this.role.toString());
	}
	
	public InputProcessor preGameStuff() {
		return new InputProcessor() {
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean scrolled(int amount) {
				return false;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				inputsKeys = inputsKeys.replace("" + keycode + "-", "");
				toUpdate = true;
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				inputsKeys += "" + keycode + "-";
				toUpdate = true;
				return false;
			}
		};
	}
	
	public void setInputs(String inputs) {
		this.inputsKeys = inputs;
	}

	public String[] getPseudos() {
		return this.pseudos;
	}

	public String getPseudo() {
		return this.pseudo;
	}
	
	public IRole getRole() {
		return this.role;
	}

	@Override
	public Body getBody() {
		return this.body;
	}
}
