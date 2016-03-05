package com.jahepi.spacegame.entities.level2;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.level1.EnemyLaserDiagonal;

public class EnemyLaserBlast extends EnemyLaserDiagonal {

	public EnemyLaserBlast(float degrees) {
		super(degrees);
		this.animation = new Animation(1.0f/15.0f, Resource.getInstance().getShipAtlas().findRegions("OrangeSpin"), Animation.PlayMode.LOOP);
		this.size.x = 3.0f;
		this.size.y = 3.0f;
		
		this.rectangle.width = this.size.x;
		this.rectangle.height = this.size.y;
	}
	
	public void render(SpriteBatch batch) {
		if (isDestroyed) {
			this.region = destroyAnimation.getKeyFrame(time);
			batch.draw(region, position.x - (7.0f / 2), position.y - (7.0f / 2), 7.0f / 2, 7.0f / 2, 7.0f, 7.0f, 1.0f, 1.0f, 90.0f);
		} else {
			this.region = animation.getKeyFrame(time);
			batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 270.0f);
		}
	}
}
