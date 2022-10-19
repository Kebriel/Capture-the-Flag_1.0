package sylaires.ctf.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class TeamHandler {
	
	public static HashMap<Player, PacketPlayOutScoreboardTeam> packet_cache = new HashMap<Player, PacketPlayOutScoreboardTeam>();
	
	public static ArrayList<UUID> blueQueue = new ArrayList<UUID>();
	public static ArrayList<UUID> redQueue = new ArrayList<UUID>();
	
	public static ArrayList<UUID> blueTeam = new ArrayList<UUID>();
	public static ArrayList<UUID> redTeam = new ArrayList<UUID>();
	
	public static boolean joinQueue(Player p, String color) {
		int queueLimit = (Bukkit.getOnlinePlayers().size()/2)-1;
		if(queueLimit <= 0) {
			queueLimit=1;
		}
		if(color.equalsIgnoreCase("red")) {
			if(redQueue.size() >= queueLimit) {
				return false;
			}else {
				redQueue.add(p.getUniqueId());
				return true;
			}
			
		}else if(color.equalsIgnoreCase("blue")) {
			if(blueQueue.size() >= queueLimit) {
				return false;
			}else {
				blueQueue.add(p.getUniqueId());
				return true;
			}
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	public static void createTeams() {
		Random rand = new Random();
		ArrayList<UUID> all_players = new ArrayList<UUID>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			all_players.add(p.getUniqueId());
		}
		Collections.shuffle(all_players);
		//Dump Queues into teams first
		for(int i = 0; i < redQueue.size(); i++) {
			redTeam.add(redQueue.get(i));
			all_players.remove(redQueue.get(i));
		}
		for(int i = 0; i < blueQueue.size(); i++) {
			blueTeam.add(blueQueue.get(i));
			all_players.remove(blueQueue.get(i));
		}
		for(int i = 0; i < all_players.size(); i++) {
			if(redTeam.size() > blueTeam.size()) {
				blueTeam.add(all_players.get(i));
			//	all_players.remove(i);
			}else if(redTeam.size() < blueTeam.size()) {
				redTeam.add(all_players.get(i));
			//	all_players.remove(i);
			}else {
				if(rand.nextBoolean()) {
					redTeam.add(all_players.get(i));
				//	all_players.remove(i);
				}else {
					blueTeam.add(all_players.get(i));
				//	all_players.remove(i);
				}
			}
		}
		if(blueTeam.size() + redTeam.size() != Bukkit.getOnlinePlayers().size()) {
			Bukkit.broadcastMessage("Something went wrong, players weren't properly divided!");
		}
		redQueue.clear();
		blueQueue.clear();
	}
	
	public static void reset() {
		redQueue.clear();
		blueQueue.clear();
		redTeam.clear();
		blueTeam.clear();
		for(Player p : Bukkit.getOnlinePlayers()) {
			applyPrefixes(p);
			TeamHandler.packet_cache.remove(p);
		}
	}
	
	public static void addTardyPlayer(Player p) {
		Random rand = new Random();
		if(!blueTeam.contains(p.getUniqueId()) && !redTeam.contains(p.getUniqueId())) { //Make sure they're not already on a team!
			if(redTeam.size() > blueTeam.size()) {
				blueTeam.add(p.getUniqueId());
			}else if(redTeam.size() < blueTeam.size()) {
				redTeam.add(p.getUniqueId());
			}else {
				if(rand.nextBoolean()) {
					redTeam.add(p.getUniqueId());
				}else {
					blueTeam.add(p.getUniqueId());
				}
			}
		}
	}
	
	public static void applyPrefixes(Player p) {
			Scoreboard board = new Scoreboard();
			ScoreboardTeam team;
			String tname = UUID.randomUUID().toString().replace("-", "").substring(0, 7);
			team = board.createTeam(tname);
			String prefix = "";
			if(blueTeam.contains(p.getUniqueId())) {
				prefix = ChatColor.BLUE + "";
			}else if(redTeam.contains(p.getUniqueId())) {
				prefix = ChatColor.RED + "";
			}else {
				prefix = "";
			}
			
			team.setPrefix(prefix);
			board.addPlayerToTeam(p.getName(), tname);
			
			if(CTFMain.theState == GameState.PLAYING) {
				PlayerProfile prof = ProfileManager.getProfile(p);
				if(prof.getIsSelected("perk_untrackable")) {
					team.setNameTagVisibility(EnumNameTagVisibility.NEVER);
				}
			}
			
			PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(team, 0);
			new BukkitRunnable() {
				public void run() {
					for(Player players : Bukkit.getOnlinePlayers()) {
						((CraftPlayer)players).getHandle().playerConnection.sendPacket(packet);
					}
				}
			}.runTaskLaterAsynchronously(CTFMain.instance, 1);
			
			if(packet_cache.containsKey(p)) {
				packet_cache.remove(p);
				packet_cache.put(p, packet);
			}else {
				packet_cache.put(p, packet);
			}
		}
	
	public static void applyPacketToPlayer(Player p, PacketPlayOutScoreboardTeam packet) {
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}
}
