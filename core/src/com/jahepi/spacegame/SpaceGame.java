package com.jahepi.spacegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jahepi.spacegame.ScreenManager.SCREEN_TYPE;
import com.jahepi.spacegame.listeners.ChangeScreenListener;

public class SpaceGame extends Game {
	
	private SpriteBatch batch;
	private Stage stage;
	private ScreenManager screenManager;
	private ChangeScreenListener listener;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		StretchViewport viewport = new StretchViewport(Config.GUI_WIDTH, Config.GUI_HEIGHT);
		this.stage = new Stage(viewport, this.batch);
		this.screenManager = new ScreenManager(this);
		
		this.listener = new ChangeScreenListener() {		
			@Override
			public void onChangeScreen(SCREEN_TYPE type) {
				SpaceGame.this.screenManager.setScreen(type);	
			}
		};
		
		this.screenManager.setScreen(SCREEN_TYPE.MENU_SCREEN);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void resize(int width, int height) {
		// super.resize(width, height);
		stage.getViewport().update(width, height);
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Stage getStage() {
		return stage;
	}
	
	public ChangeScreenListener getChangeScreenListener() {
		return listener;
	}

	@Override
	public void dispose() {
		this.stage.dispose();
		this.batch.dispose();
		Resource.getInstance().dispose();	
	}
}
