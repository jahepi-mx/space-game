package com.jahepi.spacegame.levelmanager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.jahepi.spacegame.Controller;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.EnemyListener;
import com.jahepi.spacegame.entities.Item;
import com.jahepi.spacegame.entities.level1.Boss;
import com.jahepi.spacegame.entities.level1.Enemy;
import com.jahepi.spacegame.entities.level1.EnemyLaser;
import com.jahepi.spacegame.entities.level1.EnemyLaserDiagonal;
import com.jahepi.spacegame.entities.level2.Boss2;
import com.jahepi.spacegame.entities.level2.Enemy3;
import com.jahepi.spacegame.entities.level2.EnemyLaserBlast;

public class Level2Controller implements LevelController, EnemyListener {

	private static final float SPAWN_ENEMY_TIME = 2.0f;
	private static final float SPAWN_ITEM_TIME = 20.0f;
	private static final int DESTROYED_ENEMIES_TO_SHOW_BOSS = 20;
	private static final int BOSS_SHOOTS = 3;
	private static final float BOSS_SHOOT_TIME = 0.2f;
	
	private Controller controller;
	private float spawnTime;
	private int bossShoots = BOSS_SHOOTS;
	private float bossShootsSecs;
	private float speed;
	private float itemTime;
	
	public Level2Controller(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void update(float deltatime) {
		// TODO Auto-generated method stub
		this.spawnTime += deltatime;
		this.bossShootsSecs += deltatime;
		this.itemTime += deltatime;
		
		if (this.spawnTime > SPAWN_ENEMY_TIME && this.controller.getBoss() == null) {
			this.spawnTime = 0;
			Enemy enemy = new Enemy3(this.controller.getShip(), this);
			this.controller.getEnemies().add(enemy);
		}
		
		if (this.itemTime > SPAWN_ITEM_TIME) {
			this.itemTime = 0;
			Item item = new Item();
			this.controller.getItems().add(item);
		}
		
		if (this.controller.getEnemiesDestroyed() == DESTROYED_ENEMIES_TO_SHOW_BOSS) {
			Boss boss = new Boss2(this);
			this.controller.activateBossFight();
			this.controller.setBoss(boss);
		}
		
		if (this.bossShoots < BOSS_SHOOTS) {
			Boss boss = this.controller.getBoss();
			if (boss != null) {
				if (this.bossShootsSecs >= BOSS_SHOOT_TIME) {
					Resource.getInstance().getBossLaserSound().play(0.80f);
					Vector2 bossPosition = boss.getPosition();
					Vector2 position = this.controller.getShip().getPosition();
					float tempX = position.x - (bossPosition.x + (boss.getSize().x / 2));
					float tempY = position.y - bossPosition.y;
					float radians = MathUtils.atan2(tempX, tempY);
					float degrees = radians * MathUtils.radiansToDegrees;
					
					EnemyLaserBlast laser = new EnemyLaserBlast(degrees + 180);
					laser.setXY(bossPosition.x + (boss.getSize().x / 2),  bossPosition.y);
					laser.setSpeed(speed);
					this.controller.getEnemyLasersTemp().add(laser);
					this.bossShootsSecs = 0;
					this.bossShoots++;
				}
			}
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
		Vector2 position = this.controller.getShip().getPosition();
		float tempX = position.x - x;
		float tempY = position.y - y;
		float radians = MathUtils.atan2(tempX, tempY);
		float degrees = radians * MathUtils.radiansToDegrees;
		
		EnemyLaser laser = new EnemyLaserDiagonal(degrees + 180);
		laser.setXY(x,  y);
		laser.setSpeed(speed);
		this.controller.getEnemyLasersTemp().add(laser);	
	}

	@Override
	public void onBossEnemyShoot(float x, float y, float speed) {
		// TODO Auto-generated method stub
		this.speed = speed;
		this.bossShoots = 0;
		this.bossShootsSecs = 0;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		this.spawnTime = 0;
		this.itemTime = 0;
	}
}