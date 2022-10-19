package sylaires.ctf.game;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.TitleUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GlobalLevel {
	
	public static void checkXP(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		int level = prof.getLevel();
		
		int xp = prof.getXp();
		int current_cost = (int) (50*(0.7*level));
		
		while(xp >= current_cost) {
			level++;
			levelup(p, level);
			p.setLevel(level);
			prof.setXP(xp-current_cost);
			xp = prof.getXp();
			prof.setLevel(level);
			current_cost = (int) (50*(0.7*level));
		}
		int required = current_cost-xp;
		
		double divide = (double) xp/current_cost;
		float percent = (float) divide;
		DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(1);
		percent = Float.valueOf(format.format(percent));
		prof.getPlayer().setLevel(level);
		if(percent == 1.0) {
		p.setExp(0.0f);
		}else { 
		p.setExp(percent);
		}
		prof.setXpToNext(required);
	}
	
	public static void checkOfflineXP(PlayerProfile prof) {
		int level = prof.getLevel();
		int xp = prof.getXp();
		int current_cost = (int) (50*(0.7*level));
		int required = current_cost-xp;
		prof.setXpToNext(required);
	}
	
	public static void levelup(Player p, int level) {
		p.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "============================================");
		p.sendMessage("                " + ChatColor.GOLD + ChatColor.MAGIC + "-=" + ChatColor.GREEN + ChatColor.BOLD + "Level Up" + ChatColor.GOLD + ChatColor.MAGIC + "=-" + ChatColor.WHITE + "                ");
		p.sendMessage(ChatColor.YELLOW + "You are now " + ChatColor.GOLD + "Level: " + ChatColor.AQUA + ChatColor.BOLD + level);
		//TODO Rewards for different levels listed here...
		p.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "============================================");
		TitleUtil.sendTitleToPlayer(p, 0, 35, 5, "" + ChatColor.GREEN + ChatColor.GOLD + "LEVEL UP", ChatColor.YELLOW + "You are now " + ChatColor.GOLD + "Level: " + ChatColor.AQUA + ChatColor.BOLD + level);
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 2);
	}

}
