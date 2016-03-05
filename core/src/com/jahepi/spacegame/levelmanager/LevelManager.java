package com.jahepi.spacegame.levelmanager;

import com.jahepi.spacegame.Controller;

public final class LevelManager {

	private Controller controller;
	
	public LevelManager(Controller controller) {
		this.controller = controller;
	}
	
	public LevelController getLevelController(int level) {
		switch(level) {
			case 1: return new Level1Controller(this.controller);
			default: return new Level2Controller(this.controller);
		}
	}
}
