package com.jahepi.spacegame.entities.level1;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.AbstractEntity;

public class EnemyLaser extends AbstractEntity {

	protected boolean isDestroyed;
	protected Animation animation;
	protected Animation destroyAnimation;
	protected float time;
	protected boolean remove;
	
	public EnemyLaser() {
		this.animation = new Animation(1.0f/10.0f, Resource.getInstance().getShipAtlas().findRegions("OrangeBlast"), Animation.PlayMode.LOOP);
		this.destroyAnimation = new Animation(1.0f/10.0f, Resource.getInstance().getShipAtlas().findRegions("OrangeBulletExplo"), Animation.PlayMode.NORMAL);
		this.size.x = 2.0f;
		this.size.y = 6.0f;
		
		this.rectangle.width = this.size.x;
		this.rectangle.height = this.size.y;
		this.speed = 20.0f;
	}
	
	public void setXY(float x, float y) {
		this.position.set(x, y);
		this.rectangle.x = this.position.x;
		this.rectangle.y = this.position.y + (size.y / 2);
	}

	public void render(SpriteBatch batch) {
		if (isDestroyed) {
			this.region = destroyAnimation.getKeyFrame(time);
			batch.draw(region, position.x - (7.0f / 2), position.y - (7.0f / 2), 7.0f / 2, 7.0f / 2, 7.0f, 7.0f, 1.0f, 1.0f, 90.0f);
		} else {
			this.region = animation.getKeyFrame(time);
			batch.draw(region, position.x + size.x, position.y + (size.y / 2), size.x / 2, size.y / 2, size.y, size.x, 1.0f, 1.0f, 270.0f);
		}
	}
	
	public void update(float deltatime) {
		this.time += deltatime;
		
		if (this.isDestroyed()) {
			if (destroyAnimation.isAnimationFinished(time)) {
				remove = true;
			}
		} else {
			this.position.y -= (this.speed * deltatime);
			this.rectangle.y = this.position.y;
		}
	}
	
	public boolean isOutOfBounds() {
		if (position.y < 0) {
			return true;
		}
		return false;
	}
	
	public boolean isRemovable() {
		return remove;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.time = 0;
		this.isDestroyed = isDestroyed;
	}
}
