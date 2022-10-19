package sylaires.ctf.display;

import org.bukkit.scheduler.BukkitRunnable;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.GameTimer;
import sylaires.ctf.game.FlagManager;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class RenderHandler {
	
	public static void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(CTFMain.theState == GameState.PLAYING) {
					FlagManager.blueFlag.rerender();
					FlagManager.redFlag.rerender();
					GameTimer.blueNpc.rerender();
					GameTimer.redNpc.rerender();
				}else {
					this.cancel();
				}
			}
		}.runTaskTimer(CTFMain.instance, 0, 300);
	}

}
