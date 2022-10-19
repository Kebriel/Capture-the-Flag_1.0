package sylaires.ctf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.display.OverviewGUI;
import sylaires.ctf.display.TrackerGUI;
import sylaires.ctf.game.FlagManager;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GenericInteract implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getItem() != null) {
			ItemStack clicked = e.getItem();
			if(clicked.getType() == Material.BEACON) {
				OverviewGUI.getNew(p);
			}
			if(clicked.getType() == Material.SKULL_ITEM) {
				p.performCommand("stats");
			}
			if(clicked.getType() == Material.EMERALD) {
				p.performCommand("cosmetic");
			}
			
			//Ingame
			if(CTFMain.theState == GameState.PLAYING) {
				if(clicked.getType() == Material.PAPER) {
					PlayerProfile prof = ProfileManager.getProfile(p);
					if(prof.getIsSelected("item_bandage")) {
						p.getInventory().remove(Material.PAPER);
						MessageUtil.sendToPlayer(p, ChatColor.GREEN + "You used your " + ChatColor.GOLD + "Bandage" + ChatColor.GREEN + " ability!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, true, false), true);
						p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0, true, false), true);
						if(p.getHealth()+2 > p.getMaxHealth()) {
							p.setHealth(p.getMaxHealth());
						}else {
							p.setHealth(p.getHealth()+2);
						}
					}
				}
				if(clicked.getType() == Material.ENDER_PEARL) {
					if(FlagManager.blueFlag.getHolder() != null) {
						if(FlagManager.blueFlag.getHolder().getUniqueId().equals(p.getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
					if(FlagManager.redFlag.getHolder() != null) {
						if(FlagManager.redFlag.getHolder().getUniqueId().equals(p.getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
				}
				if(clicked.getType() == Material.COMPASS) {
					TrackerGUI.getNew(p);
				}
			}
		}
	}

}
