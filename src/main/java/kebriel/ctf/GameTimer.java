package kebriel.ctf;

import java.util.Random;

import java.util.UUID;

import kebriel.ctf.display.NPCSpawn;
import kebriel.ctf.display.RenderHandler;
import kebriel.ctf.display.ScoreboardSidebar;
import kebriel.ctf.game.AbilityEffect;
import kebriel.ctf.game.AlchemistTimer;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.game.TeamHandler;
import kebriel.ctf.util.MessageUtil;
import kebriel.ctf.util.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GameTimer {
	
	/*
	 * TODO Migrate all static fields to private w/ getters
	 */
	
	//Public fields storing NPCSpawn objects
	public static NPCSpawn blueNpc;
	public static NPCSpawn redNpc;
	//Public integer storing the current timer's value
	public static int timer;
	//Private BukkitTask, storing the current timer
	private static BukkitTask runnable;
	//Public boolean -- whether or not a lightning round (tiebreaker) is currently enabled
	public static boolean lightning_round;
	
	/*
	 * Init method called when game starts -- called in LobbyTimer class 
	 */
	public static void init() {
		timer = 1200; //Set timer to arbitrary countdown in ticks
		lightning_round = false; //The game just started -- it's not a lightning round
		FlagManager.loadFlags(); //Run the loadFlags() method -- which handles creation of Flag objects
		new BukkitRunnable() { //Runnable to spawn flags after arbitrary slight delay -- failsafe if system is running slowly - WILL BE REMOVED

			@Override
			public void run() {
				FlagManager.spawnFlags();
			}
			
		}.runTaskLater(CTFMain.instance, 10);
		
		//Load and spawn NPCs 
		blueNpc = NPCSpawn.getNew(MapManager.getCurrent().getBlueNpc()); blueNpc.spawn();
		redNpc = NPCSpawn.getNew(MapManager.getCurrent().getRedNpc()); redNpc.spawn();
		
		ScoreboardSidebar.gameBoard(); //Set the current sideboard to the Game Board
		//Begin the re-render cycles to keep packet-created elements visible to players newly within range, or that just logged in
		RenderHandler.init();
		AbilityEffect.init(); //Allow timed abilities to begin
		AlchemistTimer.init(); //Allow the Alchemist ability to initiate
		runnable = new BukkitRunnable() { //Set runnable field to new BukkitRunnable

			@Override
			public void run() {
				if(CTFMain.theState == GameState.PLAYING) { //Check that the game hasn't ended
					if(Bukkit.getOnlinePlayers().size() == 0) { //If there's nobody online, reset the game
						LobbyTimer.init();
						TeamHandler.reset();
						
						FlagManager.blueFlags = 0;
						FlagManager.redFlags = 0;
						FlagManager.derenderFlags();
						redNpc.remove();
						blueNpc.remove();
						ScoreboardSidebar.idleBoard();
						this.cancel();
					}
					//Begin to handle victory paths, considered every tick
					if(timer == 0 && lightning_round) { //The timer ran out and it's a lightning round -- team with most flags wins by default
						if(FlagManager.redFlags > FlagManager.blueFlags) { //Red wins
							for(UUID id : TeamHandler.redTeam) { //Victory msg to every red player -- TODO consolidate messaging
								Player player = Bukkit.getPlayer(id);
								TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Your team had the most captures!");
								player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
								
								PlayerProfile prof = ProfileManager.getProfile(player);
								prof.addWin();
							}
							for(UUID id : TeamHandler.blueTeam) { //Defeat msg to every blue player -- TODO consolidate messaging
								Player player = Bukkit.getPlayer(id);
								TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Your team had the fewest captures!");
								player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);

								PlayerProfile prof = ProfileManager.getProfile(player);
								prof.addLoss();
							}
							endGame();
						}else if(FlagManager.blueFlags > FlagManager.redFlags) { //Blue wins
							for(UUID id : TeamHandler.blueTeam) { //Victory msg to every blue player -- TODO consolidate messaging
								Player player = Bukkit.getPlayer(id);
								TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Your team had the most captures!");
								player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
								
								PlayerProfile prof = ProfileManager.getProfile(player);
								prof.addWin();
							}
							for(UUID id : TeamHandler.redTeam) { //Defeat msg to every red player -- TODO consolidate messaging
								Player player = Bukkit.getPlayer(id);
								TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Your team had the fewest captures!");
								player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
								
								PlayerProfile prof = ProfileManager.getProfile(player);
								prof.addLoss();
							}
							endGame(); //Call method to reset game and handle ending
						}else { //Tied in flags -- most kills wins
							//Prepare fields to tally up kills -- May track total team kills in the future
							int red_kills = 0;
							int blue_kills = 0;
							for(UUID id : TeamHandler.redTeam) { //Tally red kills
								Player player = Bukkit.getPlayer(id);
								PlayerProfile prof = ProfileManager.getProfile(player);
								red_kills += prof.getKillsThisGame();
							}
							for(UUID id : TeamHandler.blueTeam) { //Tally blue kills
								Player player = Bukkit.getPlayer(id);
								PlayerProfile prof = ProfileManager.getProfile(player);
								blue_kills += prof.getKillsThisGame();
							}
							if(red_kills > blue_kills) { //If red has more, red wins 
								for(UUID id : TeamHandler.redTeam) { //Victory msgs to red players -- TODO consolidate messaging
									Player player = Bukkit.getPlayer(id);
									TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Your team had the most kills!");
									player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
									
									PlayerProfile prof = ProfileManager.getProfile(player);
									prof.addWin();
								}
								for(UUID id : TeamHandler.blueTeam) { //Defeat msgs to blue players -- TODO consolidate messaging
									Player player = Bukkit.getPlayer(id);
									TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Your team had the fewest kills!");
									player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
									
									PlayerProfile prof = ProfileManager.getProfile(player);
									prof.addLoss();
								}
							}else if(blue_kills > red_kills) { //If blue has more, blue wins 
								for(UUID id : TeamHandler.blueTeam) { //Victory msgs to blue players -- TODO consolidate messaging
									Player player = Bukkit.getPlayer(id);
									TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Your team had the most kills!");
									player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
									
									PlayerProfile prof = ProfileManager.getProfile(player);
									prof.addWin();
								}
								for(UUID id : TeamHandler.redTeam) { //Defeat msgs to red players -- TODO consolidate messaging
									Player player = Bukkit.getPlayer(id);
									TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Your team had the fewest kills!");
									player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
									
									PlayerProfile prof = ProfileManager.getProfile(player);
									prof.addLoss();
								}
							}else { //Kills and flag captures are tied -- A highly unlikely anomaly
								//Announce rare occurance to lobby
								MessageUtil.sendToLobby(ChatColor.GREEN + "Teams are tied on both kills and captures -- victor will be randomly selected!");
								//Prepare to select random victor
								Random rand = new Random();
								int rand_vic = rand.nextInt(2);
								if(rand_vic == 0) { //Blue randomly chosen
									for(UUID id : TeamHandler.blueTeam) {
										Player player = Bukkit.getPlayer(id);
										TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Your team won the tie!");
										player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
										
										PlayerProfile prof = ProfileManager.getProfile(player);
										prof.addWin();
									}
									for(UUID id : TeamHandler.redTeam) {
										Player player = Bukkit.getPlayer(id);
										TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Your team lost the tie!");
										player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
										
										PlayerProfile prof = ProfileManager.getProfile(player);
										prof.addLoss();
									}
								}else { //Red randomly chosen
									for(UUID id : TeamHandler.redTeam) {
										Player player = Bukkit.getPlayer(id);
										TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.YELLOW + "Your team won the tie!");
										player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
										
										PlayerProfile prof = ProfileManager.getProfile(player);
										prof.addWin();
									}
									for(UUID id : TeamHandler.blueTeam) {
										Player player = Bukkit.getPlayer(id);
										TitleUtil.sendTitleToPlayer(player, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.YELLOW + "Your team lost the tie!");
										player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1, 1);
										
										PlayerProfile prof = ProfileManager.getProfile(player);
										prof.addLoss();
									}
								}
							}
							endGame(); //Method called to reset game and handle ending
						}
					}
					if(timer == 0) { //If the original timer has fully ticked -- only called if somebody hasn't yet won by normal means
						timer = 480; //Set timer to new duration
						lightning_round = true; //It's now a lightning round, as game didn't end quickly enough
						
						for(Player p : Bukkit.getOnlinePlayers()) { //Notify players
							TitleUtil.sendTitleToPlayer(p, 0, 60, 10, "" + ChatColor.YELLOW + ChatColor.BOLD + "Deathmatch has begun!", ChatColor.GREEN + "The first team to capture a flag wins!");
							p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
						}
					}
					ScoreboardSidebar.update();
					
					timer--; //Decrement the timer
				}else { //The game has ended, reset timer and cancel runnable 
					timer = 0;
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 20); //Runs on main thread every second
	}
	
	/*
	 * Method called by all forms of game ending
	 */
	public static void endGame() {
		timer = 1200; //Timer reset
		runnable.cancel(); //If the countdown hasn't otherwise been terminated, do it now -- failsafe
		CTFMain.theState = GameState.ENDING; //Set the GameState properly
		MessageUtil.sendToLobby(ChatColor.GOLD + "The game is over!"); //Notify players
		ScoreboardSidebar.gameBoard(); //Update scoreboard
		new BukkitRunnable() { //Runnable to reset everything after a 7 second delay 

			@Override
			public void run() { 
				LobbyTimer.init(); //Start the lobby timer again
				TeamHandler.reset(); //Reset teams
				
				for(Player p : Bukkit.getOnlinePlayers()) {
					PlayerInventory inv = p.getInventory(); 
					for(PotionEffect pot : p.getActivePotionEffects()) { //Clear all effects from players
						p.removePotionEffect(pot.getType());
					}
					inv.clear(); //Empty all player inventories
					p.teleport(CTFMain.instance.getHub()); //Teleport all players to Hub location
					PlayerProfile prof = ProfileManager.getProfile(p);
					prof.addGame(); //Add a game played to player stats -- means they have to stay until end for it to count
					prof.gameReset(); //Resets all 'game specific' stats for each player
					p.setGameMode(GameMode.SURVIVAL); //Ensure no players are left in Spectator Mode from death
					prof.setDead(false); //Ensure no players are set still as dead
					
					/*
					 * TODO Undesirable: this will currently unvanish any vanished mods -- add support for staff perms when necessary
					 */
					
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.showPlayer(p); //Ensure no players are vanished 
					}
					
					Items.lobbyItems(p); //Give players all of their lobby items
				}
				//Set global flag count to zero
				FlagManager.blueFlags = 0;
				FlagManager.redFlags = 0;
				FlagManager.derenderFlags(); //Remove current flags
				//Despawn NPCs
				redNpc.remove();
				blueNpc.remove();
				ScoreboardSidebar.idleBoard(); //Set scoreboard back to Idle
			}
			
		}.runTaskLater(CTFMain.instance, 140); //Delayed task runs on main thread to accommodate Bukkit methods
		
		new BukkitRunnable() { //Runnable to display top players after a delay

			@Override
			public void run() { 
				//Find player with highest kills by comparison
				Player highest_kills = null;
				int killnum = 0;
				for(Player p : Bukkit.getOnlinePlayers()) {
					PlayerProfile prof = ProfileManager.getProfile(p);
					if(highest_kills == null) {
						highest_kills = p;
						killnum = prof.getKillsThisGame();
					}
					if(prof.getKillsThisGame() > ProfileManager.getProfile(highest_kills).getKillsThisGame()) {
						highest_kills = p;
						killnum = prof.getKillsThisGame();
					}
				}
				
				//Find player with most flags captured by comparison
				Player most_flags = null;
				int flags = 0;
				for(Player p : Bukkit.getOnlinePlayers()) {
					PlayerProfile prof = ProfileManager.getProfile(p);
					if(most_flags == null) {
						most_flags = p;
						flags = prof.getCapturesThisGame();
					}
					if(prof.getCapturesThisGame() > ProfileManager.getProfile(most_flags).getCapturesThisGame()) {
						most_flags = p;
						flags = prof.getCapturesThisGame();
					}
				}
				
				//Update PlayerProfile objects with top players to load their stats
				PlayerProfile killer_prof = ProfileManager.getProfile(highest_kills);
				PlayerProfile flag_prof = ProfileManager.getProfile(most_flags);
				for(Player p : Bukkit.getOnlinePlayers()) { //Send raw message to every online player -- TODO potentially show top players per team, and/or use chatcolor to designate team
					p.sendMessage(ChatColor.GOLD + "-----  " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Top Players: " + ChatColor.GOLD + "  -----");
					p.sendMessage("  ");
					p.sendMessage(ChatColor.YELLOW + "-" + ChatColor.GOLD + "Top Killer: " + killer_prof.getColoredName() + " - " + ChatColor.GREEN + killnum + " Kills");
					p.sendMessage(ChatColor.YELLOW + "-" + ChatColor.GOLD + "Most Flags Captured: " + flag_prof.getColoredName() + " - " + ChatColor.GREEN + flags + " Flags");
					p.sendMessage("  ");
					p.sendMessage(ChatColor.GOLD + "---------------------------");
				}
			}
			
		}.runTaskLater(CTFMain.instance, 40); //Delayed task runs after 2 sec on main thread
	}

}
