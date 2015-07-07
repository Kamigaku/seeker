package com.ledouxaurelien.seeker.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;


public interface IEntity {

	BodyDef createBody(float x, float y, float angle);
	Body getBody();
	void update(SpriteBatch batch);
	
}
