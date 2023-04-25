package kebriel.ctf.command;

import kebriel.ctf.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import kebriel.ctf.Map;
import kebriel.ctf.game.MapVoting;

public class VoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("vote")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				if(MapVoting.voting) {
					if(args.length == 0) {
						p.sendMessage(ChatColor.RED + "Usage: /vote <#>");
					}else {
						if(!MapVoting.voters.contains(p.getUniqueId())) {
							if(StringUtils.isNumeric(args[0])) {
								int vote = 0;
								try {
									vote = Integer.parseInt(args[0]);
								} catch(Exception e) {
									p.sendMessage(ChatColor.RED + "Enter a valid number.");
									return false;
								}
								if(vote > 0 && vote < MapVoting.selected.size()) {
									Map map = MapVoting.selected.get(vote-1); //Account for index
									MapVoting.voteForMap(map, p);
									MessageUtil.sendToLobby(ChatColor.GOLD + p.getName() + ChatColor.AQUA + " voted for " + ChatColor.GREEN + map.getName());
								}else {
									p.sendMessage(ChatColor.RED + "Please vote for a number between 0 and " + MapVoting.selected.size() + ".");
								}
							}else {
								p.sendMessage(ChatColor.RED + "Please enter a valid number.");
								return false;
							}
						}else {
							p.sendMessage(ChatColor.RED + "You already voted!");
						}
					}
				}else {
					p.sendMessage(ChatColor.RED + "There is no voting going on right now!");
				}
			}
		}
		return false;
	}
	
	

}
