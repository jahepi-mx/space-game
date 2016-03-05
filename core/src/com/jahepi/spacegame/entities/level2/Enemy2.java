package com.jahepi.spacegame.entities.level2;

import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.EnemyListener;
import com.jahepi.spacegame.entities.level1.Enemy;

public class Enemy2 extends Enemy {

	public Enemy2(EnemyListener listener) {
		super(listener);
		this.region = Resource.getInstance().getEnemy2Texture();
		this.life = MathUtils.random(1, 2);
		this.lifeLimit = this.life;
		this.nextShotTime = MathUtils.random(0.5f, 1.0f);
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
		
		this.position.y -= (this.speed * deltatime);
		this.degrees += this.degreesVelocity; 
		this.position.x += MathUtils.cosDeg(this.degrees) * this.curveDistance * deltatime;
		this.rectangle.y = this.position.y;
		this.rectangle.x = this.position.x;
	}
}
