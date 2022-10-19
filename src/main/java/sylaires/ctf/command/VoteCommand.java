package sylaires.ctf.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sylaires.ctf.Map;
import sylaires.ctf.game.MapVoting;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

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
								if(vote > 0 && vote < 4) {
									Map map = MapVoting.selected.get(vote-1); //Account for index
									MapVoting.voteForMap(map, p);
									MessageUtil.sendToLobby(ChatColor.GOLD + p.getName() + ChatColor.AQUA + " voted for " + ChatColor.GREEN + map.getName());
								}else {
									p.sendMessage(ChatColor.RED + "Please vote for options 1-3.");
								}
							}else {
								p.sendMessage(ChatColor.RED + "Enter a valid number.");
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
