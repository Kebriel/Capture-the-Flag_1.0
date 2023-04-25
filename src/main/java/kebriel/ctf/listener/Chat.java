package kebriel.ctf.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.TeamHandler;

public class Chat implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		e.setCancelled(true);
		PlayerProfile prof = ProfileManager.getProfile(p);
		
		String prefix = prof.getRankPrefix();
		
		if(CTFMain.theState == GameState.PLAYING || CTFMain.theState == GameState.ENDING) {
			String team = "";
			if(TeamHandler.redTeam.contains(p.getUniqueId())) {
				team = "red";
			}else {
				team = "blue";
			}
			//Team chat is default -- shout command handles other
			if(team.equalsIgnoreCase("red")) {
				for(UUID id : TeamHandler.redTeam) {
					Player player = Bukkit.getPlayer(id);
					player.sendMessage(
							"" + ChatColor.DARK_GRAY + ChatColor.BOLD + "<" + ChatColor.GRAY + ChatColor.BOLD + "TEAM CHAT" + ChatColor.DARK_GRAY + ChatColor.BOLD + "> "
							+ prefix + " "
							+ ChatColor.AQUA + ChatColor.BOLD + "[" + ChatColor.WHITE + ChatColor.BOLD + prof.getLevel() + ChatColor.AQUA + ChatColor.BOLD + "] "
							+ ChatColor.WHITE + p.getName() + ": " + e.getMessage());
				}
			}else {
				for(UUID id : TeamHandler.blueTeam) {
					Player player = Bukkit.getPlayer(id);
					player.sendMessage(
							"" + ChatColor.DARK_GRAY + ChatColor.BOLD + "<" + ChatColor.GRAY + ChatColor.BOLD + "TEAM CHAT" + ChatColor.DARK_GRAY + ChatColor.BOLD + "> "
							+ prefix + " "
							+ ChatColor.AQUA + ChatColor.BOLD + "[" + ChatColor.WHITE + ChatColor.BOLD + prof.getLevel() + ChatColor.AQUA + ChatColor.BOLD + "] "
							+ ChatColor.WHITE + p.getName() + ": " + e.getMessage());
				}
			}
		}else {
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(prefix != "") {
					player.sendMessage(
							prefix + " "
							+ ChatColor.AQUA + ChatColor.BOLD + "[" + ChatColor.WHITE + ChatColor.BOLD + prof.getLevel() + ChatColor.AQUA + ChatColor.BOLD + "] "
							+ ChatColor.WHITE + p.getName() + ": " + e.getMessage());
				}else {
					player.sendMessage("" + ChatColor.AQUA + ChatColor.BOLD + "[" + ChatColor.WHITE + ChatColor.BOLD + prof.getLevel() + ChatColor.AQUA + ChatColor.BOLD + "] "
							+ ChatColor.WHITE + p.getName() + ": " + e.getMessage());
				}
			}
		}
	}

}
