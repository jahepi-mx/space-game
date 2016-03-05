package com.jahepi.spacegame;

import com.badlogic.gdx.Screen;
import com.jahepi.spacegame.screens.MainScreen;
import com.jahepi.spacegame.screens.PlayScreen;

public class ScreenManager {

	public static enum SCREEN_TYPE {
		MENU_SCREEN,
		PLAY_SCREEN,
	};
	
	private Screen currentScreen;
	private SpaceGame game;
	
	public ScreenManager(SpaceGame game) {
		this.game = game;
	}
	
	private Screen getScreen(SCREEN_TYPE type) {
		switch (type) {
			case MENU_SCREEN:
				currentScreen =  new MainScreen(this.game.getStage(), this.game.getChangeScreenListener());
				return currentScreen;
			case PLAY_SCREEN:
				currentScreen =  new PlayScreen(this.game.getBatch(), this.game.getChangeScreenListener());
				return currentScreen;
		}
		return null;
	}
	
	public void setScreen(SCREEN_TYPE type) {
		Screen currentScreen = this.currentScreen;
		Screen newScreen = this.getScreen(type);
		if (newScreen != null) {
			this.game.setScreen(newScreen);
			if (currentScreen != null) {
				currentScreen.dispose();
			}
		}
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}
}
