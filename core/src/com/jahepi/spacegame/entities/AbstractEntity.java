package com.jahepi.spacegame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public abstract class AbstractEntity implements Disposable {
	
	protected Vector2 size;
	protected Vector2 position;
	protected TextureRegion region;
	protected Rectangle rectangle;
	protected float speed;
	
	public AbstractEntity() {
		this.size = new Vector2();
		this.position = new Vector2();
		this.rectangle = new Rectangle();
	}
	
	public Vector2 getSize() {
		return size;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public TextureRegion getRegion() {
		return region;
	}

	public void setRegion(TextureRegion region) {
		this.region = region;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	public abstract void update(float deltatime);
	
	public abstract void render(SpriteBatch batch);
	
	public void debug(ShapeRenderer batch) {
		batch.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	public boolean collide(AbstractEntity entity) {
		return entity.getRectangle().overlaps(this.rectangle);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub		
	}	
}
