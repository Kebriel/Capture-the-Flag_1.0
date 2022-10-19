package sylaires.ctf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class Autosave {
	
	/*
	 * An experimental initialization method that should only ever be called once, lest it run several simultaneous threads.
	 * May be replaced with a constructor-singleton later. 
	 */
	public static void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) { //Saves the data of all online players
					ProfileManager.getProfile(p).saveToDB();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 200); //Database integration is already Async, and Bukkit methods never should be (msg sending)
	}

}
