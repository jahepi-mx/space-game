package com.jahepi.spacegame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.spacegame.ScreenManager.SCREEN_TYPE;
import com.jahepi.spacegame.entities.Item;
import com.jahepi.spacegame.entities.Laser;
import com.jahepi.spacegame.entities.level1.Enemy;
import com.jahepi.spacegame.entities.level1.EnemyLaser;
import com.jahepi.spacegame.listeners.ChangeScreenListener;
import com.jahepi.spacegame.texts.SampleText;

public class Render implements Disposable, ControllerListener {
	
	private OrthographicCamera camera;
	private OrthographicCamera cameraGui;
	private SpriteBatch batch;
	private ShapeRenderer shapeRender;
	private Controller controller;
	private BitmapFont fpsFont;
	private BitmapFont scoreFont;
	private BitmapFont gameOverFont;
	private GlyphLayout glyphLayout;
	private ChangeScreenListener changeScreenListener;
	private SampleText sampleText;
	
	public Render(SpriteBatch batch, ChangeScreenListener changeScreenListener) {
		this.batch = batch;
		this.shapeRender = new ShapeRenderer();
		this.shapeRender.setAutoShapeType(true);
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Config.GAME_WIDTH, Config.GAME_HEIGHT);
		this.camera.position.x = Config.GAME_WIDTH / 2;
		this.camera.position.y = Config.GAME_HEIGHT / 2;
		this.cameraGui = new OrthographicCamera();
		this.cameraGui.setToOrtho(false, Config.GUI_WIDTH, Config.GUI_HEIGHT);
		this.cameraGui.position.x = Config.GUI_WIDTH / 2;
		this.cameraGui.position.y = Config.GUI_HEIGHT / 2;
		this.controller = new Controller(this);
		this.fpsFont = Resource.getInstance().getFpsFont();
		this.scoreFont = Resource.getInstance().getScoreFont();
		this.gameOverFont = Resource.getInstance().getGameOverFont();
		this.glyphLayout = Resource.getInstance().getGlyphLayout();
		this.changeScreenListener = changeScreenListener;
		this.sampleText = new SampleText();
	}
	
	public void render(float deltatime) {
		controller.update(deltatime);
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		controller.getBackground().render(batch);
		Array<Item> items = controller.getItems();
		for (Item item : items) {
			item.render(batch);
		}
		controller.getShip().render(batch);
		if (controller.getBoss() != null) {
			controller.getBoss().render(batch);
		}
		Array<Laser> lasers = controller.getLasers();
		for (Laser laser : lasers) {
			laser.render(batch);
		}
		Array<Enemy> enemies = controller.getEnemies();
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		Array<EnemyLaser> enemyLasers = controller.getEnemyLasers();
		for (EnemyLaser laser : enemyLasers) {
			laser.render(batch);
		}
		batch.end();
		
		shapeRender.setProjectionMatrix(camera.combined);
		shapeRender.begin(ShapeType.Filled);
		Array<Enemy> enemies2 = controller.getEnemies();
		Iterator<Enemy> enemyIterator2 = enemies2.iterator();
		while (enemyIterator2.hasNext()) {
			Enemy enemy = enemyIterator2.next();
			enemy.drawLifeBar(shapeRender);
		}
		if (controller.getBoss() != null) {
			this.controller.getBoss().drawLifeBar(shapeRender);
		}
		controller.getShip().drawLifeBar(shapeRender);
		shapeRender.end();
		
		if (Config.DEBUG) {
			shapeRender.setProjectionMatrix(camera.combined);
			shapeRender.begin();
			controller.getBackground().debug(shapeRender);
			controller.getShip().debug(shapeRender);
			if (controller.getBoss() != null) {
				controller.getBoss().debug(shapeRender);
			}
			Array<Item> itemsDebug = controller.getItems();
			for (Item item : itemsDebug) {
				item.debug(shapeRender);
			}
			Array<Laser> lasersDebug = controller.getLasers();
			for (Laser laser : lasersDebug) {
				laser.debug(shapeRender);
			}
			Array<EnemyLaser> enemyLasersDebug = controller.getEnemyLasers();
			for (EnemyLaser laser : enemyLasersDebug) {
				laser.debug(shapeRender);
			}
			shapeRender.end();
		}
		
		// Draw GUI elements 
		batch.setProjectionMatrix(cameraGui.combined);
		batch.begin();
		glyphLayout.setText(scoreFont, "Score: " + this.controller.getPoints());
		scoreFont.draw(batch, glyphLayout, glyphLayout.height, Config.GUI_HEIGHT - glyphLayout.height);
		if (controller.isGameOver()) {
			glyphLayout.setText(gameOverFont, "GAME OVER");
			gameOverFont.draw(batch, glyphLayout, (Config.GUI_WIDTH / 2) - (glyphLayout.width / 2), (Config.GUI_HEIGHT / 2) + (glyphLayout.height / 2));
		}
		glyphLayout.setText(fpsFont, "Fps " + Gdx.graphics.getFramesPerSecond());
		fpsFont.draw(batch, glyphLayout, Config.GUI_WIDTH - glyphLayout.width - glyphLayout.height, Config.GUI_HEIGHT - glyphLayout.height);
		if (controller.isChangingLevel()) {
			glyphLayout.setText(gameOverFont, "LEVEL " + controller.getLevel());
			gameOverFont.draw(batch, glyphLayout, (Config.GUI_WIDTH / 2) - (glyphLayout.width / 2), (Config.GUI_HEIGHT - (glyphLayout.height * 4)));
		}
		
		sampleText.update(deltatime);
		sampleText.render(batch);
		batch.end();
		
		if (controller.isChangeScreenFlag()) {
			this.changeScreenListener.onChangeScreen(SCREEN_TYPE.MENU_SCREEN);
		}
	}

	@Override
	public void dispose() {
		shapeRender.dispose();
		controller.dispose();
	}

	@Override
	public void onChangeLevel() {
		sampleText.play();
	}
}