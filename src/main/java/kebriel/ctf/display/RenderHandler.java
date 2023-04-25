package kebriel.ctf.display;

import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.GameTimer;
import kebriel.ctf.game.FlagManager;

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
