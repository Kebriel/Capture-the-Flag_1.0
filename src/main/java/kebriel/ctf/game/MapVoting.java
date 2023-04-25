package kebriel.ctf.game;

import java.util.*;

import kebriel.ctf.display.ScoreboardSidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import kebriel.ctf.Map;
import kebriel.ctf.MapManager;

public class MapVoting {

	public static boolean voting;

	public static int map_nums = 3;

	public static java.util.Map<Map, Integer> votes_for_map = new HashMap<Map, Integer>();

	public static List<UUID> voters = new ArrayList<UUID>();

	public static List<Map> selected = new ArrayList<Map>();

	public static List<Map> getRandomlySelected() {
		Random rand = new Random();
		List<Integer> toAdd = new ArrayList<Integer>();
		if(map_nums > MapManager.maps.size()) map_nums = MapManager.maps.size();
		for(int i = 0; i < map_nums; i++) {
			int num = rand.nextInt(MapManager.maps.size());
			while(toAdd.contains(num)) { //Ensures no duplicates are added
				num = rand.nextInt(MapManager.maps.size());
			}
			toAdd.add(rand.nextInt(MapManager.maps.size()));
		}

		List<Map> random_maps = new ArrayList<Map>();
		for(Integer i : toAdd) {
			random_maps.add(MapManager.maps.get(i));
			votes_for_map.put(MapManager.maps.get(i), 0);
		}
		return random_maps;
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
		for(int i = 1; i <= selected.size(); i++) {
			p.sendMessage(ChatColor.GREEN + "" + i + ". " + ChatColor.GOLD + selected.get(i-1).getName());
		}
		p.sendMessage(ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/vote <#>" + ChatColor.AQUA + " to vote for a map!");

	}

	public static void reset() {
		selected.clear();
		votes_for_map.clear();
		voters.clear();
	}

	public static Map getWinner() {
		Map winner = null;
		for(Map m : votes_for_map.keySet()) {
			if(votes_for_map.get(m) == Collections.max(votes_for_map.values())) {
				winner = m; break;
			}
		}

		voting = false;
		MapManager.setCurrent(winner);
		return winner;
	}

}
