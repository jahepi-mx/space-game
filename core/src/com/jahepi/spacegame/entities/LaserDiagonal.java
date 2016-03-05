package com.jahepi.spacegame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Resource;

public class LaserDiagonal extends Laser {

	protected float degrees;
	
	public LaserDiagonal(float degrees) {
		this.region = Resource.getInstance().getShipAtlas().findRegion("rocket");
		this.degrees = degrees;
	}
	
	public void render(SpriteBatch batch) {
		if (isDestroyed) {
			this.region = destroyAnimation.getKeyFrame(time);
			batch.draw(region, position.x - (7.0f / 2), position.y - (7.0f / 2), 7.0f / 2, 7.0f / 2, 7.0f, 7.0f, 1.0f, 1.0f, 90.0f);
		} else {
			batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 0.0f);
		}
	}
	
	@Override
	public void update(float deltatime) {
		this.time += deltatime;
		
		if (this.isDestroyed()) {
			if (destroyAnimation.isAnimationFinished(time)) {
				remove = true;
			}
		} else {
			this.position.x += (this.speed * MathUtils.sinDeg(degrees) * deltatime);
			this.position.y += (this.speed * MathUtils.cosDeg(degrees) * deltatime);
			this.rectangle.x = this.position.x;
			this.rectangle.y = this.position.y;
		}
	}
}
