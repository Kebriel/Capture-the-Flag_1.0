package sylaires.ctf.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class MessageUtil {

	/*
	TODO -- Archaic class should be rebuilt with constructor and to encompass much more messaging
	 */
	
	public static String ctf_prefix = ChatColor.RED + "[" + ChatColor.GOLD + ChatColor.BOLD + "CTF" + ChatColor.RED + "]";
	
	public static void sendToPlayer(Player p, String msg) {
		p.sendMessage(ctf_prefix + " " + ChatColor.WHITE + msg);
	}
	
	public static void sendToLobby(String msg) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ctf_prefix + " " + ChatColor.WHITE + msg);
		}
	}
	
	

}
