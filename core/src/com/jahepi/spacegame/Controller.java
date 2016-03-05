package com.jahepi.spacegame;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.jahepi.spacegame.entities.Background;
import com.jahepi.spacegame.entities.Item;
import com.jahepi.spacegame.entities.Laser;
import com.jahepi.spacegame.entities.Ship;
import com.jahepi.spacegame.entities.level1.Boss;
import com.jahepi.spacegame.entities.level1.Enemy;
import com.jahepi.spacegame.entities.level1.EnemyLaser;
import com.jahepi.spacegame.levelmanager.LevelController;
import com.jahepi.spacegame.levelmanager.LevelManager;

public class Controller implements Disposable {
	
	private static final float CHANGE_LEVEL_TIME = 10.0f;
	private static final float GAME_OVER_TIME = 2.0f;
	
	private Ship ship;
	private Boss boss;
	private Background background;
	private Array<Laser> lasers;
	private Array<Laser> lasersRemoval;
	private Array<Item> items;
	private Array<Item> itemsRemoval;
	private Array<EnemyLaser> enemyLasers;
	private Array<EnemyLaser> enemyLasersTemp;
	private Array<EnemyLaser> enemyLasersRemoval;
	private Array<Enemy> enemies;
	private Array<Enemy> enemiesRemoval;
	private int points;
	private int enemiesDestroyed;
	private boolean gameOverFlag;
	private float gameOverSecs = 0;
	private float changeLevelSecs = CHANGE_LEVEL_TIME;
	private int level;
	private LevelManager levelManager;
	private LevelController levelController;
	private boolean changeScreenFlag;
	private ControllerListener listener;
	
	public Controller(ControllerListener listener) {
		this.ship = new Ship();
		this.background = new Background();
		this.lasers = new Array<Laser>();
		this.lasersRemoval = new Array<Laser>();
		this.items = new Array<Item>();
		this.itemsRemoval = new Array<Item>();
		this.enemies = new Array<Enemy>();
		this.enemiesRemoval = new Array<Enemy>();
		this.enemyLasers = new Array<EnemyLaser>();
		this.enemyLasersTemp = new Array<EnemyLaser>();
		this.enemyLasersRemoval = new Array<EnemyLaser>();
		this.level = 1;
		this.levelManager = new LevelManager(this);
		this.levelController = this.levelManager.getLevelController(level);
		this.listener = listener;
	}
	
	public void activateBossFight() {
		Resource.getInstance().getGameMusic().stop();
		Resource.getInstance().getBossMusic().play();
		this.background.accelerate();
		enemiesDestroyed = 0;
	}
	
