package sylaires.ctf.game;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import sylaires.ctf.CTFMain;
import sylaires.ctf.Items;
import sylaires.ctf.MapManager;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.display.ScoreboardSidebar;
import sylaires.ctf.listener.AbilityProcDamage;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;
import sylaires.ctf.util.TitleUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class KillHandler {
	
	public static void playerKillPlayer(Player killer, Player killed, boolean flag) {
		PlayerProfile aprof = ProfileManager.getProfile(killer);
		aprof.addKill();
		
		if(aprof.getIsSelected("ability_berserk")) {
			aprof.setBerserked(true);
		}
		
		int kill_xp = 5;
		int kill_gold = 5;
		if(aprof.getIsSelected("ability_xp")) kill_xp *= 2;
		if(flag) {
			aprof.addCarrierKill();
			kill_xp *= 2;
			kill_gold *= 2;
		}
		
		int royal_num = 0;
		if(TeamHandler.redTeam.contains(killer.getUniqueId())) {
			for(UUID id : TeamHandler.redTeam) {
				Player player = Bukkit.getPlayer(id);
				PlayerProfile player_prof = ProfileManager.getProfile(player);
				if(player_prof.getIsSelected("perk_royalty")) {
					royal_num++;
				}
			}
		}else if(TeamHandler.blueTeam.contains(killer.getUniqueId())) {
			for(UUID id : TeamHandler.blueTeam) {
				Player player = Bukkit.getPlayer(id);
				PlayerProfile player_prof = ProfileManager.getProfile(player);
				if(player_prof.getIsSelected("perk_royalty")) {
					royal_num++;
				}
			}
		}
		kill_xp += royal_num;
		kill_gold += royal_num;
		
		if(TeamHandler.redTeam.contains(killed.getUniqueId())) {
			if(!flag) {
				MessageUtil.sendToPlayer(killer, ChatColor.AQUA + "You killed " + ChatColor.RED + killed.getName() + ChatColor.AQUA + "! " + ChatColor.GOLD + "+" + kill_gold + "g " + ChatColor.AQUA + "+" + kill_xp + "XP");
				killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
				PlayerProfile prof = ProfileManager.getProfile(killer);
				prof.addGold(kill_gold, true);
				prof.addXp(kill_xp);
			}else {
				MessageUtil.sendToPlayer(killer, ChatColor.AQUA + "You killed " + ChatColor.RED + killed.getName() + ChatColor.AQUA + ", and they were holding a flag! " + ChatColor.GOLD + "+" + kill_gold + "g " + ChatColor.AQUA + "+" + kill_xp +  "XP");
				killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
				PlayerProfile prof = ProfileManager.getProfile(killer);
				prof.addGold(kill_gold, true);
				prof.addXp(kill_xp);
			}
		}else if(TeamHandler.blueTeam.contains(killed.getUniqueId())){
			if(!flag) {
				MessageUtil.sendToPlayer(killer, ChatColor.AQUA + "You killed " + ChatColor.BLUE + killed.getName() + ChatColor.AQUA + "! " + ChatColor.GOLD + "+" + kill_gold + "g " + ChatColor.AQUA + + kill_xp +  "XP");
				killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
				PlayerProfile prof = ProfileManager.getProfile(killer);
				prof.addGold(kill_gold, true);
				prof.addXp(kill_xp);
			}else {
				MessageUtil.sendToPlayer(killer, ChatColor.AQUA + "You killed " + ChatColor.BLUE + killed.getName() + ChatColor.AQUA + ", and they were holding a flag! " + ChatColor.GOLD + "+" + kill_gold + "g " + ChatColor.AQUA + "+" + kill_xp +  "XP");
				killer.playSound(killer.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
				PlayerProfile prof = ProfileManager.getProfile(killer);
				prof.addGold(kill_gold, true);
				prof.addXp(kill_xp);
			}
		}
		
		//Abilities that affect kills
		PlayerProfile killer_prof = ProfileManager.getProfile(killer);
		if(killer_prof.getIsSelected("item_blocks")) { 
			ItemStack block = new ItemBuilder(Material.GLASS, ChatColor.GOLD + "Ghost Block").setAmount(5).toItem();
			killer.getInventory().addItem(block);
		}
		
		if(TeamHandler.redTeam.contains(killer.getUniqueId())) {
			MessageUtil.sendToLobby(ChatColor.RED + killer.getName() + ChatColor.GRAY + " killed " + ChatColor.BLUE + killed.getName());
		}else {
			MessageUtil.sendToLobby(ChatColor.BLUE + killer.getName() + ChatColor.GRAY + " killed " + ChatColor.RED + killed.getName());
		}
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(aprof.isBerserked()) {
					aprof.setBerserked(false);
				}
			}
			
		}.runTaskLater(CTFMain.instance, 60);
		ScoreboardSidebar.gameBoard(killer);
	}
	
	/*
	 * Supermethod to handle the killing of a player, called in Listener
	 */
	public static void killPlayer(Player p, DamageCause cause) {
		//Restore to max health
		p.setHealth(p.getMaxHealth());
		//Deal with any fall damage minecraft bugs
		p.setFallDistance(0.0f); 
		//Clear their inventory
		p.getInventory().clear();
		//Remove fire
		p.setFireTicks(0);
		//Clear potion effects
		for(PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		//Instantiate profile of player who died
		PlayerProfile vprof = ProfileManager.getProfile(p);
		//Give them a death in stats
		vprof.addDeath();
		//Set them to be considered dead
		vprof.setDead(true);
		//Put them in spectator mode
		p.setGameMode(GameMode.SPECTATOR);
		//Placeholder extra hit effect
		p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
		//By default, a player isn't a flagholder
		boolean flagHolder = false;
		//Load last hitters
		ArrayList<UUID> last_hitters = vprof.getLastHitters();
		//Drop flag
		if(FlagManager.blueFlag.getHolder() != null) { //If blue flag has a holder
				if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) { //If blue flag's holder's UUID equals ours
					if(cause == DamageCause.VOID) { //If we died in the void, instantly return the flag
						FlagManager.blueFlag.returnFlag();
					}else { //Elsewise, drop it on the ground
						FlagManager.blueFlag.drop();
					}
						flagHolder = true; //We've learned that we're a flagholder
				}
			}
		if(FlagManager.redFlag.getHolder() != null) { //If red flag has a holder
				if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) { //If red flag's holder's UUID equals ours
					if(cause == DamageCause.VOID) { //if we died in the void, instantly return the flag
						FlagManager.redFlag.returnFlag();
					}else { //Elsewise, drop it on the ground
						FlagManager.redFlag.drop();
					}
					flagHolder = true; //We've learned that we're a flagholder
				}
			}
		
		if(last_hitters.isEmpty()) { //If we're not contained, we haven't been hit this life at all
			//Play team-appropriate death message
			if(TeamHandler.redTeam.contains(p.getUniqueId())) {
				MessageUtil.sendToLobby(ChatColor.RED + p.getName() + ChatColor.GRAY + " has died!");
			}else if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
				MessageUtil.sendToLobby(ChatColor.BLUE + p.getName() + ChatColor.GRAY + " has died!");
			}
		}else{
			//Get player object of the most recent hitter, which is who is going to get the kill
			Player killer = Bukkit.getPlayer(last_hitters.get(last_hitters.size()-1));
			//Sort through last damagers until finding an online player (you won't get kills or assists if you logged off) 
			int index = 1;
			while(killer == null) {
				try {
					//Search each hitter
					killer = Bukkit.getPlayer(last_hitters.get(last_hitters.size()-index));
					last_hitters.remove(last_hitters.size()-index);
				} catch(IndexOutOfBoundsException e) { break; } //Thrown if literally every player who hit you has since logged off
				index++;
			}
			if(killer != null) playerKillPlayer(killer, p, flagHolder);
			for(UUID ps : last_hitters) {
				if(ps != killer.getUniqueId()) { //Don't give the killer an assist
					Player player = Bukkit.getPlayer(ps);
					if(TeamHandler.redTeam.contains(p.getUniqueId())) {
						int assist_xp = 1;
						int assist_gold = 1;
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
						PlayerProfile prof = ProfileManager.getProfile(ps);
						if(prof.getIsSelected("ability_xp")) assist_xp *= 2;
						prof.addAssist();
						
						int royal_num = 0;
						if(TeamHandler.redTeam.contains(killer.getUniqueId())) {
							for(UUID id : TeamHandler.redTeam) {
								PlayerProfile player_prof = ProfileManager.getProfile(id);
								if(player_prof.getIsSelected("perk_royalty")) {
									royal_num++;
								}
							}
						}else if(TeamHandler.blueTeam.contains(killer.getUniqueId())) {
							for(UUID id : TeamHandler.blueTeam) {
								PlayerProfile player_prof = ProfileManager.getProfile(id);
								if(player_prof.getIsSelected("perk_royalty")) {
									royal_num++;
								}
							}
						}
						assist_xp += royal_num;
						assist_gold += royal_num;
						
						prof.addGold(assist_gold, true);
						prof.addXp(assist_xp);
						MessageUtil.sendToPlayer(player, ChatColor.AQUA + "You got an assist on " + ChatColor.BLUE + p.getName() + ChatColor.AQUA + "! " + ChatColor.GOLD + "+" + assist_gold + "g " + ChatColor.AQUA + "+" + assist_xp + "XP");
						
					}else if(TeamHandler.blueTeam.contains(p.getUniqueId()))	{
						int assist_xp = 1;
						int assist_gold = 1;
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
						PlayerProfile prof = ProfileManager.getProfile(ps);
						if(prof.getIsSelected("ability_xp")) assist_xp *= 2;
						prof.addAssist();
						
						int royal_num = 0;
						if(TeamHandler.redTeam.contains(killer.getUniqueId())) {
							for(UUID id : TeamHandler.redTeam) {
								PlayerProfile player_prof = ProfileManager.getProfile(id);
								if(player_prof.getIsSelected("perk_royalty")) {
									royal_num++;
								}
							}
						}else if(TeamHandler.blueTeam.contains(killer.getUniqueId())) {
							for(UUID id : TeamHandler.blueTeam) {
								PlayerProfile player_prof = ProfileManager.getProfile(id);
								if(player_prof.getIsSelected("perk_royalty")) {
									royal_num++;
								}
							}
						}
						assist_xp += royal_num;
						assist_gold += royal_num;
						
						prof.addGold(assist_gold, true);
						prof.addXp(assist_xp);
						MessageUtil.sendToPlayer(player, ChatColor.AQUA + "You got an assist on " + ChatColor.BLUE + p.getName() + ChatColor.AQUA + "! " + ChatColor.GOLD + "+" + assist_gold + "g " + ChatColor.AQUA + "+" + assist_xp + "XP");
						
					}
				}
			}
			vprof.clearLastHitters();
			if(AbilityProcDamage.reinforce_cooldown.contains(p.getUniqueId())) {
				AbilityProcDamage.reinforce_cooldown.remove(p.getUniqueId());
			}
			
		}
		new BukkitRunnable() {
			int t = 5;
			@Override
			public void run() {
				if(t == 0) {
					p.setGameMode(GameMode.SURVIVAL);
					Items.gameItems(p);
					ProfileManager.getProfile(p).setDead(false);;
					
					p.setFallDistance(0.0f);
					if(TeamHandler.redTeam.contains(p.getUniqueId())) {
						p.teleport(MapManager.getCurrent().getRedSpawn());
					}else if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
						p.teleport(MapManager.getCurrent().getBlueSpawn());
					}
					
					this.cancel();
				}
				if(t != 0) {
					TitleUtil.sendTitleToPlayer(p, 0, 22, 0, "" + ChatColor.RED + ChatColor.BOLD + "YOU DIED", ChatColor.YELLOW + "Respawning in... " + ChatColor.AQUA + ChatColor.BOLD + t);
				}
				t--;
			}
			
		}.runTaskTimer(CTFMain.instance, 20, 20);
		ScoreboardSidebar.gameBoard(p);
	}

}
