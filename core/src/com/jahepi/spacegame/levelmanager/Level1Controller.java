package com.jahepi.spacegame.levelmanager;

import com.jahepi.spacegame.Controller;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.EnemyListener;
import com.jahepi.spacegame.entities.Item;
import com.jahepi.spacegame.entities.level1.Boss;
import com.jahepi.spacegame.entities.level1.Enemy;
import com.jahepi.spacegame.entities.level1.EnemyLaser;
import com.jahepi.spacegame.entities.level1.EnemyLaserDiagonal;

public class Level1Controller implements LevelController, EnemyListener {

	private static final float SPAWN_ENEMY_TIME = 2.0f;
	private static final float SPAWN_ITEM_TIME = 5.0f;
	private static final int DESTROYED_ENEMIES_TO_SHOW_BOSS = 20;
	
	private Controller controller;
	private float spawnTime;
	private float itemTime;
	
	public Level1Controller(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void update(float deltatime) {
		this.spawnTime += deltatime;
		this.itemTime += deltatime;
		
		if (this.spawnTime > SPAWN_ENEMY_TIME && this.controller.getBoss() == null) {
			this.spawnTime = 0;
			Enemy enemy = new Enemy(this);
			this.controller.getEnemies().add(enemy);
		}
		
		if (this.itemTime > SPAWN_ITEM_TIME) {
			this.itemTime = 0;
			Item item = new Item();
			this.controller.getItems().add(item);
		}
		
		if (this.controller.getEnemiesDestroyed() == DESTROYED_ENEMIES_TO_SHOW_BOSS) {
			Boss boss = new Boss(this);
			this.controller.activateBossFight();
			this.controller.setBoss(boss);
		}
	}

	@Override
	public void onEnemyDamage() {
		int points = this.controller.getPoints() + 1;
		this.controller.setPoints(points);
	}

	@Override
	public void onEnemyDestroy() {
		int number = this.controller.getEnemiesDestroyed() + 1;
		this.controller.setEnemiesDestroyed(number);	
	}

	@Override
	public void onEnemyShoot(float x, float y, float speed) {
		// TODO Auto-generated method stub
		EnemyLaser laser = new EnemyLaser();
		laser.setXY(x,  y);
		laser.setSpeed(speed);
		this.controller.getEnemyLasersTemp().add(laser);	
	}

	@Override
	public void onBossEnemyShoot(float x, float y, float speed) {
		Resource.getInstance().getBossLaserSound().play(0.80f);
		EnemyLaser laser = new EnemyLaser();
		laser.setXY(x,  y);
		laser.setSpeed(speed);
		this.controller.getEnemyLasersTemp().add(laser);
		
		EnemyLaserDiagonal laser2 = new EnemyLaserDiagonal(25.0f);
		laser2.setXY(x,  y);
		laser2.setSpeed(speed);
		this.controller.getEnemyLasersTemp().add(laser2);
		
		EnemyLaserDiagonal laser3 = new EnemyLaserDiagonal(-25.0f);
		laser3.setXY(x,  y);
		laser3.setSpeed(speed);
		this.controller.getEnemyLasersTemp().add(laser3);	
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		this.spawnTime = 0;
		this.itemTime = 0;
	}
}