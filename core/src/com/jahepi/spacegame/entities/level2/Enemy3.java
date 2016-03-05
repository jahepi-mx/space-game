package com.jahepi.spacegame.entities.level2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.EnemyListener;
import com.jahepi.spacegame.entities.Ship;
import com.jahepi.spacegame.entities.level1.Enemy;

public class Enemy3 extends Enemy {

	private Ship ship;
	
	public Enemy3(Ship ship, EnemyListener listener) {
		super(listener);
		this.region = Resource.getInstance().getEnemy2Texture();
		this.life = MathUtils.random(2, 3);
		this.lifeLimit = this.life;
		this.nextShotTime = MathUtils.random(0.5f, 1.0f);
		this.ship = ship;
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
				if (this.position.y > 30) {
					this.listener.onEnemyShoot(position.x + (size.x / 2), position.y - (size.y / 2), this.speed + (this.speed * 0.8f));
				}
			}
		}
		float x = this.ship.getPosition().x - this.position.x;
		float y = this.ship.getPosition().y - this.position.y;
		if (this.ship.isDestroyed()) {
			//x = this.ship.getPosition().x - this.position.x - 50;
			y = this.ship.getPosition().y - this.position.y - 50;
		}
		float radians = MathUtils.atan2(x, y);
		float degrees = radians * MathUtils.radiansToDegrees + 180;
		if (!isDestroyed()) {
			this.degrees = degrees * -1;
		}
		this.position.y -= (this.speed * MathUtils.cosDeg(degrees) * deltatime);
		this.position.x -= (this.speed * MathUtils.sinDeg(degrees) * deltatime);
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
		batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, this.degrees);
		batch.setColor(1, 1, 1, 1);
	}
}