package sylaires.ctf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.game.FlagManager;
import sylaires.ctf.game.FlagState;
import sylaires.ctf.game.TeamHandler;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

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
