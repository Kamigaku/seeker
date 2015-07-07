package com.ledouxaurelien.seeker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ledouxaurelien.seeker.singleton.ScreenManager;

public class MainScreen implements Screen {
	
	private BitmapFont defaultFont;
	private BitmapFont activeFont;
	private int currentChoice = 0;
	private String[] options = {
		"Start a game",
		"Options",
		"Quit"
	};
	
	public MainScreen() {
		defaultFont = new BitmapFont();
		defaultFont.setColor(new com.badlogic.gdx.graphics.Color(255, 255, 255, 1));
		activeFont = new BitmapFont();
		activeFont.setColor(new com.badlogic.gdx.graphics.Color(255, 0, 0, 1));
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenManager.game.batch.begin();
		Gdx.input.setInputProcessor(new InputProcessor() {
			
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
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.DPAD_DOWN) {
					currentChoice++;
					if(currentChoice == options.length)
						currentChoice = 0;					
				}
				if (keycode == Keys.DPAD_UP) {
					currentChoice--;
					if(currentChoice == -1)
						currentChoice = options.length - 1;
				}
				if(keycode == Keys.ENTER) {
					if(currentChoice == 0) {
						ScreenManager.getScreenManger().changeScreen(new MultiplayerScreen());
					}
					if(currentChoice == 1) {
					}
					if(currentChoice == 2)
						Gdx.app.exit();
				}
				return false;
			}
		});
		for(int i = 0; i < options.length; i++) {
			if(currentChoice == i)
				activeFont.draw(ScreenManager.game.batch, options[i], (Gdx.graphics.getWidth() / 2) - 50, (Gdx.graphics.getHeight() / 2) - (50 * i));
			else
				defaultFont.draw(ScreenManager.game.batch, options[i], (Gdx.graphics.getWidth() / 2) - 50, (Gdx.graphics.getHeight() / 2) - (50 * i));
		}
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
		this.defaultFont.dispose();
		this.activeFont.dispose();
	}

}