	public void update(float deltatime) {
		
		if (isGameOver()) {
			gameOverSecs += deltatime;
		}
		
		this.changeLevelSecs += deltatime;
		this.ship.update(deltatime);
		
		if (boss != null) {
			if (boss.disappear()) {
				Resource.getInstance().getGameMusic().play();
				Resource.getInstance().getBossMusic().stop();
				this.background.desaccelerate();
				boss = null;
				this.level++;
				this.changeLevelSecs = 0;
				this.levelController = this.levelManager.getLevelController(level);
				listener.onChangeLevel();
			} else {
				this.boss.update(deltatime);
			}
		}
		
		this.background.update(deltatime);
		
		//Gdx.app.log("AcelerometerY", "Y: " + Gdx.input.getAccelerometerY());
		//Gdx.app.log("AcelerometerX", "X: " + Gdx.input.getAccelerometerX());
		
		if (!isGameOver()) {
			if (Gdx.app.getType() == ApplicationType.Android) {
				if (Gdx.input.getAccelerometerX() >= 1.0f) {
					this.ship.left(deltatime);
				} else if (Gdx.input.getAccelerometerX() < -1.0f) {
					this.ship.right(deltatime);
				}
			}
			
			if (Gdx.input.isKeyPressed(Keys.A)) {
				this.ship.left(deltatime);
			} else if (Gdx.input.isKeyPressed(Keys.D)) {
				this.ship.right(deltatime);
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) {
			
			if (isGameOver()) {
				if (this.gameOverSecs >= GAME_OVER_TIME) {
					changeScreenFlag = true;
				}
			} else {
				Array<Laser> shipLasers = this.ship.shoot();
				for (Laser laser : shipLasers) {
					lasers.add(laser);
				}
			}
		}
		
		if (changeLevelSecs < CHANGE_LEVEL_TIME && !isGameOver()) {
			return;
		}
		
		this.levelController.update(deltatime);
		
		for (Enemy enemy : enemies) {
			for (Laser laser : lasers) {
				if (laser.collide(enemy) && !laser.isDestroyed()) {
					laser.setDestroyed(true);
					enemy.damage(1);
				}
			}
			enemy.update(deltatime);
			if (enemy.isDestroyed() == false) {
				if (enemy.collide(ship)) {
					if (ship.isDestroyed() == false) {
						enemy.destroy();
						ship.damage(1);
						if (ship.isDestroyed()) {
							gameOverFlag = true;
						}
					}
				}
			}
			if (enemy.isOutOfBounds() || enemy.disappear()) {
				this.enemiesRemoval.add(enemy);
			}
		}
		this.enemies.removeAll(this.enemiesRemoval, true);
		this.enemiesRemoval.clear();
		
		for (Laser laser : lasers) {
			if (boss != null) {
				if (laser.collide(boss) && !laser.isDestroyed()) {
					laser.setDestroyed(true);
					boss.damage(1);
				}
			}
			laser.update(deltatime);
			if (laser.isOutOfBounds() || laser.isRemovable()) {
				this.lasersRemoval.add(laser);
			}
		}
		this.lasers.removeAll(this.lasersRemoval, true);
		this.lasersRemoval.clear();
		
		for (Item item : items) {
			if (item.collide(ship) && !ship.isDestroyed()) {
				ship.setItem(item);
				item.setDestroyed(true);
			}
			item.update(deltatime);
			if (item.isOutOfBounds() || item.isRemovable()) {
				this.itemsRemoval.add(item);
			}
		}
		this.items.removeAll(this.itemsRemoval, true);
		this.itemsRemoval.clear();
		
		// Push requested enemy lasers to the queue
		for (EnemyLaser laser : enemyLasersTemp) {
			enemyLasers.add(laser);
		}
		enemyLasersTemp.clear();
		
		for (EnemyLaser laser : enemyLasers) {
			laser.update(deltatime);
			if (laser.collide(ship) && !laser.isDestroyed()) {
				if (ship.isDestroyed() == false) {
					ship.damage(1);
					laser.setDestroyed(true);
					if (ship.isDestroyed()) {
						gameOverFlag = true;
					}
				}
			}
			if (laser.isOutOfBounds() || laser.isRemovable()) {
				this.enemyLasersRemoval.add(laser);
			}
		}
		this.enemyLasers.removeAll(this.enemyLasersRemoval, true);
		this.enemyLasersRemoval.clear();
		
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public Boss getBoss() {
		return boss;
	}
	
	public void setBoss(Boss boss) {
		this.boss = boss;
	}
	
	public Array<Laser> getLasers() {
		return this.lasers;
	}
	
	public Array<Item> getItems() {
		return this.items;
	}
	
	public Array<EnemyLaser> getEnemyLasers() {
		return this.enemyLasers;
	}
	
	public Array<Enemy> getEnemies() {
		return this.enemies;
	}
	
	public Background getBackground() {
		return background;
	}
	
	public int getPoints() {
		return this.points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public int getEnemiesDestroyed() {
		return this.enemiesDestroyed;
	}
	
	public void setEnemiesDestroyed(int enemiesDestroyed) {
		this.enemiesDestroyed = enemiesDestroyed;
	}
	
	public boolean isGameOver() {
		return gameOverFlag;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public Array<EnemyLaser> getEnemyLasersTemp() {
		return enemyLasersTemp;
	}

	public void setEnemyLasersTemp(Array<EnemyLaser> enemyLasersTemp) {
		this.enemyLasersTemp = enemyLasersTemp;
	}
	
	public boolean isChangingLevel() {
		return this.changeLevelSecs < CHANGE_LEVEL_TIME && !isGameOver();
	}

	public boolean isChangeScreenFlag() {
		return changeScreenFlag;
	}

	public void reset() {
		this.points = 0;
		this.level = 1;
		this.changeLevelSecs = 0;
		this.ship.reset();
		this.lasers.clear();
		this.lasersRemoval.clear();
		this.items.clear();
		this.itemsRemoval.clear();
		this.gameOverFlag = false;
		this.gameOverSecs = 0;
		this.enemies.clear();
		this.enemiesRemoval.clear();
		this.enemyLasers.clear();
		this.enemyLasersRemoval.clear();
		this.enemyLasersTemp.clear();
		this.enemiesDestroyed = 0;
		this.boss = null;
		Resource.getInstance().getGameMusic().play();
		Resource.getInstance().getBossMusic().stop();
		this.background.desaccelerate();
		this.levelController = this.levelManager.getLevelController(level);
	}
	
	@Override
	public void dispose() {
		ship = null;
		boss = null;
		background = null;
		items = null;
		itemsRemoval = null;
		lasers = null;
		lasersRemoval = null;
		enemyLasers = null;
		enemyLasersTemp = null;
		enemyLasersRemoval = null;
		enemies = null;
		enemiesRemoval = null;
		levelManager = null;
		levelController = null;
	}
}