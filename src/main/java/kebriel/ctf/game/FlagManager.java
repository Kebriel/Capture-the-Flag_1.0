package kebriel.ctf.game;

import java.util.UUID;

import kebriel.ctf.display.ScoreboardSidebar;
import kebriel.ctf.util.MessageUtil;
import kebriel.ctf.util.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.GameTimer;
import kebriel.ctf.MapManager;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;

public class FlagManager {
	
	public static Flag redFlag;
	public static Flag blueFlag;
	
    public static int blueFlags = 0;
    public static int redFlags = 0;
	
	public static void loadFlags() {
		redFlag = new Flag("red");
		blueFlag = new Flag("blue");
	}
	
	public static void spawnFlags() {
		redFlag.spawn();
		blueFlag.spawn();
		checkForFlags();
	}
	
	public static void derenderFlags() {
		try {
			redFlag.remove();
			blueFlag.remove();
			redFlag.wipeHolder();
			blueFlag.wipeHolder();
		} catch(NullPointerException e) {}
	}
	
	public static void captureFlag(Flag flag) {
		if(flag.getTeam().equalsIgnoreCase("red")) { //Blue captured red
			blueFlags++;
			if(blueFlags == 3 || GameTimer.lightning_round) { //Blue wins
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
						if(GameTimer.lightning_round) {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.BLUE + "Blue Team " + ChatColor.GREEN + "captured a flag!");
						}else {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.BLUE + "Blue Team " + ChatColor.GREEN + "captured three flags!");
						}
						MessageUtil.sendToPlayer(p, ChatColor.BLUE + "Blue " + ChatColor.GREEN + " won the game!");
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
						PlayerProfile prof = ProfileManager.getProfile(p);
						int vic_gold = 80;
						int vic_xp = 80;
						for(UUID id : TeamHandler.blueTeam) {
							Player player = Bukkit.getPlayer(id);
							PlayerProfile pr = ProfileManager.getProfile(player);
							if(pr.getIsSelected("perk_royalty")) {
								vic_gold += 10;
								vic_xp += 10;
							}
						}
						prof.addGold(vic_gold, true);
						prof.addXp(vic_xp);
						MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Victory bonus: +" + ChatColor.AQUA + vic_xp + " XP " + ChatColor.GREEN + "+" + ChatColor.GOLD + vic_gold + "g");
						prof.addWin();
					}
					if(TeamHandler.redTeam.contains(p.getUniqueId())) {
						if(GameTimer.lightning_round) {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.BLUE + "Blue Team " + ChatColor.RED + "captured a flag!");
						}else {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", ChatColor.BLUE + "Blue Team " + ChatColor.RED + "captured three flags!");
						}
						MessageUtil.sendToPlayer(p, ChatColor.BLUE + "Blue " + ChatColor.GREEN + " won the game!");
						p.playSound(p.getLocation(), Sound.WITHER_DEATH, 0.5f, 1);
						PlayerProfile prof = ProfileManager.getProfile(p);
						int par_gold = 40;
						int par_xp = 40;
						for(UUID id : TeamHandler.redTeam) {
							Player player = Bukkit.getPlayer(id);
							PlayerProfile pr = ProfileManager.getProfile(player);
							if(pr.getIsSelected("perk_royalty")) {
								par_gold += 10;
								par_xp += 10;
							}
						}
						prof.addGold(par_gold, true);
						prof.addXp(par_xp);
						MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Participation bonus: +" + ChatColor.AQUA + par_xp + " XP " + ChatColor.GREEN + "+" + ChatColor.GOLD + par_gold + "g");
						prof.addLoss();
					}
				}
				GameTimer.endGame();
				return;
			}else {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
						TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.GREEN + "Flag captured!", ChatColor.BLUE + flag.getHolder().getName() + ChatColor.GREEN + " captured the " + ChatColor.RED + "Red Flag");
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
					}
					if(TeamHandler.redTeam.contains(p.getUniqueId())) {
						TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.RED + "Flag captured!", ChatColor.BLUE + flag.getHolder().getName() + ChatColor.RED + " captured your flag");
						p.playSound(p.getLocation(), Sound.WITHER_DEATH, 0.9f, 1);
					}
				}
			}
		}else if(flag.getTeam().equalsIgnoreCase("blue")) { //Red captured blue
			redFlags++;
			if(redFlags == 3 || GameTimer.lightning_round) { //Red wins
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(TeamHandler.redTeam.contains(p.getUniqueId())) {
						if(GameTimer.lightning_round) {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.RED + "Red Team " + ChatColor.GREEN + "captured a flag!");
						}else {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.GREEN + ChatColor.BOLD + "You won!", ChatColor.RED + "Red Team " + ChatColor.GREEN + "captured three flags!");
						}
						MessageUtil.sendToPlayer(p, ChatColor.RED + "Red " + ChatColor.GREEN + " won the game!");
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
						PlayerProfile prof = ProfileManager.getProfile(p);
						int vic_gold = 80;
						int vic_xp = 80;
						for(UUID id : TeamHandler.redTeam) {
							Player player = Bukkit.getPlayer(id);
							PlayerProfile pr = ProfileManager.getProfile(player);
							if(pr.getIsSelected("perk_royalty")) {
								vic_gold += 10;
								vic_xp += 10;
							}
						}
						prof.addGold(vic_gold, true);
						prof.addXp(vic_xp);
						MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Victory bonus: +" + ChatColor.AQUA + vic_xp + " XP " + ChatColor.GREEN + "+" + ChatColor.GOLD + vic_gold + "g");
						prof.addWin();
					}
					if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
						if(GameTimer.lightning_round) {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", "" + ChatColor.RED + "Red Team " + ChatColor.RED + "captured a flag!");
						}else {
							TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.RED + ChatColor.BOLD + "You lost!", "" + ChatColor.RED + "Red Team " + ChatColor.RED + "captured three flags!");
						}
						MessageUtil.sendToPlayer(p, ChatColor.RED + "Red " + ChatColor.GREEN + " won the game!");
						p.playSound(p.getLocation(), Sound.WITHER_DEATH, 0.5f, 1);
						PlayerProfile prof = ProfileManager.getProfile(p);
						int par_gold = 40;
						int par_xp = 40;
						for(UUID id : TeamHandler.blueTeam) {
							Player player = Bukkit.getPlayer(id);
							PlayerProfile pr = ProfileManager.getProfile(player);
							if(pr.getIsSelected("perk_royalty")) {
								par_gold += 10;
								par_xp += 10;
							}
						}
						prof.addGold(par_gold, true);
						prof.addXp(par_xp);
						MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Participation bonus: +" + ChatColor.AQUA + par_xp + " XP " + ChatColor.GREEN + "+" + ChatColor.GOLD + par_gold + "g");
						prof.addLoss();
					}
				}
				GameTimer.endGame();
				return;
			}else {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(TeamHandler.redTeam.contains(p.getUniqueId())) {
						TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.GREEN + "Flag captured!", ChatColor.RED + flag.getHolder().getName() + ChatColor.GREEN + " captured the " + ChatColor.BLUE + "Blue Flag");
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
					}
					if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
						TitleUtil.sendTitleToPlayer(p, 0, 80, 10, "" + ChatColor.RED + "Flag captured!", ChatColor.RED + flag.getHolder().getName() + ChatColor.RED + " captured your flag");
						p.playSound(p.getLocation(), Sound.WITHER_DEATH, 0.9f, 1);
					}
				}
			}
		}
		flag.capture();
		ScoreboardSidebar.gameBoard();
	}
	
	private static void checkForFlags() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if(CTFMain.theState == GameState.PLAYING) {
					for(Entity e : CTFMain.instance.getWorld().getNearbyEntities(MapManager.getCurrent().getBlueFlag(), 2, 2, 2)) {
						if(e instanceof Player) {
							Player p = (Player) e;
							if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
								if(FlagManager.blueFlag.getHolder() != null) {
									if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) { //Return blue to blue
										
										PlayerProfile prof = ProfileManager.getProfile(p);
										int gold = 50;
										int xp = 50;
										for(UUID id : TeamHandler.blueTeam) {
											Player player = Bukkit.getPlayer(id);
											PlayerProfile pr = ProfileManager.getProfile(player);
											if(pr.getIsSelected("perk_royalty")) {
												gold += 10;
												xp += 10;
											}
										}
										prof.addGold(gold, true);
										prof.addXp(xp);
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Flag returned!" + ChatColor.GOLD + " +" + gold +  "g" + ChatColor.AQUA + " +" + xp + "XP");
										MessageUtil.sendToLobby(ChatColor.BLUE + p.getName() + ChatColor.GREEN + " has returned the " + ChatColor.BLUE + "Blue Flag" + ChatColor.GREEN + " to its rightful place!");
										p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
										
										FlagManager.blueFlag.returnFlag();
									}
								}
								if(FlagManager.redFlag.getHolder() != null) {
									if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) { //Capture red by blue
										
										PlayerProfile prof = ProfileManager.getProfile(p);
										int gold = 90;
										int xp = 90;
										for(UUID id : TeamHandler.blueTeam) {
											Player player = Bukkit.getPlayer(id);
											PlayerProfile pr = ProfileManager.getProfile(player);
											if(pr.getIsSelected("perk_royalty")) {
												gold += 10;
												xp += 10;
											}
										}
										prof.addGold(gold, true);
										prof.addXp(xp);
										prof.addCapture();
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Flag captured!" + ChatColor.GOLD + " +" + gold +  "g" + ChatColor.AQUA + " +" + xp + "XP");
										
										FlagManager.captureFlag(FlagManager.redFlag);
									}
								}
							}
						}
					}
					for(Entity e : CTFMain.instance.getWorld().getNearbyEntities(MapManager.getCurrent().getRedFlag(), 2, 2, 2)) {
						if(e instanceof Player) {
							Player p = (Player) e;
							if(TeamHandler.redTeam.contains(p.getUniqueId())) {
								if(FlagManager.redFlag.getHolder() != null) {
									if(FlagManager.redFlag.getHolder().getUniqueId() == p.getUniqueId()) { //Return red to red
										
										PlayerProfile prof = ProfileManager.getProfile(p);
										int gold = 50;
										int xp = 50;
										for(UUID id : TeamHandler.redTeam) {
											Player player = Bukkit.getPlayer(id);
											PlayerProfile pr = ProfileManager.getProfile(player);
											if(pr.getIsSelected("perk_royalty")) {
												gold += 10;
												xp += 10;
											}
										}
										prof.addGold(gold, true);
										prof.addXp(xp);
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Flag returned!" + ChatColor.GOLD + " +" + gold +  "g" + ChatColor.AQUA + " +" + xp + "XP");
										MessageUtil.sendToLobby(ChatColor.RED + p.getName() + ChatColor.GREEN + " has returned the " + ChatColor.RED + "Red Flag" + ChatColor.GREEN + " to its rightful place!");
										p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
										
										FlagManager.redFlag.returnFlag();
									}
								}
								if(FlagManager.blueFlag.getHolder() != null) {
									if(FlagManager.blueFlag.getHolder().getUniqueId() == p.getUniqueId()) { //Capture blue by red
										
										PlayerProfile prof = ProfileManager.getProfile(p);
										int gold = 90;
										int xp = 90;
										for(UUID id : TeamHandler.redTeam) {
											Player player = Bukkit.getPlayer(id);
											PlayerProfile pr = ProfileManager.getProfile(player);
											if(pr.getIsSelected("perk_royalty")) {
												gold += 10;
												xp += 10;
											}
										}
										prof.addGold(gold, true);
										prof.addXp(xp);
										prof.addCapture();
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Flag captured!" + ChatColor.GOLD + " +" + gold +  "g" + ChatColor.AQUA + " +" + xp + "XP");
										
										FlagManager.captureFlag(FlagManager.blueFlag);
									}
								}
							}
						}
					}
				}else {
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 20);
	}

}
