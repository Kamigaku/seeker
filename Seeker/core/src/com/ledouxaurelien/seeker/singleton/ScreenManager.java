package com.ledouxaurelien.seeker.singleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ledouxaurelien.seeker.GameLauncher;

public class ScreenManager {

	private static ScreenManager screenManager;
	
	public static GameLauncher game;
	
	private Screen currentScreen;
	
	public ScreenManager() {
	}
	
	public static ScreenManager getScreenManger() {
		if(screenManager == null)
			screenManager = new ScreenManager();
		return screenManager;
	}
	
	public void changeScreen(Screen screen) {
		if(this.currentScreen != null)
			System.out.println("Changement d'�cran de " + this.currentScreen.toString() + " � " + screen.toString());
		this.currentScreen = screen;
		Gdx.graphics.setTitle(screen.toString());
		game.setScreen(this.currentScreen);
	}
	
	public Screen getCurrentScreen() {
		return this.currentScreen;
	}
	
}
