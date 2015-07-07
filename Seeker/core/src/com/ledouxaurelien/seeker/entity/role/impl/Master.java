package com.ledouxaurelien.seeker.entity.role.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledouxaurelien.seeker.entity.role.IRole;
import com.ledouxaurelien.seeker.singleton.LevelManager;

public class Master implements IRole {

	private Sprite sprite;
	
	public Master() {
		this.sprite = new Sprite((Texture)LevelManager.getLevelManager().getAssetManager().get("sprites/player.png"));
	}
	
	@Override
	public boolean haveWin() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return this.sprite;
	}

}
