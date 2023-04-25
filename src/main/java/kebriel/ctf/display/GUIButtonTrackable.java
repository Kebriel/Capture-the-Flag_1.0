package kebriel.ctf.display;

import kebriel.ctf.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.CompassTracker;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.util.ItemBuilder;

public class GUIButtonTrackable extends GUIButton {
	
	private InventoryHolder gui;
	private ItemStack stack;
	private String tracking;
	Player p;
	
	public GUIButtonTrackable(InventoryHolder gui, String toTrack, Player p) {
		this.gui = gui;
		tracking = toTrack;
		this.p = p;
		
		PlayerProfile prof = ProfileManager.getProfile(p);
		CompassTracker tracker = prof.getCompassTracker();
		
		stack = new ItemBuilder(Material.BANNER)
				.addLore(ChatColor.YELLOW + "Click to track this flag")
				.toItem();
		
		if(toTrack.equalsIgnoreCase("red")) {
			stack = new ItemBuilder(stack, ChatColor.RED + "Red Flag").toItem();
			BannerMeta meta = (BannerMeta) stack.getItemMeta();
			meta.setBaseColor(DyeColor.RED);
			stack.setItemMeta(meta);
		}else if(toTrack.equalsIgnoreCase("blue")) {
			stack = new ItemBuilder(stack, ChatColor.BLUE + "Blue Flag").toItem();
			BannerMeta meta = (BannerMeta) stack.getItemMeta();
			meta.setBaseColor(DyeColor.BLUE);
			stack.setItemMeta(meta);
		}else {
			throw new IllegalArgumentException("Invalid team to track!");
		}
		
		if(tracker.getTracking().equalsIgnoreCase(toTrack)) { //You are already tracking the flag linked to this button
			stack = new ItemBuilder(stack).setLore(0, ChatColor.GREEN + "You are currently tracking this flag")
					.addEnchantment(Enchantment.ARROW_DAMAGE, 0).addFlag(ItemFlag.HIDE_ENCHANTS).toItem();
		}
		Player holder = null;
		if(tracking.equalsIgnoreCase("blue")) {
			holder = FlagManager.blueFlag.getHolder();
		}else if(tracking.equalsIgnoreCase("red")) {
			holder = FlagManager.redFlag.getHolder();
		}
		if(holder != null) {
			PlayerProfile holder_prof = ProfileManager.getProfile(holder);
			if(holder_prof.getIsSelected("perk_untrackable")) {
				stack = new ItemBuilder(stack).addLore(" ").addLore(ChatColor.RED + "The player holding this flag cannot be tracked!").toItem();
			}
		}
		
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		if(gui instanceof TrackerGUI) {
			
			Player holder = null;
			if(tracking.equalsIgnoreCase("blue")) {
				holder = FlagManager.blueFlag.getHolder();
			}else if(tracking.equalsIgnoreCase("red")) {
				holder = FlagManager.redFlag.getHolder();
			}
			if(holder != null) {
				PlayerProfile holder_prof = ProfileManager.getProfile(holder);
				if(holder_prof.getIsSelected("perk_untrackable")) {
					MessageUtil.sendToPlayer(holder, ChatColor.RED + "The player holding that flag has a perk preventing them from being tracked!");
					p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					return;
				}
			}
			
			PlayerProfile prof = ProfileManager.getProfile(p);
			CompassTracker tracker = prof.getCompassTracker();
			
			if(tracker.getTracking().equalsIgnoreCase(tracking)) {
				MessageUtil.sendToPlayer(holder, ChatColor.RED + "You are already tracking that flag!");
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
				return;
			}
			
			tracker.track(tracking);
			tracker.setItem();
			MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Tracking...");
			p.playSound(p.getLocation(), Sound.ORB_PICKUP, 0.5f, 1);
			TrackerGUI.getNew(p);
		}
	}

}
