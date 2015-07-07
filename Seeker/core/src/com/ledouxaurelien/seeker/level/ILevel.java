package com.ledouxaurelien.seeker.level;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ledouxaurelien.seeker.entity.IEntity;

public interface ILevel {

	void render(SpriteBatch batch);
	void dispose();
	Body addBody(BodyDef bodyDef);
	void addInputProcessor(InputProcessor ip);
	void removeBody(IEntity entity);
	AssetManager getAssetManager();
	OrthographicCamera getCamera();
	
}
