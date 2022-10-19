package sylaires.ctf.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.game.KillHandler;
import sylaires.ctf.game.TeamHandler;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class PlayerDeath implements Listener {
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if(CTFMain.theState == GameState.PLAYING) {
			if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				Player attacker = (Player) e.getDamager();
				Player victim = (Player) e.getEntity();
				//Friendly fire
				if(TeamHandler.redTeam.contains(victim.getUniqueId()) && TeamHandler.redTeam.contains(attacker.getUniqueId())) {		
					e.setCancelled(true);
					return;
				}else if(TeamHandler.blueTeam.contains(victim.getUniqueId()) && TeamHandler.blueTeam.contains(attacker.getUniqueId())) {		
					e.setCancelled(true);
					return;
				}else {
					PlayerProfile vprof = ProfileManager.getProfile(victim);
					vprof.addHitter(attacker.getUniqueId());
				}
			}		
		}else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDeath(EntityDamageEvent e) {
		if(CTFMain.theState == GameState.PLAYING) {
				if(e.getEntity() instanceof Player) {
					Player p = (Player) e.getEntity();
					if((p.getHealth()-e.getDamage()) <= 0) {
						e.setCancelled(true);
						KillHandler.killPlayer(p, e.getCause());
						}
					}
			}
		}

}
