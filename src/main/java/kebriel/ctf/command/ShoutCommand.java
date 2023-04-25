package kebriel.ctf.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.TeamHandler;

public class ShoutCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("shout")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(CTFMain.theState == GameState.PLAYING || CTFMain.theState == GameState.ENDING) {
					if(args.length == 0) {
						p.sendMessage(ChatColor.RED + "Usage: /shout <message>");
					}else {
						String msg = "";
						for(int i = 0; i < args.length; i++) {
							if(i == 0) {
								msg += args[i];
							}else {
								msg += " " + args[i];
							}
						}
						
						PlayerProfile prof = ProfileManager.getProfile(p);
						String prefix = prof.getRankPrefix();
						
						String teamPref = "";
						if(TeamHandler.redTeam.contains(p.getUniqueId())) {
							teamPref = ChatColor.RED + "RED";
						}else if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
							teamPref = ChatColor.BLUE + "BLUE";
						}
						
						for(Player player : Bukkit.getOnlinePlayers()) {
							player.sendMessage(
									ChatColor.GRAY + "[" + teamPref + ChatColor.GRAY + "] "
									+ prefix + " "
									+ ChatColor.AQUA + ChatColor.BOLD + "[" + ChatColor.WHITE + ChatColor.BOLD + prof.getLevel() + ChatColor.AQUA + ChatColor.BOLD + "] "
									+ ChatColor.WHITE + p.getName() + ": " + msg);
						}
					}
				}else {
					p.sendMessage(ChatColor.RED + "You can only shout during a game!");
				}
			}
		}
		return false;
	}
	
	

}
