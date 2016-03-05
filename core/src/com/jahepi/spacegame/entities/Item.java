package com.jahepi.spacegame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;

public class Item extends AbstractEntity {

	private static final float MAX_SIZE = 10.0f;
	private static final float MIN_SIZE = 5.0f;
	
	private boolean remove;
	private boolean isDestroyed;
	private TYPE type;
	private Vector2 sizeTemp;
	
	public static enum TYPE {
		POWER_UP,
		LIFE,
	}
	
	private static final TYPE[] VALUES = TYPE.values();
	private static final int SIZE = VALUES.length - 1;
	
	public Item() {
		this.size.x = 5.0f;
		this.size.y = 5.0f;
		this.rectangle.width = this.size.x;
		this.rectangle.height = this.size.y;
		this.speed = 40.0f;
		this.position.x = MathUtils.random(this.size.x, Config.GAME_WIDTH - this.size.x);
		this.position.y = Config.GAME_HEIGHT + this.size.y; 
		this.rectangle.x = this.position.x - (this.size.x / 2);
		this.sizeTemp = new Vector2(MAX_SIZE, MAX_SIZE);
		
		this.type = VALUES[MathUtils.random(0, SIZE)];
		TextureAtlas atlas = Resource.getInstance().getShipAtlas();
		switch (type) {
			case POWER_UP:
				this.region = atlas.findRegion("Energy");
				break;
			case LIFE:
				this.region = atlas.findRegion("Shield");
				break;
		}
	}

	@Override
	public void update(float deltatime) {
		if (!this.isDestroyed()) {
			this.size.lerp(this.sizeTemp, 0.09f);
			this.position.y -= (this.speed * deltatime);
			this.rectangle.y = this.position.y - (this.size.y / 2);
			this.rectangle.width = this.size.x;
			this.rectangle.height = this.size.y;
			if (this.size.x >= (MAX_SIZE - 0.5f)) {
				this.sizeTemp.set(MIN_SIZE - 0.5f, MIN_SIZE  - 0.5f);
			}
			if (this.size.x < MIN_SIZE) {
				this.sizeTemp.set(MAX_SIZE, MAX_SIZE);
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(region, position.x - (size.y / 2), position.y - (size.y / 2), size.x / 2, size.y / 2, size.y, size.x, 1.0f, 1.0f, 0.0f);
	}
	
	public boolean isOutOfBounds() {
		if (position.y < -this.size.y) {
			return true;
		}
		return false;
	}
	
	public TYPE getType() {
		return type;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
		this.remove = isDestroyed;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public boolean isRemovable() {
		return remove;
	}
}
