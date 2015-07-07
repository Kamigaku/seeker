package com.ledouxaurelien.seeker.entity.role.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledouxaurelien.seeker.entity.role.IRole;
import com.ledouxaurelien.seeker.singleton.LevelManager;

public class Murderer implements IRole {
	
	private Sprite sprite;
	
	public Murderer() {
		this.sprite = new Sprite((Texture)LevelManager.getLevelManager().getAssetManager().get("sprites/player.png"));
	}
	
	@Override
	public boolean haveWin() {
		return true;
	}

	@Override
	public String toString() {
		return "Murderer";
	}

	@Override
	public Sprite getSprite() {
		return this.sprite;
	}
	
}
