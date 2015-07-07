package com.ledouxaurelien.seeker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ledouxaurelien.seeker.screens.MainScreen;
import com.ledouxaurelien.seeker.singleton.ScreenManager;
import com.ledouxaurelien.seeker.singleton.ServerManager;

public class GameLauncher extends Game {
	
	public SpriteBatch batch;
	
	public void create() {
		this.batch = new SpriteBatch();
		ScreenManager.game = this;
		ScreenManager.getScreenManger().changeScreen(new MainScreen());
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		ServerManager.getServerManager().client.close();
	}
}
