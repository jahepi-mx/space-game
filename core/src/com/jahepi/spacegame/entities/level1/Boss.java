package com.jahepi.spacegame.entities.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.EnemyListener;

public class Boss extends Enemy {

	protected static final float CHANGE_DIRECTION = 3.0f;
	
	protected float changeDirectionSecs;
	
	public Boss(EnemyListener listener) {
		super(listener);
		this.size.x = 25.0f;
		this.size.y = 25.0f;
		this.region = Resource.getInstance().getBossTexture();
		this.curveDistance = -20.0f;
		this.position.y = Config.GAME_HEIGHT + (this.size.y / 2);
		this.position.x = (Config.GAME_WIDTH / 2) - (this.size.x / 2);
		this.speed = 5.0f;
		this.degreesVelocity = 2.0f;
		this.rectangle.width = this.size.x;
		this.rectangle.height = this.size.y;
		this.life = 100;
		this.lifeLimit = this.life;
		this.nextShotTime = MathUtils.random(0.5f, 2.0f);
	}

	@Override
	public void update(float deltatime) {
		// TODO Auto-generated method stub
		this.time += deltatime;
		this.damageSecs += deltatime;
		this.changeDirectionSecs += deltatime;
		
		if (this.changeDirectionSecs > CHANGE_DIRECTION) {
			this.changeDirectionSecs = 0;
			this.curveDistance *= MathUtils.randomSign();
		}
		
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
				this.listener.onBossEnemyShoot(position.x + (size.x / 2), position.y, 50.0f);
			}
		}
		
		if (this.position.y > 47) {
			this.position.y -= (this.speed * deltatime);
		}
		this.degrees += this.degreesVelocity; 
		this.position.x += MathUtils.sinDeg(this.degrees) * this.curveDistance * deltatime;
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
		batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 180.0f);
		batch.setColor(1, 1, 1, 1);
	}
	
	public void damage(int points) {
		if (this.life > 0) {
			this.damageSecs  = 0;
			this.life -= points;
			this.listener.onEnemyDamage();
			if (this.life <= 0) {
				this.time = 0;
				Resource.getInstance().getExplosionSound().play(0.6f);
			} else {
				Resource.getInstance().getDamageSound().play();
			}
		}
	}
}
