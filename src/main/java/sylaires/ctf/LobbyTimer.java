package sylaires.ctf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import sylaires.ctf.display.ScoreboardSidebar;
import sylaires.ctf.game.CompassTracker;
import sylaires.ctf.game.MapVoting;
import sylaires.ctf.game.TeamHandler;
import sylaires.ctf.util.MessageUtil;
import sylaires.ctf.util.TitleUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class LobbyTimer {

	/*
	TODO Replace static field with local + getter, replace static method with constructor called in onEnable()
	 */

	public static int timer; //The global timer integer for LobbyTimer
	
	/*
	 * init method that will begin the process of waiting for players, or counting down to start
	 */
	public static void init() {
		CTFMain.theState = GameState.WAITING; //Set to Waiting by default
		timer = 60; //Timer to display waiting message every minute
		MapVoting.reset(); //Ensure that MapVoting is ready
		ScoreboardSidebar.idleBoard(); //Setup scoreboard for all players
		new BukkitRunnable() { //Initiate runnable 
			@Override
			public void run() {
				if(CTFMain.theState == GameState.WAITING || CTFMain.theState == GameState.STARTING) { //Make sure we're either waiting or starting
					if(CTFMain.theState == GameState.WAITING) { //Check to see if game is waiting
						if(Bukkit.getOnlinePlayers().size() < CTFMain.instance.getReqPlayers()) { //If there are not enough players
							if(timer == 0) { //Reset timer, update scoreboard, notify lobby
								MessageUtil.sendToLobby(ChatColor.AQUA + "Waiting for players!");
								timer = 60;
								ScoreboardSidebar.idleBoard();
							}
						}else { //There are enough players to start
							CTFMain.theState = GameState.STARTING; //Update GameState
							timer = 30; //Set timer to countdown 30 sec
							MapVoting.init();
							MapVoting.voting = true; //Voting is now occurring 
							ScoreboardSidebar.votingBoard(); //Update scoreboard for lobby
							MessageUtil.sendToLobby(ChatColor.AQUA + "The game is starting in " + ChatColor.WHITE + timer + ChatColor.AQUA + " seconds!");
						}
					}else {
						if(Bukkit.getOnlinePlayers().size() < CTFMain.instance.getReqPlayers()) { //If there are no longer enough players, revert to waiting again
							CTFMain.theState = GameState.WAITING;
							MessageUtil.sendToLobby(ChatColor.AQUA + "Waiting for players!");
							timer = 60;
							ScoreboardSidebar.idleBoard();
						}else{
							if(timer == 10 || timer == 5 || timer == 3 || timer == 2 || timer == 1) { //Notification for countdown on key numbers
								MessageUtil.sendToLobby(ChatColor.AQUA + "The game is starting in " + ChatColor.WHITE + timer + ChatColor.AQUA + " seconds!");
							}
							if(timer == 5) { //End voting 5 sec before game start
								MessageUtil.sendToLobby(ChatColor.AQUA + "Voting has ended. The map " + ChatColor.GOLD + MapVoting.getWinner().getName() + ChatColor.AQUA + " has won!");
							}
							if(timer == 0) { //Start game
								MessageUtil.sendToLobby(ChatColor.YELLOW + "The game has started!"); //Notify lobby

								TeamHandler.createTeams(); //Divide players into teams

								for(Player p : Bukkit.getOnlinePlayers()) {
									TeamHandler.applyPrefixes(p); //Apply proper team colors to all player names
									PlayerProfile prof = ProfileManager.getProfile(p);
									prof.setCompassTracker(new CompassTracker(p)); //Load a compass tracker object for each player
									p.setFallDistance(0.0f); //Fixes bug where players die instantly when being teleported

									//Teleport players to their appropriate spawns
									if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
										p.teleport(MapManager.getCurrent().getBlueSpawn().add(0, 1, 0));
										TitleUtil.sendTitleToPlayer(p, 0, 40, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "Game started!", ChatColor.GOLD + "You are on the " + ChatColor.BLUE + "Blue Team");
									}else if(TeamHandler.redTeam.contains(p.getUniqueId())){
										p.teleport(MapManager.getCurrent().getRedSpawn().add(0, 1, 0));
										TitleUtil.sendTitleToPlayer(p, 0, 40, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "Game started!", ChatColor.GOLD + "You are on the " + ChatColor.RED + "Red Team");
									}
									Items.gameItems(p); //Set player inventories
								}

								MapVoting.reset(); //Reset map voting for next game
								CTFMain.theState = GameState.PLAYING; //Set GameState properly

								GameTimer.init(); //Initiate GameTimer
								ScoreboardSidebar.gameBoard(); //Switch Scoreboard
								this.cancel(); //Terminate current process
							}
						}
					}
					timer--;
				}else { //Game is not proper GameState, terminate
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 20); //Runs every second on the main thread
	}

}
