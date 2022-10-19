package sylaires.ctf.game;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AlchemistTimer {
	
	public static void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(CTFMain.theState == GameState.PLAYING) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						PlayerProfile prof = ProfileManager.getProfile(p);
						if(!prof.isDead()) {
							if(prof.getIsSelected("ability_alchemist")) {
								Random rand = new Random();
								int randpot = rand.nextInt(6);
								ItemStack pot = new ItemBuilder(Material.POTION).toItem();
								switch(randpot) {
								case 0: pot = new ItemBuilder(pot, ChatColor.AQUA + "Potion of Healing").addAffectToPotion(PotionEffectType.HEAL, 1, 1, false).toItem(); 
								pot = new ItemBuilder(pot).addLore(ChatColor.RED + "Instant Healing II").toItem();
								break;
								case 1: pot = new ItemBuilder(pot, ChatColor.AQUA + "Potion of Regen").addAffectToPotion(PotionEffectType.REGENERATION, 1, 200, false).toItem(); 
								pot = new ItemBuilder(pot).addLore(ChatColor.RED + "Regeneration II (10s)").toItem();
								break;
								case 2: pot = new ItemBuilder(pot, ChatColor.AQUA + "Potion of Absorption").addAffectToPotion(PotionEffectType.ABSORPTION, 1, 1000, false).toItem(); 
								pot = new ItemBuilder(pot).addLore(ChatColor.YELLOW + "Absorbtion II (50s)").toItem();
								break;
								case 3: pot = new ItemBuilder(pot, ChatColor.AQUA + "Potion of Speed").addAffectToPotion(PotionEffectType.SPEED, 1, 200, false).toItem(); 
								pot = new ItemBuilder(pot).addLore(ChatColor.AQUA + "Speed II (10s)").toItem();
								break;
								case 4: pot = new ItemBuilder(pot, ChatColor.AQUA + "Potion of Jump Boost").addAffectToPotion(PotionEffectType.JUMP, 1, 200, false).toItem(); 
								pot = new ItemBuilder(pot).addLore(ChatColor.GREEN + "Jump Boost II (10s)").toItem();
								break;
								case 5: pot = new ItemBuilder(pot, ChatColor.AQUA + "Potion of Strength").addAffectToPotion(PotionEffectType.INCREASE_DAMAGE, 0, 200, false).toItem(); 
								pot = new ItemBuilder(pot).addLore(ChatColor.RED + "Strength I (10s)").toItem();
								break;
								}
								MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your " + ChatColor.GOLD + "Alchemist" + ChatColor.GREEN + " ability has given you a potion!");
								p.playSound(p.getLocation(), Sound.LAVA, 1.4f, 1);
								p.getInventory().addItem(pot);
							}
						}
					}
				}else {
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 600);
	}

}
