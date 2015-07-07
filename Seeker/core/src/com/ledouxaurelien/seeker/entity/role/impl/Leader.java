package com.ledouxaurelien.seeker.entity.role.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledouxaurelien.seeker.entity.role.IRole;
import com.ledouxaurelien.seeker.singleton.LevelManager;

public class Leader implements IRole {

	private Sprite sprite;
	
	public Leader() {
		this.sprite = new Sprite((Texture)LevelManager.getLevelManager().getAssetManager().get("sprites/player.png"));
	}
	
	@Override
	public boolean haveWin() {
		return false;
	}
	
	@Override
	public String toString() {
		return "Leader";
	}

	@Override
	public Sprite getSprite() {
		return this.sprite;
	}

}
