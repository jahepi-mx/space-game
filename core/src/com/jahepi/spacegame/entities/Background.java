package com.jahepi.spacegame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Config;
import com.jahepi.spacegame.Resource;

public class Background {

	private SpaceBackground space1;
	private SpaceBackground space2;
	private PlanetBackground planet1;
	private PlanetBackground planet2;
	
	public Background() {
		this.space1 = new SpaceBackground("Space1", false);
		this.space2 = new SpaceBackground("Space2", true);
		this.space1.setSpaceBackground(this.space2);
		this.space2.setSpaceBackground(this.space1);
		this.space2.getPosition().y += this.space1.getSize().y;
		this.planet1 = new PlanetBackground(Resource.getInstance().getPlanetTexture1(), 10, 30);
		this.planet2 = new PlanetBackground(Resource.getInstance().getPlanetTexture2(), 5, 10);
	}
	
	public void accelerate() {
		this.space1.accelerate();
		this.space2.accelerate();
		this.planet1.accelerate();
		this.planet2.accelerate();
	}
	
	public void desaccelerate() {
		this.space1.desaccelerate();
		this.space2.desaccelerate();
		this.planet1.desaccelerate();
		this.planet2.desaccelerate();
	}

	public void update(float deltatime) {
		this.space1.update(deltatime);
		this.space2.update(deltatime);
		this.planet1.update(deltatime);
		this.planet2.update(deltatime);
	}
	
	public void render(SpriteBatch batch) {
		this.space1.render(batch);
		this.space2.render(batch);
		this.planet1.render(batch);
		this.planet2.render(batch);
	}
	
	public void debug(ShapeRenderer shape) {
		this.space1.debug(shape);
		this.space2.debug(shape);
		this.planet1.debug(shape);
		this.planet2.debug(shape);
	}
}

class PlanetBackground extends AbstractEntity {

	private static final float MAX_ACCELERATION = 50.0f;
	
	private float acceleration;
	private boolean isAccelerated;
	private float pauseSecs;
	private float timeSecs;
	
	public PlanetBackground(Texture texture, int minSize, int maxSize) {
		this.speed = 20.0f;
		this.region = new TextureRegion(texture);
		int size = MathUtils.random(minSize, maxSize);
		this.size.x = size;
		this.size.y = size;
		this.pauseSecs = MathUtils.random(1, 5);
		this.position.y = Config.GAME_HEIGHT + this.size.y;
		this.position.x = MathUtils.random(0, Config.GAME_WIDTH);
	}
	
	@Override
	public void update(float deltatime) {
		this.timeSecs += deltatime;
		if (this.timeSecs > this.pauseSecs) {
			if (this.position.y < -this.size.y) {
				int size = MathUtils.random(10, 40);
				this.size.x = size;
				this.size.y = size;
				this.timeSecs = 0;
				this.pauseSecs = MathUtils.random(1, 5);
				this.position.x = MathUtils.random(0, Config.GAME_WIDTH);
				this.position.y = Config.GAME_HEIGHT + this.size.y;
			}
			
			if (isAccelerated) {
				this.acceleration += 2.0f;
				if (this.acceleration >= MAX_ACCELERATION) {
					this.acceleration = MAX_ACCELERATION;
				}
			} else {
				this.acceleration -= 2.0f;
				if (this.acceleration <= 0) {
					this.acceleration = 0;
				}
			}
			this.position.y -= (this.acceleration * deltatime);
			this.position.y -= (this.speed * deltatime);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		if (this.timeSecs > this.pauseSecs) {
			batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 0.0f);
		}
	}
	
	public void accelerate() {
		this.isAccelerated = true;
	}
	
	public void desaccelerate() {
		this.isAccelerated = false;
	}
}

class SpaceBackground extends AbstractEntity {

	private static final float MAX_ACCELERATION = 150.0f;
	
	private String name;
	private float origY;
	private SpaceBackground spaceBackground;
	private float acceleration;
	private boolean isAccelerated;
	
	public SpaceBackground(String name, boolean flipY) {
		this.name = name;
		this.speed = 80.0f;
		this.region = new TextureRegion(Resource.getInstance().getSpaceTexture());
		this.size.x = Config.GAME_WIDTH;
		this.size.y = Config.GAME_HEIGHT * 4;
		this.position.y = 0;
		this.position.x = (this.size.x / 2) - (this.size.x / 2);
		this.origY = this.position.y + this.size.y;
		this.region.flip(false, flipY);
	}
	
	public void setSpaceBackground(SpaceBackground spaceBackground) {
		this.spaceBackground = spaceBackground;
	}

	public void setOrigY(float origY) {
		this.origY = origY;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(region, position.x, position.y, size.x / 2, size.y / 2, size.x, size.y, 1.0f, 1.0f, 0.0f);
	}
	
	public void update(float deltatime) {
		
		if (this.position.y < -this.size.y) {
			this.position.y = this.origY;
			spaceBackground.getPosition().y = 0;
		}
		
		if (isAccelerated) {
			this.acceleration += 2.0f;
			if (this.acceleration >= MAX_ACCELERATION) {
				this.acceleration = MAX_ACCELERATION;
			}
		} else {
			this.acceleration -= 2.0f;
			if (this.acceleration <= 0) {
				this.acceleration = 0;
			}
		}
		this.position.y -= (this.acceleration * deltatime);
		this.position.y -= (this.speed * deltatime);
	}
	
	public void accelerate() {
		this.isAccelerated = true;
	}
	
	public void desaccelerate() {
		this.isAccelerated = false;
	}
	
	public String getName() {
		return this.name;
	}
}
