package com.jahepi.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jahepi.spacegame.Render;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.listeners.ChangeScreenListener;

public class PlayScreen implements Screen {

	private SpriteBatch batch;
	private Render render;
	
	public PlayScreen(SpriteBatch batch, ChangeScreenListener changeScreenlistener) {
		this.batch = batch;
		render = new Render(this.batch, changeScreenlistener);
	}

	@Override
	public void show() {
		Resource.getInstance().getGameMusic().play();
		Resource.getInstance().getBossMusic().stop();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		render.render(delta);
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
		
	}

	@Override
	public void dispose() {
		Resource.getInstance().getGameMusic().stop();
		Resource.getInstance().getBossMusic().stop();
		render.dispose();
	}
}
