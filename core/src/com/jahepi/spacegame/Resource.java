package com.jahepi.spacegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

public class Resource implements Disposable {
	
	private Texture spaceTexture;
	private Texture planetTexture1;
	private Texture planetTexture2;
	private Texture yodaTexture;
	private Texture backgroundMenuTexture;
	private Texture playBtnTexture;
	private TextureAtlas shipAtlas;
	private Music gameMusic;
	private Music bossMusic;
	private Music introMusic;
	private Sound bossLaserSound;
	private Sound enemyLaserSound;
	private Sound shipLaserSound;
	private Sound damageSound;
	private Sound rocketSound;
	private Sound explosionSound;
	private FreeTypeFontGenerator fontGenerator;
	private BitmapFont gameOverFont;
	private BitmapFont fpsFont;
	private BitmapFont scoreFont;
	private BitmapFont titleFont;
	private BitmapFont adviceFont;
	private GlyphLayout glyphLayout;
	
	private static Resource self;
	
	private Resource() {
		AssetManager manager = new AssetManager();
		manager.load("space.atlas", TextureAtlas.class);
		manager.finishLoading();
		this.spaceTexture = new Texture(Gdx.files.internal("background.jpg"));
		this.planetTexture1 = new Texture(Gdx.files.internal("planet1.png"));
		this.planetTexture2 = new Texture(Gdx.files.internal("planet2.png"));
		this.yodaTexture = new Texture(Gdx.files.internal("yoda.gif"));
		this.playBtnTexture = new Texture(Gdx.files.internal("mainButton.png"));
		this.backgroundMenuTexture = new Texture(Gdx.files.internal("background_menu.jpg"));
		this.shipAtlas = manager.get("space.atlas"); 
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("mp3/game.wav"));
		bossMusic = Gdx.audio.newMusic(Gdx.files.internal("mp3/boss.wav"));
		introMusic = Gdx.audio.newMusic(Gdx.files.internal("mp3/intro.wav"));
		bossLaserSound = Gdx.audio.newSound(Gdx.files.internal("mp3/bosslaser.wav"));
		enemyLaserSound = Gdx.audio.newSound(Gdx.files.internal("mp3/enemylaser.wav"));
		shipLaserSound = Gdx.audio.newSound(Gdx.files.internal("mp3/shiplaser.wav"));
		damageSound = Gdx.audio.newSound(Gdx.files.internal("mp3/damage.mp3"));
		rocketSound = Gdx.audio.newSound(Gdx.files.internal("mp3/rocket.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("mp3/explosion.wav"));
		gameMusic.setLooping(true);
		bossMusic.setLooping(true);
		introMusic.setLooping(true);
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/game.ttf"));
	    
	    FreeTypeFontParameter parameters = new FreeTypeFontParameter();
	    parameters.size = 120;
	    parameters.shadowOffsetX = 1;
	    parameters.shadowOffsetY = 1;
	    parameters.borderWidth = 5;
	    parameters.borderColor = Color.WHITE;
	    parameters.color = Color.RED;
	    gameOverFont = fontGenerator.generateFont(parameters);
	    
	    FreeTypeFontParameter parameters2 = new FreeTypeFontParameter();
	    parameters2.size = 70;
	    parameters2.color = Color.GREEN;
	    fpsFont = fontGenerator.generateFont(parameters2);
	    
	    FreeTypeFontParameter parameters3 = new FreeTypeFontParameter();
	    parameters3.size = 80;
	    parameters3.borderWidth = 2;
	    parameters3.borderColor = Color.CYAN;
	    parameters3.color = Color.WHITE;
		scoreFont = fontGenerator.generateFont(parameters3);
		
		FreeTypeFontParameter parameters4 = new FreeTypeFontParameter();
	    parameters4.size = 100;
	    parameters4.borderWidth = 2;
	    parameters4.borderColor = Color.CYAN;
	    parameters4.color = Color.WHITE;
		titleFont = fontGenerator.generateFont(parameters4);
		
		FreeTypeFontParameter parameters5 = new FreeTypeFontParameter();
	    parameters5.size = 70;
	    parameters5.color = Color.YELLOW;
		adviceFont = fontGenerator.generateFont(parameters5);
		
		glyphLayout = new GlyphLayout();
	}
	
	
	public static Resource getInstance() {
		if (self == null) {
			self = new Resource();
		}
		return self;
	}


	public TextureAtlas getShipAtlas() {
		return shipAtlas;
	}
	
	public Texture getSpaceTexture() {
		return spaceTexture;
	}
	
	public Texture getPlanetTexture1() {
		return planetTexture1;
	}
	
	public Texture getPlanetTexture2() {
		return planetTexture2;
	}
	
	public Texture getYodaTexture() {
		return yodaTexture;
	}
	
	public TextureRegion getBossTexture() {
		return shipAtlas.findRegion("boss");
	}
	
	public TextureRegion getBoss2Texture() {
		return shipAtlas.findRegion("boss2");
	}
	
	public TextureRegion getShipTexture() {
		return shipAtlas.findRegion("ship");
	}
	
	public TextureRegion getEnemyTexture() {
		return shipAtlas.findRegion("enemy");
	}
	
	public TextureRegion getEnemy2Texture() {
		return shipAtlas.findRegion("enemy2");
	}
	
	public Music getGameMusic() {
		return gameMusic;
	}

	public Music getBossMusic() {
		return bossMusic;
	}
	
	public Music getIntroMusic() {
		return introMusic;
	}

	public Sound getBossLaserSound() {
		return bossLaserSound;
	}

	public Sound getEnemyLaserSound() {
		return enemyLaserSound;
	}

	public Sound getShipLaserSound() {
		return shipLaserSound;
	}

	public Sound getExplosionSound() {
		return explosionSound;
	}

	public Sound getRocketSound() {
		return rocketSound;
	}

	public Sound getDamageSound() {
		return damageSound;
	}

	public BitmapFont getGameOverFont() {
		return gameOverFont;
	}

	public BitmapFont getFpsFont() {
		return fpsFont;
	}
	
	public BitmapFont getAdviceFont() {
		return adviceFont;
	}

	public BitmapFont getScoreFont() {
		return scoreFont;
	}

	public BitmapFont getTitleFont() {
		return titleFont;
	}

	public GlyphLayout getGlyphLayout() {
		return glyphLayout;
	}

	public Texture getBackgroundMenuTexture() {
		return backgroundMenuTexture;
	}


	public Texture getPlayBtnTexture() {
		return playBtnTexture;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		shipAtlas.dispose();
		spaceTexture.dispose();
		planetTexture1.dispose();
		planetTexture2.dispose();
		gameMusic.dispose();
		bossMusic.dispose();
		bossLaserSound.dispose();
		shipLaserSound.dispose();
		enemyLaserSound.dispose();
		damageSound.dispose();
		explosionSound.dispose();
		introMusic.dispose();
		fpsFont.dispose();
		scoreFont.dispose();
		gameOverFont.dispose();
		playBtnTexture.dispose();
		backgroundMenuTexture.dispose();
		titleFont.dispose();
		rocketSound.dispose();
	}

}