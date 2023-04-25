package kebriel.ctf.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.GlobalLevel;

public class StatsCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("stats")) {
			if(args.length == 0) { //Show stats for sender
				if(sender instanceof Player) {
					Player p = (Player) sender;
					PlayerProfile prof = ProfileManager.getProfile(p);
					int deaths = prof.getDeaths();
					int kills = prof.getKills();
					int wins = prof.getWins();
					int losses = prof.getLosses();
					double kdr = prof.getKdr();
					double wlr = prof.getWlr();
					int games = prof.getGames();
					int gold = prof.getGold();
					int captures = prof.getCaptures();
					int level = prof.getLevel();
					String time_played = prof.getTimePlayed();
					int xp = prof.getXp();
					int carrier_kills = prof.getCarrierKills();
					int challenges = prof.getChallengesCompleted();
					int assists = prof.getAssists();
					
					p.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "=============================================");
					p.sendMessage(ChatColor.GREEN + p.getName() + "'s Stats:");
					p.sendMessage(ChatColor.AQUA + "Games Played: " + ChatColor.GOLD + games + ChatColor.AQUA + " Wins: " + ChatColor.GOLD + wins + ChatColor.AQUA + " Losses: " + ChatColor.GOLD + losses + ChatColor.AQUA + " WLR: " + ChatColor.GOLD + wlr);
					p.sendMessage(ChatColor.AQUA + "Flags Captured: " + ChatColor.GOLD + captures + ChatColor.AQUA + " Flag Carriers Killed: " + ChatColor.GOLD + carrier_kills);
					p.sendMessage(ChatColor.AQUA + "Assists: " + ChatColor.GOLD + assists +  ChatColor.AQUA + " Kills: " + ChatColor.GOLD + kills + ChatColor.AQUA + " Deaths: " + ChatColor.GOLD + deaths + ChatColor.AQUA + " KDR: " + ChatColor.GOLD + kdr);
					p.sendMessage(ChatColor.AQUA + "Level: " + ChatColor.GOLD + level + ChatColor.AQUA + " XP: " + ChatColor.GOLD + xp + ChatColor.AQUA + " XP Needed: " + ChatColor.GOLD + prof.getXpToNext() + ChatColor.AQUA + " Gold: " + ChatColor.GOLD + gold);
					p.sendMessage(ChatColor.AQUA + "Challenges Completed: " + ChatColor.GOLD + challenges + ChatColor.AQUA + " Time Played: " + ChatColor.GOLD + time_played);
					p.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "=============================================");
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 2);
				}else {
					sender.sendMessage("Use /stats <player> from console");
				}
			}else { //Stats for specified player
				if(args.length > 1) {
					sender.sendMessage(ChatColor.RED + "Usage: /stats <player>");
				}else {
					if(Bukkit.getPlayer(args[0]) != null) { //The player is valid and online
						PlayerProfile prof = ProfileManager.getProfile(Bukkit.getPlayer(args[0]));
					
						int deaths = prof.getDeaths();
						int kills = prof.getKills();
						int wins = prof.getWins();
						int losses = prof.getLosses();
						double kdr = prof.getKdr();
						double wlr = prof.getWlr();
						int games = prof.getGames();
						int gold = prof.getGold();
						int captures = prof.getCaptures();
						int level = prof.getLevel();
						String time_played = prof.getTimePlayed();
						int xp = prof.getXp();
						int carrier_kills = prof.getCarrierKills();
						int challenges = prof.getChallengesCompleted();
						int assists = prof.getAssists();
						
						sender.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "=============================================");
						sender.sendMessage(ChatColor.GREEN + prof.getPName() + "'s Stats:");
						sender.sendMessage(ChatColor.AQUA + "Games Played: " + ChatColor.GOLD + games + ChatColor.AQUA + " Wins: " + ChatColor.GOLD + wins + ChatColor.AQUA + " Losses: " + ChatColor.GOLD + losses + ChatColor.AQUA + " WLR: " + ChatColor.GOLD + wlr);
						sender.sendMessage(ChatColor.AQUA + "Flags Captured: " + ChatColor.GOLD + captures + ChatColor.AQUA + " Flag Carriers Killed: " + ChatColor.GOLD + carrier_kills);
						sender.sendMessage(ChatColor.AQUA + "Assists: " + ChatColor.GOLD + assists +  ChatColor.AQUA + " Kills: " + ChatColor.GOLD + kills + ChatColor.AQUA + " Deaths: " + ChatColor.GOLD + deaths + ChatColor.AQUA + " KDR: " + ChatColor.GOLD + kdr);
						sender.sendMessage(ChatColor.AQUA + "Level: " + ChatColor.GOLD + level + ChatColor.AQUA + " XP: " + ChatColor.GOLD + xp + ChatColor.AQUA + " XP Needed: " + ChatColor.GOLD + prof.getXpToNext() + ChatColor.AQUA + " Gold: " + ChatColor.GOLD + gold);
						sender.sendMessage(ChatColor.AQUA + "Challenges Completed: " + ChatColor.GOLD + challenges + ChatColor.AQUA + " Time Played: " + ChatColor.GOLD + time_played);
						sender.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "=============================================");
						if(sender instanceof Player) {
							Player p = (Player) sender;
							p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 2);
						}
					}else if(Bukkit.getOfflinePlayer(args[0]) != null) { //The player is valid but offline
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
						ProfileManager.queryFromDatabase(target);
							new BukkitRunnable() {

								@Override
								public void run() {
									PlayerProfile prof = ProfileManager.getProfile(target);
										
									if(prof == null) {
										sender.sendMessage(ChatColor.RED + "Couldn't fetch that player's stats!");
									}else {
									GlobalLevel.checkOfflineXP(prof);
									int deaths = prof.getDeaths();
									int kills = prof.getKills();
									int wins = prof.getWins();
									int losses = prof.getLosses();
									double kdr = prof.getKdr();
									double wlr = prof.getWlr();
									int games = prof.getGames();
									int gold = prof.getGold();
									int captures = prof.getCaptures();
									int level = prof.getLevel();
									String time_played = prof.getTimePlayed();
									int xp = prof.getXp();
									int carrier_kills = prof.getCarrierKills();
									int challenges = prof.getChallengesCompleted();
									int assists = prof.getAssists();
										
									sender.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "=============================================");
									sender.sendMessage(ChatColor.GREEN + prof.getPName() + "'s Stats:");
									sender.sendMessage(ChatColor.AQUA + "Games Played: " + ChatColor.GOLD + games + ChatColor.AQUA + " Wins: " + ChatColor.GOLD + wins + ChatColor.AQUA + " Losses: " + ChatColor.GOLD + losses + ChatColor.AQUA + " WLR: " + ChatColor.GOLD + wlr);
									sender.sendMessage(ChatColor.AQUA + "Flags Captured: " + ChatColor.GOLD + captures + ChatColor.AQUA + " Flag Carriers Killed: " + ChatColor.GOLD + carrier_kills);
									sender.sendMessage(ChatColor.AQUA + "Assists: " + ChatColor.GOLD + assists +  ChatColor.AQUA + " Kills: " + ChatColor.GOLD + kills + ChatColor.AQUA + " Deaths: " + ChatColor.GOLD + deaths + ChatColor.AQUA + " KDR: " + ChatColor.GOLD + kdr);
									sender.sendMessage(ChatColor.AQUA + "Level: " + ChatColor.GOLD + level + ChatColor.AQUA + " XP: " + ChatColor.GOLD + xp + ChatColor.AQUA  + " XP Needed: " + ChatColor.GOLD + prof.getXpToNext() + ChatColor.AQUA + " Gold: " + ChatColor.GOLD + gold);
									sender.sendMessage(ChatColor.AQUA + "Challenges Completed: " + ChatColor.GOLD + challenges + ChatColor.AQUA + " Time Played: " + ChatColor.GOLD + time_played);
									sender.sendMessage("" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "=============================================");
									if(sender instanceof Player) {
										Player p = (Player) sender;
										p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 2);
									}
								}
								}
									
							}.runTaskLaterAsynchronously(CTFMain.instance, 15);
					}else {
						sender.sendMessage(ChatColor.RED + "That player is either invalid or has no stats!");
					}
				}
			}
		}
		return false;
	}
	
	

}
 