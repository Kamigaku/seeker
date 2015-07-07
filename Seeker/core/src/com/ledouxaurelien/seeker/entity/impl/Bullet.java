package com.ledouxaurelien.seeker.entity.impl;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.ledouxaurelien.seeker.entity.IEntity;
import com.ledouxaurelien.seeker.singleton.LevelManager;

public class Bullet implements IEntity {
	
	private Sprite sprite;
	private Body body;
	private float speed;
	
	public Bullet() {
		this.sprite = new Sprite();
		this.speed = 50f;
	}

	@Override
	public BodyDef createBody(float x, float y, float angle) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.angle = angle;
		this.body = LevelManager.getLevelManager().getLevel().addBody(bodyDef);
		PolygonShape collider = new PolygonShape(); 
		collider.setAsBox(3, 2);
		FixtureDef fDef = new FixtureDef();
		fDef.density = 0f;
		fDef.friction = 0f;
		fDef.restitution = 0f;
		fDef.shape = collider;
		body.createFixture(fDef);
		collider.dispose();
		return bodyDef;
	}

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public void update(SpriteBatch batch) {
		
	}
	
	public void defineVector(float x, float y) {
		this.body.setLinearVelocity(x * speed, y * speed);
	}

}
