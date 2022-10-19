package sylaires.ctf.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.display.ScoreboardSidebar;
import sylaires.ctf.game.FlagManager;
import sylaires.ctf.game.KillHandler;
import sylaires.ctf.game.TeamHandler;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class Quit implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ScoreboardSidebar.idleBoard();
		PlayerProfile prof = ProfileManager.getProfile(p);
		try {
			if(ProfileManager.getProfile(p).getEffect() != null) {
				ProfileManager.getProfile(p).getEffect().disable();
			}
		}catch(NullPointerException ex) {}
		
		if(TeamHandler.redQueue.contains(p.getUniqueId())) {
			TeamHandler.redQueue.remove(p.getUniqueId());
		}else if(TeamHandler.blueQueue.contains(p.getUniqueId())) {
			TeamHandler.blueQueue.remove(p.getUniqueId());
		}
		
		//Prefix stuff
		TeamHandler.packet_cache.remove(p);
		
		//Assists
		if(CTFMain.theState == GameState.PLAYING) {
			if(TeamHandler.redTeam.contains(p.getUniqueId())) {
				e.setQuitMessage(ChatColor.RED + p.getName() + ChatColor.GRAY + " has left");
			}else if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
				e.setQuitMessage(ChatColor.BLUE + p.getName() + ChatColor.GRAY + " has left");
			}else {
				e.setQuitMessage(ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has left");
			}
			if(!prof.getLastHitters().isEmpty()) {	//Has taken damage from someone
				KillHandler.killPlayer(p, DamageCause.ENTITY_ATTACK);
			}
			try {
				if(FlagManager.redFlag.getHolder().getUniqueId().equals(p.getUniqueId())) { //Holder logged off with the flag
					FlagManager.redFlag.drop();
				}
			}catch(NullPointerException ex) {}
			try {
				if(FlagManager.blueFlag.getHolder().getUniqueId().equals(p.getUniqueId())) {
					FlagManager.blueFlag.drop();
				}
			} catch(NullPointerException ex) {}
		}else {
			e.setQuitMessage(ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has left");
		}
		
		
		//Save player data
		ProfileManager.getProfile(p).saveToDB();
		//De-cache this player
		ProfileManager.removeProfile(p);
		}

}
