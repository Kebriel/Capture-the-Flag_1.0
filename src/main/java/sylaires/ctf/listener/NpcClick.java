package sylaires.ctf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import sylaires.ctf.CTFMain;
import sylaires.ctf.MapManager;
import sylaires.ctf.display.OverviewGUI;
import sylaires.ctf.game.TeamHandler;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class NpcClick implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractAtEntityEvent e) {
		Player p = e.getPlayer();
		Entity en = e.getRightClicked();
		if(en instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand) en;
			String name = ChatColor.stripColor(stand.getName().toLowerCase());
			if(name.equalsIgnoreCase("abilities") || name.contains("manage") || name.equalsIgnoreCase("clickable")) {
				if(TeamHandler.redTeam.contains(p.getUniqueId())) {
					Location loc1 = new Location(CTFMain.instance.getWorld(), stand.getLocation().getBlockX(), stand.getLocation().getBlockY(), stand.getLocation().getBlockZ());
					Location loc2 = new Location(CTFMain.instance.getWorld(), MapManager.getCurrent().getRedNpc().getBlockX(), MapManager.getCurrent().getRedNpc().getBlockY(), MapManager.getCurrent().getRedNpc().getBlockZ());
					if(loc1.clone().subtract(loc2).getX() <= 1.2 && loc1.clone().subtract(loc2).getY() <= 1.2 && loc1.clone().subtract(loc2).getZ() <= 1.2) {
						OverviewGUI.getNew(p); return;
					}
				}
				if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
					Location loc1 = new Location(CTFMain.instance.getWorld(), stand.getLocation().getBlockX(), stand.getLocation().getBlockY(), stand.getLocation().getBlockZ());
					Location loc2 = new Location(CTFMain.instance.getWorld(), MapManager.getCurrent().getBlueNpc().getBlockX(), MapManager.getCurrent().getBlueNpc().getBlockY(), MapManager.getCurrent().getBlueNpc().getBlockZ());
					if(loc1.clone().subtract(loc2).getX() <= 1.2 && loc1.clone().subtract(loc2).getY() <= 1.2 && loc1.clone().subtract(loc2).getZ() <= 1.2) {
						OverviewGUI.getNew(p); return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onLeftClick(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
			e.setCancelled(true);
			Player p = (Player) e.getDamager();
			Entity en = e.getEntity();
			ArmorStand stand = (ArmorStand) en;
			String name = ChatColor.stripColor(stand.getName().toLowerCase());
			if(name.equalsIgnoreCase("loadouts") || name.contains("manage") || name.equalsIgnoreCase("clickable")) {
				if(TeamHandler.redTeam.contains(p.getUniqueId())) {
					Location loc1 = new Location(CTFMain.instance.getWorld(), stand.getLocation().getBlockX(), stand.getLocation().getBlockY(), stand.getLocation().getBlockZ());
					Location loc2 = new Location(CTFMain.instance.getWorld(), MapManager.getCurrent().getRedNpc().getBlockX(), MapManager.getCurrent().getRedNpc().getBlockY(), MapManager.getCurrent().getRedNpc().getBlockZ());
					if(loc1.equals(loc2)) {
						OverviewGUI.getNew(p); return;
					}
				}
				if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
					Location loc1 = new Location(CTFMain.instance.getWorld(), stand.getLocation().getBlockX(), stand.getLocation().getBlockY(), stand.getLocation().getBlockZ());
					Location loc2 = new Location(CTFMain.instance.getWorld(), MapManager.getCurrent().getBlueNpc().getBlockX(), MapManager.getCurrent().getBlueNpc().getBlockY(), MapManager.getCurrent().getBlueNpc().getBlockZ());
					if(loc1.equals(loc2)) {
						OverviewGUI.getNew(p); return;
					}
				}
			}
		}
	}

}
