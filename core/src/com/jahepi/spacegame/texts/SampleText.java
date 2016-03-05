package com.jahepi.spacegame.texts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;

public class SampleText {
	
	private static final int PLAYING_TIME = 11;
	private static final int FADE_OUT_TIME = 10;
	
	private BitmapFont font;
	private TextureRegion region;
	private GlyphLayout glyphLayout;
	private String message = "A Jedi uses the Force for\nknowledge and defense,\nnever for attack.";
	private ParticleEffect effect;
	private float playingTime = PLAYING_TIME;
	private float alpha = 0;
	
	public SampleText() {
		font = Resource.getInstance().getAdviceFont();
		glyphLayout = Resource.getInstance().getGlyphLayout();
		region = new TextureRegion(Resource.getInstance().getYodaTexture());
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("particles/particle"), Gdx.files.internal("particles"));
		effect.setPosition(Config.GUI_WIDTH / 2, 0);
		effect.scaleEffect(5);
	}
	
	public void render(SpriteBatch batch) {
		if (playingTime < PLAYING_TIME) {
			effect.draw(batch);
	
			Color fontColor = batch.getColor();
			font.setColor(fontColor.r, fontColor.g, fontColor.b, alpha);
			glyphLayout.setText(font, message);
			font.draw(batch, glyphLayout, (Config.GUI_WIDTH / 2) - (glyphLayout.width / 2), (Config.GUI_HEIGHT / 2) + (glyphLayout.height / 2));
			font.setColor(fontColor);
			
			Color imageColor = batch.getColor();
			batch.setColor(imageColor.r, imageColor.g, imageColor.b, alpha);
			batch.draw(region, Config.GUI_WIDTH - 150, 0, 0, 0, 150, 150, 1, 1, 0);
			batch.setColor(imageColor);
		}
	}
	
	public void update(float deltatime) {
		if (playingTime < PLAYING_TIME) {			
			if (playingTime >= FADE_OUT_TIME) {
				alpha -= deltatime;
			} else {
				if (alpha < 1) {
					alpha += deltatime;
				}
				if (alpha > 1) {
					alpha = 1;
				}
			}
			playingTime += deltatime;
			effect.update(deltatime);
		}
	}
	
	public void play() {
		playingTime = 0;
		alpha = 0;
		effect.reset();
		effect.start();
	}
}
