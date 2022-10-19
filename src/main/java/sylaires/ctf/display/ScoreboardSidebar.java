package sylaires.ctf.display;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.GameTimer;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.ability.AbilityRegistry;
import sylaires.ctf.ability.IAbility;
import sylaires.ctf.game.FlagManager;
import sylaires.ctf.game.MapVoting;
import sylaires.ctf.game.TeamHandler;
import sylaires.ctf.util.ScoreboardBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class ScoreboardSidebar {
	
	public static void votingBoard() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			votingBoard(p);
		}
	}
	
	public static void votingBoard(Player p) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective title = board.registerNewObjective("vote", "dummy");
		title.setDisplayName("" + ChatColor.GOLD + ChatColor.BOLD + "Maps");
		title.setDisplaySlot(DisplaySlot.SIDEBAR);
			
		title.getScore(MapVoting.selected.get(0).getName() + "").setScore(MapVoting.votes_for_map.get(MapVoting.selected.get(0)));
		title.getScore(MapVoting.selected.get(1).getName() + "").setScore(MapVoting.votes_for_map.get(MapVoting.selected.get(1)));
		title.getScore(MapVoting.selected.get(2).getName() + "").setScore(MapVoting.votes_for_map.get(MapVoting.selected.get(2)));
			
		p.setScoreboard(board);
	}
	
	public static void gameBoard() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			gameBoard(p);
		}
	}
	
	public static void gameBoard(Player p) {
		ScoreboardBuilder builder = new ScoreboardBuilder("" + ChatColor.BLUE + ChatColor.MAGIC + "===" + ChatColor.GOLD + ChatColor.BOLD + " CTF " + ChatColor.RED + ChatColor.MAGIC + "===", p);
		PlayerProfile profile = ProfileManager.getProfile(p);
		if(CTFMain.theState == GameState.PLAYING || CTFMain.theState == GameState.ENDING) {
			if(CTFMain.theState == GameState.PLAYING) {
				builder.addLine(ChatColor.GREEN + "Game: " + ChatColor.YELLOW + ChatColor.BOLD + "PLAYING");
			}else {
				builder.addLine(ChatColor.GREEN + "Game: " + ChatColor.YELLOW + ChatColor.BOLD + "ENDING");
			}
			int minutes = GameTimer.timer/60;
			int seconds = GameTimer.timer-(minutes*60);
			String strSeconds = String.valueOf(seconds);
			if(seconds < 10) {
				strSeconds = "0" + strSeconds;
			}
			if(GameTimer.lightning_round) {
				builder.addLine(ChatColor.RED + "Game Ends: " + ChatColor.WHITE + minutes + ":" + strSeconds);
			}else {
				builder.addLine(ChatColor.YELLOW + "Deathmatch In: " + ChatColor.WHITE + minutes + ":" + strSeconds);
			}
			if(TeamHandler.redTeam.contains(p.getUniqueId())) {
				builder.addLine(ChatColor.GOLD + "Team: " + ChatColor.RED + "RED");
			}else {
				builder.addLine(ChatColor.GOLD + "Team: " + ChatColor.BLUE + "BLUE");
			}
			builder.addLine(" ");
			builder.addLine("" + ChatColor.BLUE + ChatColor.BOLD + "BLUE");
			builder.addLine(ChatColor.GOLD + "Flag: " + ChatColor.GREEN + FlagManager.blueFlag.getState().toString());
			builder.addLine(ChatColor.AQUA + "Flags Captured: " + ChatColor.WHITE + FlagManager.blueFlags);
			builder.addLine("  ");
			builder.addLine("" + ChatColor.RED + ChatColor.BOLD + "RED");
			builder.addLine(ChatColor.GOLD + "Flag: " + ChatColor.GREEN + FlagManager.redFlag.getState().toString() + " ");
			builder.addLine(ChatColor.AQUA + "Flags Captured: " + ChatColor.WHITE + FlagManager.redFlags + " ");
			builder.addLine("   ");
			builder.addLine(ChatColor.RED + "Kills: " + ChatColor.WHITE + profile.getKillsThisGame());
			builder.addLine(ChatColor.GOLD + "Gold Earned: " + ChatColor.WHITE + profile.getGoldThisGame());
			
		}
		p.setScoreboard(builder.build());
	}

	public static void idleBoard() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			idleBoard(p);
		}
	}
	
	public static void idleBoard(Player p) {
		PlayerProfile profile = ProfileManager.getProfile(p);
		
		IAbility ab1 = AbilityRegistry.getInstanceByID(profile.getSelected(1, SlotType.ABILITY));
		String s_ab1 = "";
		if(ab1 == null) {  s_ab1 = "None"; }else { s_ab1 = ab1.getName(); }
		IAbility ab2 = AbilityRegistry.getInstanceByID(profile.getSelected(2, SlotType.ABILITY));
		String s_ab2 = "";
		if(ab2 == null) {  s_ab2 = "None"; }else { s_ab2 = ab2.getName(); }
		IAbility ab3 = AbilityRegistry.getInstanceByID(profile.getSelected(3, SlotType.ABILITY));
		String s_ab3 = "";
		if(ab3 == null) {  s_ab3 = "None"; }else { s_ab3 = ab3.getName(); }
		IAbility ab4 = AbilityRegistry.getInstanceByID(profile.getSelected(4, SlotType.ABILITY));
		String s_ab4 = "";
		if(ab4 == null) {  s_ab4 = "None"; }else { s_ab4 = ab4.getName(); }
		
		IAbility inv1 = AbilityRegistry.getInstanceByID(profile.getSelected(1, SlotType.INVENTORY));
		String s_inv1 = "";
		if(inv1 == null) {  s_inv1 = "None"; }else { s_inv1 = inv1.getName(); }
		
		IAbility perk1 = AbilityRegistry.getInstanceByID(profile.getSelected(1, SlotType.PERK));
		String s_perk1 = "";
		if(perk1 == null) {  s_perk1 = "None"; }else { s_perk1 = perk1.getName(); }
		IAbility perk2 = AbilityRegistry.getInstanceByID(profile.getSelected(2, SlotType.PERK));
		String s_perk2 = "";
		if(perk2 == null) {  s_perk2 = "None"; }else { s_perk2 = perk2.getName(); }
		ScoreboardBuilder builder = new ScoreboardBuilder("" + ChatColor.BLUE + ChatColor.MAGIC + "===" + ChatColor.GOLD + ChatColor.BOLD + " CTF " + ChatColor.RED + ChatColor.MAGIC + "===", p);
		
		builder.addLine(ChatColor.GREEN + "Game: " + ChatColor.YELLOW + ChatColor.BOLD + "WAITING");
		builder.addLine("Players: " + ChatColor.AQUA + Bukkit.getOnlinePlayers().size() + "/" + CTFMain.instance.getReqPlayers());
		builder.addLine(" ");
		builder.addLine(ChatColor.AQUA + "Level: " + ChatColor.WHITE + profile.getLevel());
		builder.addLine(ChatColor.GOLD + "Gold: " + ChatColor.WHITE + profile.getGold());
		builder.addLine("  ");
		builder.addLine(ChatColor.YELLOW + "Abilities:");
		builder.addLine(ChatColor.GREEN + "-" + s_ab1);
		builder.addLine(ChatColor.GREEN + "-" + s_ab2 + " ");
		
		if(profile.getIsSlotUnlocked(3, SlotType.ABILITY)) {
			builder.addLine(ChatColor.GREEN + "-" + s_ab3 + "  ");
		}
		if(profile.getIsSlotUnlocked(4, SlotType.ABILITY)) {
			builder.addLine(ChatColor.GREEN + "-" + s_ab4 + "   ");
		}
		if(profile.getIsSlotUnlocked(1, SlotType.INVENTORY)) {
			builder.addLine("    ");
			builder.addLine(ChatColor.YELLOW + "Inv Slot: " + ChatColor.WHITE + s_inv1);
		}
		if(profile.getIsSlotUnlocked(1, SlotType.PERK)) {
			if(!builder.hasLine("    ")) {
				builder.addLine("    ");
			}
			builder.addLine(ChatColor.GOLD + "Perk 1: " + ChatColor.WHITE + s_perk1);
		}
		if(profile.getIsSlotUnlocked(2, SlotType.PERK)) {
			if(!builder.hasLine("    ")) {
				builder.addLine("    ");
			}
			builder.addLine(ChatColor.GOLD + "Perk 2: " + ChatColor.WHITE + s_perk2);
		}
		
		p.setScoreboard(builder.build());
		
	}
	
	public static void update(Player p) {
		if(CTFMain.theState == GameState.WAITING) {
			idleBoard(p);
			return;
		}
		if(CTFMain.theState == GameState.STARTING) {
			votingBoard(p);
			return;
		}
		if(CTFMain.theState == GameState.PLAYING || CTFMain.theState == GameState.ENDING) {
			gameBoard(p);
			return;
		}
	}
	
	public static void update() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			update(p);
		}
	}

}
