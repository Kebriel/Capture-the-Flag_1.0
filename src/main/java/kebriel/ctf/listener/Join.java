package kebriel.ctf.listener;

import kebriel.ctf.cosmetic.AuraEffect;
import kebriel.ctf.cosmetic.CosmeticType;
import kebriel.ctf.display.ScoreboardSidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.GameTimer;
import kebriel.ctf.Items;
import kebriel.ctf.MapManager;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.CompassTracker;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.game.GlobalLevel;
import kebriel.ctf.game.MapVoting;
import kebriel.ctf.game.TeamHandler;

public class Join implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setFoodLevel(20);
		p.setGameMode(GameMode.SURVIVAL);
		e.setJoinMessage(ChatColor.AQUA + p.getName() + ChatColor.GRAY + " has joined");
		
		new BukkitRunnable() {

			@Override
			public void run() {
				GlobalLevel.checkXP(p);
				PlayerProfile prof = ProfileManager.getProfile(p);
				prof.addGold(200000, false);
				if(prof.getLevel() < 300) {
					prof.setLevel(300);
				}
				try {
					if(!prof.getSelectedCosmetic(CosmeticType.AURA).equalsIgnoreCase("none")) {
						prof.setEffect(new AuraEffect(p, prof.getSelectedCosmetic(CosmeticType.AURA)));
					}
				} catch(NullPointerException e) {	}
			}
			
		}.runTaskLater(CTFMain.instance, 4);
		
		if(CTFMain.theState == GameState.WAITING) {
			ScoreboardSidebar.idleBoard();
		}else if(CTFMain.theState == GameState.STARTING) {
			ScoreboardSidebar.votingBoard();
		}else if(CTFMain.theState == GameState.PLAYING || CTFMain.theState == GameState.ENDING) {
			ScoreboardSidebar.gameBoard();
		}
		
		if(CTFMain.theState != GameState.PLAYING) {
			p.setHealth(p.getMaxHealth());
		}
		
		//Inventories and sb
		if(CTFMain.theState == GameState.WAITING || CTFMain.theState == GameState.STARTING) {
			p.getInventory().clear();
			PlayerProfile prof = ProfileManager.getProfile(p);
			prof.setDead(false);
			Items.lobbyItems(p);
			new BukkitRunnable() {

				@Override
				public void run() {
					if(CTFMain.theState == GameState.WAITING) {
						ScoreboardSidebar.idleBoard();
					}else {
						ScoreboardSidebar.votingBoard(p);
					}
				}
				
			}.runTaskLater(CTFMain.instance, 5);
		}
		
		//Join during voting
		if(MapVoting.voting) MapVoting.displayOptions(p);
		
		//Handle mid-game join
		if(CTFMain.theState == GameState.PLAYING || CTFMain.theState == GameState.ENDING) {
			PlayerProfile prof = ProfileManager.getProfile(p);
			if(!prof.isDead()) {
				TeamHandler.addTardyPlayer(p);
				FlagManager.blueFlag.renderForPlayer(p);
				FlagManager.redFlag.renderForPlayer(p);
				GameTimer.blueNpc.renderForPlayer(p);
				GameTimer.redNpc.renderForPlayer(p);
				prof.setCompassTracker(new CompassTracker(p));
				Items.gameItems(p);
				if(TeamHandler.redTeam.contains(p.getUniqueId())) {
					p.teleport(MapManager.getCurrent().getRedSpawn());
				}else {
					p.teleport(MapManager.getCurrent().getBlueSpawn());
				}
				
				//Team prefixes
				new BukkitRunnable() {

					@Override
					public void run() {
						TeamHandler.applyPrefixes(e.getPlayer());
						for(Player p : Bukkit.getOnlinePlayers()) {
							if(!p.getUniqueId().toString().equals(e.getPlayer().getUniqueId().toString())) { //It isn't the player that just joined
								TeamHandler.applyPacketToPlayer(e.getPlayer(), TeamHandler.packet_cache.get(p));
							}
						}
					}
					
				}.runTaskLater(CTFMain.instance, 3);
				ScoreboardSidebar.gameBoard(p);
			}
		}else {
			p.teleport(CTFMain.instance.getHub());
		}
		
		
		
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		//Profiles db stuff
		ProfileManager.addProfile(new PlayerProfile(p));
	}

}
