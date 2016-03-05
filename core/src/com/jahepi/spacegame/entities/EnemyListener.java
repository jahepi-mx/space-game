package com.jahepi.spacegame.entities;

public interface EnemyListener {
	public void onEnemyDamage();
	public void onEnemyDestroy();
	public void onEnemyShoot(float x, float y, float speed);
	public void onBossEnemyShoot(float x, float y, float speed);
}
