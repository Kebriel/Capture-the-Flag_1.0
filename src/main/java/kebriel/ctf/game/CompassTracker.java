package kebriel.ctf.game;

import kebriel.ctf.util.ItemBuilder;
import kebriel.ctf.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.MapManager;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;

public class CompassTracker {
	
	private Player p;
	private String tracking;
	
	public CompassTracker(Player p) {
		this.p = p;
	}
	
	public void setItem() {
		ItemStack compass = null;
		if(tracking != null) {
			if(tracking.equalsIgnoreCase("blue")) {
				compass = new ItemBuilder(Material.COMPASS, "Tracking: " + ChatColor.BLUE + "Blue Flag")
						.addLore(ChatColor.AQUA + "Click to select a flag to track")
						.toItem();
			}else if(tracking.equalsIgnoreCase("red")) {
				compass = new ItemBuilder(Material.COMPASS, "Tracking: " + ChatColor.RED + "Red Flag")
						.addLore(ChatColor.AQUA + "Click to select a flag to track")
						.toItem();
			}else if(tracking.equalsIgnoreCase("none")) {
				compass = new ItemBuilder(Material.COMPASS, "Tracking: " + ChatColor.GRAY + "Nothing")
						.addLore(ChatColor.AQUA + "Click to select a flag to track")
						.toItem();
			}
		}else {
			compass = new ItemBuilder(Material.COMPASS, "Tracking: " + ChatColor.GRAY + "Nothing")
					.addLore(ChatColor.AQUA + "Click to select a flag to track")
					.toItem();
		}
		p.getInventory().setItem(8, compass);
	}
	
	public void track(String team) {
		this.tracking = team;
		if(team != null && !tracking.equalsIgnoreCase("none")) {
			if(team.equalsIgnoreCase("red")) {
				new BukkitRunnable() {

					@Override
					public void run() {
						if(FlagManager.redFlag.getHolder() != null) {
							if(tracking.equalsIgnoreCase("red")) {
								PlayerProfile holder_prof = ProfileManager.getProfile(FlagManager.redFlag.getHolder());
								if(holder_prof.getIsSelected("perk_untrackable") && !FlagManager.redFlag.getHolder().getUniqueId().equals(p.getUniqueId())) {
									track("none");
									setItem();
									p.playSound(p.getLocation(), Sound.ENDERMAN_HIT, 0.8f, 1);
									MessageUtil.sendToPlayer(p, ChatColor.RED + "The flag that you were previously tracking was picked up by an untrackable player! Tracker reset");
									this.cancel();
								}else {
									p.setCompassTarget(FlagManager.redFlag.getHolder().getLocation());
								}
							}else {
								this.cancel();
							}
						}else {
							if(tracking.equalsIgnoreCase("red")) {
							p.setCompassTarget(MapManager.getCurrent().getRedFlag());
							}else {
								this.cancel();
							}
						}
					}
						
				}.runTaskTimer(CTFMain.instance, 0, 3);
			}else if(team.equalsIgnoreCase("blue")) {
				new BukkitRunnable() {

					@Override
					public void run() {
						if(FlagManager.blueFlag.getHolder() != null) {
							if(tracking.equalsIgnoreCase("blue")) {
								PlayerProfile holder_prof = ProfileManager.getProfile(FlagManager.blueFlag.getHolder());
								if(holder_prof.getIsSelected("perk_untrackable") && !FlagManager.blueFlag.getHolder().getUniqueId().equals(p.getUniqueId())) {
									track("none");
									setItem();
									p.playSound(p.getLocation(), Sound.ENDERMAN_HIT, 0.8f, 1);
									MessageUtil.sendToPlayer(p, ChatColor.RED + "The flag that you were previously tracking was picked up by an untrackable player! Tracker reset");
									this.cancel();
								}else {
									p.setCompassTarget(FlagManager.blueFlag.getHolder().getLocation());
								}
							}else {
								this.cancel();
							}
						}else {
							if(tracking.equalsIgnoreCase("blue")) {
								p.setCompassTarget(MapManager.getCurrent().getBlueFlag());
							}else {
								this.cancel();
							}
						}
					}
						
				}.runTaskTimer(CTFMain.instance, 0, 3);
			}
		}else {
			if(TeamHandler.redTeam.contains(p.getUniqueId())) {
				p.setCompassTarget(MapManager.getCurrent().getRedSpawn());
			}else {
				p.setCompassTarget(MapManager.getCurrent().getBlueSpawn());
			}
		}
	}

	
	public String getTracking() {
		if(tracking == null) {
			return "none";
		}else {
			return tracking;
		}
	}

}
