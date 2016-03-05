package com.jahepi.spacegame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;

public class Ship extends AbstractEntity {
	
	protected static final float DAMAGE_TIME = 0.1f;
	protected static final float POWER_UP_TIME = 4.0f;
	private static float SHOOT_SECONDS_LIMIT = 0.2f;
	
	private float shootSecs;
	private boolean powerUpActivated;
	private float powerUpSecs;
	private float friction;
	private float speedTemp;
	private Animation explosionAnimation;
	private float time;
	private boolean isDestroyed;
	private int life;
	protected int lifeLimit;
	protected float damageSecs = DAMAGE_TIME;
	protected Array<Laser> lasers;
	
	public Ship() {
		this.region = Resource.getInstance().getShipTexture();
		this.explosionAnimation = new Animation(1.0f/11.0f, Resource.getInstance().getShipAtlas().findRegions("Explo"), Animation.PlayMode.NORMAL);
		this.size.x = 6.0f;
		this.size.y = 6.0f;
		this.position.y = Config.GAME_HEIGHT / 11;
		this.position.x = (Config.GAME_WIDTH / 2) - (this.size.x / 2);
		
		this.rectangle.width = this.size.x;
		this.rectangle.height = this.size.y;
		this.rectangle.x = this.position.x;
		this.rectangle.y = this.position.y;
		this.speed = 20.0f;
		this.friction = 0.99f;
		this.life = 10;
		this.lifeLimit = this.life;
		this.lasers = new Array<Laser>();
	}
	
	public void render(SpriteBatch batch) {
		
		if (this.damageSecs < DAMAGE_TIME) {
			batch.setColor(1, 0, 0, 1);
		}
		
		if (isDestroyed) {
			if (explosionAnimation.isAnimationFinished(time)) {
				return;
			}
		}
		batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 0.0f);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void update(float deltatime) {
		this.shootSecs += deltatime;
		this.damageSecs += deltatime;
		this.powerUpSecs += deltatime;
		this.time += deltatime;
		
		if (powerUpActivated) {
			if (powerUpSecs >= POWER_UP_TIME) {
				powerUpActivated = false;
				SHOOT_SECONDS_LIMIT = 0.2f;
			}
		}
		
		if (isDestroyed) {
			this.region = explosionAnimation.getKeyFrame(time);
		}
		
		if (this.position.x >= (Config.GAME_WIDTH - this.size.x)) {
			this.position.x = Config.GAME_WIDTH - this.size.x;
			this.rectangle.x = this.position.x;
			return;
		}
		
		if (this.position.x <= 0) {
			this.position.x = 0;
			this.rectangle.x = this.position.x;
			return;
		}
		
		this.speedTemp *= this.friction;
		this.position.x += (this.speedTemp * deltatime);
		this.rectangle.x = this.position.x;
	}
	
	public Array<Laser> shoot() {
		lasers.clear();
		if (shootSecs >= SHOOT_SECONDS_LIMIT) {
			shootSecs = 0;
			if (powerUpActivated) {
				Resource.getInstance().getRocketSound().play(0.60f);
				LaserDiagonal laser1 = new LaserDiagonal(0.0f);
				laser1.setXY(position.x + (size.x / 2), position.y + size.y + 2.0f);
				lasers.add(laser1);
				LaserDiagonal laser2 = new LaserDiagonal(15.0f);
				laser2.setXY(position.x + (size.x / 2), position.y + size.y + 2.0f);
				lasers.add(laser2);
				LaserDiagonal laser3 = new LaserDiagonal(-15.0f);
				laser3.setXY(position.x + (size.x / 2), position.y + size.y + 2.0f);
				lasers.add(laser3);
			} else {
				Resource.getInstance().getShipLaserSound().play();
				Laser laser = new Laser();
				laser.setXY(position.x + (size.x / 2), position.y + size.y + 2.0f);
				lasers.add(laser);
			}
		}
		return lasers;
	}

	public void right(float deltatime) {
		this.speedTemp = 4.0f;
		this.position.x += (this.speed * deltatime);
		this.rectangle.x = this.position.x;
	}
	
	public void left(float deltatime) {
		this.speedTemp = -4.0f;
		this.position.x -= (this.speed * deltatime);
		this.rectangle.x = this.position.x;
	}
	
	private void destroy() {
		time = 0;
		Resource.getInstance().getExplosionSound().play(0.6f);
		isDestroyed = true;
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public void damage(int points) {
		if (this.life > 0) {
			this.damageSecs  = 0;
			this.life -= points;
			if (this.life <= 0) {
				destroy();
			} else {
				Resource.getInstance().getDamageSound().play();
			}
		}
	}
	
	public void drawLifeBar(ShapeRenderer shape) {
		if (isDestroyed() == false) {
			float ratio = (float) this.life / (float) this.lifeLimit;
			shape.rect(position.x, position.y - (size.y / 3), this.size.x * ratio, 0.5f, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
		}
	}
	
	public void setItem(Item item) {
		switch (item.getType()) {
			case LIFE:
				this.life = this.lifeLimit;
				break;
			case POWER_UP:
				SHOOT_SECONDS_LIMIT = 0.15f;
				powerUpActivated = true;
				this.powerUpSecs = 0;
		}
	}
	
	public void reset() {
		this.time = 0;
		this.region = Resource.getInstance().getShipTexture();
		this.isDestroyed = false;
		this.damageSecs = DAMAGE_TIME;
		this.life = this.lifeLimit;
	}
}