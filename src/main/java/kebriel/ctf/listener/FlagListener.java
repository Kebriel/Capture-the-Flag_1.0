package kebriel.ctf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.game.FlagState;
import kebriel.ctf.game.TeamHandler;
import kebriel.ctf.util.MessageUtil;

public class FlagListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(CTFMain.theState == GameState.PLAYING) {
			if(e.getRightClicked() instanceof ArmorStand) {
				ArmorStand a = (ArmorStand) e.getRightClicked();
				Player p = e.getPlayer();
				PlayerProfile prof = ProfileManager.getProfile(p);
				if(!prof.isDead()) {
					if(a.getName().contains("RED") || a.getName().contains("redflag") || a.getName().equalsIgnoreCase("redclickable")) {
						if(FlagManager.blueFlag.getHolder() != null) {
							if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) {
								MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								return;
							}
						}
						if(FlagManager.redFlag.getHolder() != null) {
							if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) {
								MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								return;
							}
						}
						if(TeamHandler.redTeam.contains(p.getUniqueId())) {
							if(FlagManager.redFlag.getState() == FlagState.MISSING) {
								FlagManager.redFlag.pickup(p);
							}
						}else {
							FlagManager.redFlag.pickup(p);
						}
					}else if(a.getName().contains("BLUE") || a.getName().contains("blueflag") || a.getName().equalsIgnoreCase("blueclickable")) {
						if(FlagManager.blueFlag.getHolder() != null) {
							if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) {
								MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								return;
							}
						}
						if(FlagManager.redFlag.getHolder() != null) {
							if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) {
								MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								return;
							}
						}
						if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
							if(FlagManager.blueFlag.getState() == FlagState.MISSING) {
								FlagManager.blueFlag.pickup(p);
							}
						}else {
							FlagManager.blueFlag.pickup(p);
						}
					}
				}
			}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onLeftClick(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
			ArmorStand a = (ArmorStand) e.getEntity();
			Player p = (Player) e.getDamager();
			if(a.getName().contains("RED") || a.getName().contains("redflag") || a.getName().equalsIgnoreCase("redclickable")) {
				if(FlagManager.blueFlag.getHolder() != null) {
					if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) {
						MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return;
					}
				}
				if(FlagManager.redFlag.getHolder() != null) {
					if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) {
						MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return;
					}
				}
				
				if(TeamHandler.redTeam.contains(p.getUniqueId())) {
					if(FlagManager.redFlag.getState() == FlagState.MISSING) {
						FlagManager.redFlag.pickup(p);
					}
				}else {
					FlagManager.redFlag.pickup(p);
				}
			}else if(a.getName().contains("BLUE") || a.getName().contains("blueflag") || a.getName().equalsIgnoreCase("blueclickable")) {
				if(FlagManager.blueFlag.getHolder() != null) {
					if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) {
						MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return;
					}
				}
				if(FlagManager.redFlag.getHolder() != null) {
					if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) {
						MessageUtil.sendToPlayer(p, ChatColor.RED + "You can't hold two flags at once!");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return;
					}
				}
				if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
					if(FlagManager.blueFlag.getState() == FlagState.MISSING) {
						FlagManager.blueFlag.pickup(p);
					}
				}else {
					FlagManager.blueFlag.pickup(p);
				}
			}
		}
	}

}
