package com.jahepi.spacegame.entities.level1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.AbstractEntity;
import com.jahepi.spacegame.entities.EnemyListener;

public class Enemy extends AbstractEntity {
	
	protected static final float DAMAGE_TIME = 0.1f;
	
	protected float nextShotTime;
	protected float nextShotTimeSecs;
	protected float curveDistance;
	protected float degrees;
	protected float degreesVelocity;
	protected int life;
	protected int lifeLimit;
	protected float damageSecs = DAMAGE_TIME;
	protected EnemyListener listener;
	protected boolean disappear;
	protected Animation explosionAnimation;
	protected float time;
	
	public Enemy(EnemyListener listener) {
		this.size.x = 10.0f;
		this.size.y = 10.0f;
		this.region = Resource.getInstance().getEnemyTexture();
		this.curveDistance = MathUtils.random(this.size.x, Config.GAME_WIDTH) * MathUtils.randomSign();
		this.position.y = Config.GAME_HEIGHT + this.size.y;
		this.position.x = (Config.GAME_WIDTH / 2) - (this.size.x / 2);
		this.speed = MathUtils.random(10.0f, 40.0f);
		this.degreesVelocity = 2.0f;
		this.rectangle.width = this.size.x;
		this.rectangle.height = this.size.y;
		this.life = MathUtils.random(2, 4);
		this.lifeLimit = this.life;
		this.listener = listener;
		this.nextShotTime = MathUtils.random(0.5f, 2.0f);
		this.explosionAnimation = new Animation(1.0f/11.0f, Resource.getInstance().getShipAtlas().findRegions("Explo"), Animation.PlayMode.NORMAL);
	}

	@Override
	public void update(float deltatime) {
		// TODO Auto-generated method stub
		this.time += deltatime;
		this.damageSecs += deltatime;
		
		if (this.isDestroyed()) {
			if (explosionAnimation.isAnimationFinished(time)) {
				disappear = true;
			}
		}
		
		if (isDestroyed() == false) {
			this.nextShotTimeSecs += deltatime;
			if (this.nextShotTimeSecs >= this.nextShotTime) {
				this.nextShotTimeSecs = 0;
				this.nextShotTime = MathUtils.random(0.5f, 2.0f);
				Resource.getInstance().getEnemyLaserSound().play();
				this.listener.onEnemyShoot(position.x + (size.x / 2), position.y - (size.y / 2), this.speed + (this.speed * 0.8f));
			}
		}
		
		this.position.y -= (this.speed * deltatime);
		this.degrees += this.degreesVelocity; 
		this.position.x += MathUtils.cosDeg(this.degrees) * this.curveDistance * deltatime;
		this.rectangle.y = this.position.y;
		this.rectangle.x = this.position.x;
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub
		if (this.damageSecs < DAMAGE_TIME) {
			batch.setColor(1, 0, 0, 1);
		}
		if (isDestroyed()) {
			this.region = explosionAnimation.getKeyFrame(time);
		}
		batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 0.0f);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void drawLifeBar(ShapeRenderer shape) {
		if (isDestroyed() == false) {
			float ratio = (float) this.life / (float) this.lifeLimit;
			shape.rect(position.x, position.y + size.x + 1.0f, this.size.x * ratio, 0.5f, Color.RED, Color.RED, Color.RED, Color.RED);
		}
	}
	
	public void destroy() {
		this.damage(this.life);
	}

	public boolean isDestroyed() {
		return this.life <= 0;
	}
	
	public boolean isOutOfBounds() {
		if (position.y < -this.size.y) {
			return true;
		}
		return false;
	}
	
	public boolean disappear() {
		return disappear;
	}
	
	public void damage(int points) {
		if (this.life > 0) {
			this.damageSecs  = 0;
			this.life -= points;
			this.listener.onEnemyDamage();
			if (this.life <= 0) {
				this.time = 0;
				this.listener.onEnemyDestroy();
				Resource.getInstance().getExplosionSound().play(0.6f);
			} else {
				Resource.getInstance().getDamageSound().play();
			}
		}
	}
}
