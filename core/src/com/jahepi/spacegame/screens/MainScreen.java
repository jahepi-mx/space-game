package com.jahepi.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.ScreenManager.SCREEN_TYPE;
import com.jahepi.spacegame.listeners.ChangeScreenListener;

public class MainScreen implements Screen {

	private Stage stage;
	private OrthographicCamera camera;
	private ChangeScreenListener changeScreenlistener;
	private Texture background;
	private Button playBtn;
	private Label startLabel;
	private BitmapFont titleFont;
	private GlyphLayout glyphLayout;
	
	public MainScreen(Stage stage, ChangeScreenListener changeScreenlistener) {
		this.stage = stage;
		this.camera = new OrthographicCamera(Config.GUI_WIDTH, Config.GUI_HEIGHT);
		this.camera.position.x = Config.GUI_WIDTH / 2;
		this.camera.position.y = Config.GUI_HEIGHT / 2;
		this.camera.update();
		this.changeScreenlistener = changeScreenlistener;
		this.background = Resource.getInstance().getBackgroundMenuTexture();
		this.titleFont = Resource.getInstance().getTitleFont();
		this.glyphLayout = Resource.getInstance().getGlyphLayout();
		Gdx.input.setInputProcessor(this.stage);
		
		TextureRegion region = new TextureRegion(Resource.getInstance().getPlayBtnTexture());
		playBtn = new Button(new TextureRegionDrawable(region));
		playBtn.setPosition((Config.GUI_WIDTH / 2) - (playBtn.getWidth() / 2), (Config.GUI_HEIGHT / 2) - (playBtn.getHeight() / 2));
		playBtn.setTransform(true);
		playBtn.setOriginX(playBtn.getWidth() / 2);
		playBtn.setOriginY(playBtn.getHeight() / 2);
		playBtn.setScale(0.4f);
		playBtn.padLeft(160.0f);
		
		LabelStyle style = new LabelStyle();
		BitmapFont start = Resource.getInstance().getFpsFont();
		style.font = start;
		startLabel = new Label("START NEW GAME", style);
		startLabel.setFontScale(1.7f);
		startLabel.setOriginX(startLabel.getWidth() / 2);
		startLabel.setOriginY(startLabel.getHeight() / 2);
		startLabel.setPosition(playBtn.getWidth() / 2, playBtn.getHeight() / 2);
		playBtn.add(startLabel);
		
		playBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MainScreen.this.changeScreenlistener.onChangeScreen(SCREEN_TYPE.PLAY_SCREEN);
			}		
		});
		
		this.stage.addActor(playBtn);
	}

	@Override
	public void show() {
		Resource.getInstance().getIntroMusic().play();
	}

	@Override
	public void render(float delta) {
		SpriteBatch batch = (SpriteBatch) stage.getBatch();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0, Config.GUI_WIDTH, Config.GUI_HEIGHT);
		glyphLayout.setText(titleFont, "SPACE SHOOTER");
		titleFont.draw(batch, glyphLayout, (Config.GUI_WIDTH / 2) - (glyphLayout.width / 2), (Config.GUI_HEIGHT / 2) + (glyphLayout.height * 4));
		batch.end();
		stage.act(delta);
		stage.draw();
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
		Resource.getInstance().getIntroMusic().stop();
		this.stage.clear();
	}
}
