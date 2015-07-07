package com.ledouxaurelien.seeker.entity.role.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.ledouxaurelien.seeker.entity.role.IRole;
import com.ledouxaurelien.seeker.singleton.LevelManager;

public class GangMember implements IRole {

	private String gangName;
	private Sprite sprite;
	
	public GangMember(String gangName) {
		this.gangName = gangName;
		this.sprite = new Sprite((Texture)LevelManager.getLevelManager().getAssetManager().get("sprites/player.png"));
	}

	@Override
	public boolean haveWin() {
		return false;
	}
	
	@Override
	public String toString() {
		return "GangMember";
	}

	@Override
	public Sprite getSprite() {
		return this.sprite;
	}
}
