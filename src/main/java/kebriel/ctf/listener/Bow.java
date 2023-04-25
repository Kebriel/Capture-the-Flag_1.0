package kebriel.ctf.listener;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;

public class Bow implements Listener {
	
	public static HashMap<Arrow, Location> farshot_shots = new HashMap<Arrow, Location>();
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			PlayerProfile prof = ProfileManager.getProfile(p);
			if(e.getProjectile() instanceof Arrow) {
				Arrow a = (Arrow) e.getProjectile();
				if(prof.getIsSelected("ability_farshot")) {
					farshot_shots.put(a, p.getLocation());
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if(a.isOnGround()) {
							a.remove();
							this.cancel();
						}
					}
					
				}.runTaskTimer(CTFMain.instance, 0, 1);
			}
		}
	}

}
