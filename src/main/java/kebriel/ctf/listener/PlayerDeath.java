package kebriel.ctf.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.KillHandler;
import kebriel.ctf.game.TeamHandler;

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
