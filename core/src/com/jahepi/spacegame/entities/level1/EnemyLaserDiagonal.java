package com.jahepi.spacegame.entities.level1;

import com.badlogic.gdx.math.MathUtils;

public class EnemyLaserDiagonal extends EnemyLaser {

	protected float degrees;
	
	public EnemyLaserDiagonal(float degrees) {
		this.degrees = degrees;
	}

	@Override
	public void update(float deltatime) {
		this.time += deltatime;
		
		if (this.isDestroyed()) {
			if (destroyAnimation.isAnimationFinished(time)) {
				remove = true;
			}
		} else {
			this.position.x -= (this.speed * MathUtils.sinDeg(degrees) * deltatime);
			this.position.y -= (this.speed * MathUtils.cosDeg(degrees) * deltatime);
			this.rectangle.x = this.position.x;
			this.rectangle.y = this.position.y;
		}
	}
	
}
