package com.jahepi.spacegame.entities.level2;

import com.badlogic.gdx.math.MathUtils;
import com.jahepi.spacegame.Resource;
import com.jahepi.spacegame.entities.EnemyListener;
import com.jahepi.spacegame.entities.level1.Boss;

public class Boss2 extends Boss {

	public Boss2(EnemyListener listener) {
		super(listener);
		this.region = Resource.getInstance().getBoss2Texture();
		this.life = 150;
		this.lifeLimit = this.life;
		this.nextShotTime = MathUtils.random(0.5f, 2.0f);
	}

}
