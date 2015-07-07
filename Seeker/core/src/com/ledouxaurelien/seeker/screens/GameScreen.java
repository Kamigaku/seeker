package com.ledouxaurelien.seeker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.ledouxaurelien.seeker.hud.HUD;
import com.ledouxaurelien.seeker.level.ILevel;
import com.ledouxaurelien.seeker.singleton.LevelManager;
import com.ledouxaurelien.seeker.singleton.ScreenManager;

public class GameScreen implements Screen {
	
	public GameScreen(ILevel level) {
		LevelManager.getLevelManager().setLevel(level);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenManager.game.batch.begin();
		LevelManager.getLevelManager().render(ScreenManager.game.batch);
		ScreenManager.game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		LevelManager.getLevelManager().dispose();
	}

}
