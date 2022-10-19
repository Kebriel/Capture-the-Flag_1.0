package sylaires.ctf.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import sylaires.ctf.Map;
import sylaires.ctf.MapManager;
import sylaires.ctf.display.ScoreboardSidebar;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class MapVoting {
	
	public static boolean voting;
	
	public static HashMap<Map, Integer> votes_for_map = new HashMap<Map, Integer>();
	
	public static ArrayList<UUID> voters = new ArrayList<UUID>();
	
	public static ArrayList<Map> selected = new ArrayList<Map>();
	
	public static ArrayList<Map> getRandomlySelected() {
		//Get three maps to vote for
		Random rand = new Random();
		int int1 = rand.nextInt(MapManager.maps.size());
		int int2 = rand.nextInt(MapManager.maps.size());
		int int3 = rand.nextInt(MapManager.maps.size());
		Map map1 = MapManager.maps.get(int1);
		Map map2 = null;
		Map map3 = null;
		for(int i = 1; i < 1000; i++) {
			map2 = MapManager.maps.get(int2);
			if(map2.getId()!=map1.getId()) {
				break;
			}else {
				int2 = rand.nextInt(MapManager.maps.size()); continue; //Randomize over and over
			}
		}
		for(int i = 1; i < 1000; i++) {
			map3 = MapManager.maps.get(int3);
			if(map3.getId()!=map1.getId()&& map3.getId()!=map2.getId()) {
				break;
			}else {
				int3 = rand.nextInt(MapManager.maps.size()); continue;
			}
		}
		ArrayList<Map> threeRandom = new ArrayList<Map>();
		threeRandom.add(map1); threeRandom.add(map2); threeRandom.add(map3);
		votes_for_map.put(map1, 0); votes_for_map.put(map2, 0); votes_for_map.put(map3, 0);
		return threeRandom;
	}
	
	public static void voteForMap(Map map, Player p) {
		votes_for_map.put(map, votes_for_map.get(map)+1);
		voters.add(p.getUniqueId());
		ScoreboardSidebar.votingBoard();
	}
	
	public static void init() {
		selected = getRandomlySelected();
		for(Player p : Bukkit.getOnlinePlayers()) {
			MapVoting.displayOptions(p);
		}
	}
	
	public static void displayOptions(Player p) {
		p.sendMessage(ChatColor.AQUA + "Vote for a " + ChatColor.YELLOW + "Map:");
		p.sendMessage(ChatColor.GREEN + "1. " + ChatColor.GOLD + selected.get(0).getName());
		p.sendMessage(ChatColor.GREEN + "2. " + ChatColor.GOLD + selected.get(1).getName());
		p.sendMessage(ChatColor.GREEN + "3. " + ChatColor.GOLD + selected.get(2).getName());
		p.sendMessage(ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/vote <#>" + ChatColor.AQUA + " to vote for a map!");

	}
	
	public static void reset() {
		selected.clear();
		votes_for_map.clear();
		voters.clear();
	}
	
	public static Map getWinner() {
		Map winner = null;
		int highest = 0;
		for(int i = 0; i < votes_for_map.size(); i++) {
			if(votes_for_map.get(new ArrayList<Map>(votes_for_map.keySet()).get(i)) > highest) {
				highest = votes_for_map.get(new ArrayList<Map>(votes_for_map.keySet()).get(i));
				winner = new ArrayList<Map>(votes_for_map.keySet()).get(i);
			}
		}
		if(winner == null) {
			Random rand = new Random();
			int r = rand.nextInt(selected.size());
			winner = selected.get(r);
		}
		voting = false;
		MapManager.setCurrent(winner);
		return winner;
	}

}
