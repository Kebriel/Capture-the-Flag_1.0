package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonLocked extends GUIButton {
	
	private ItemStack stack;
	private int price;
	private int num;
	private SlotType type;

	public GUIButtonLocked(GUISlot slot) {
		super();
		type = slot.getType();
		num = slot.getSlot();
		if(type == SlotType.ABILITY) {
			price = num == 3 ? price = 5000 : (price = 10000);
			String which = num == 3 ? (which = ChatColor.GOLD + "3rd") : (which = ChatColor.GOLD + "4th");
			stack = new ItemBuilder(Material.BEDROCK, ChatColor.GREEN + "Ability Slot " + slot.getSlot() + " " + ChatColor.RED + ChatColor.BOLD + "LOCKED")
					.addLore(ChatColor.AQUA + "You may purchase this to gain a")
					.addLore(which + ChatColor.AQUA + " ability slot")
					.addLore("  ")
					.addLore(ChatColor.DARK_AQUA + "Click to buy for " + ChatColor.GOLD + price + "g")
					.toItem();
		}else if(type == SlotType.INVENTORY) {
			price = 8000;
			stack = new ItemBuilder(Material.BEDROCK, ChatColor.YELLOW + "Extra Inventory Slot " + ChatColor.RED + ChatColor.BOLD + "LOCKED")
					.addLore(ChatColor.AQUA + "You may purchase this to open another")
					.addLore(ChatColor.AQUA + "slot in your inventory for tools or consumables")
					.addLore("  ")
					.addLore(ChatColor.DARK_AQUA + "Click to buy for " + ChatColor.GOLD + price + "g")
					.toItem();
		}else if(type == SlotType.PERK) {
			int reqlevel = num == 1 ? reqlevel = 50 : (reqlevel = 150);
			price = num == 1 ? price = 10000 : (price = 20000);
			stack = new ItemBuilder(Material.BEDROCK, ChatColor.GOLD + "Perk Slot " + slot.getSlot() + " " + ChatColor.RED + ChatColor.BOLD + "LOCKED")
					.addLore(ChatColor.AQUA + "You may purchase this to select")
					.addLore(ChatColor.AQUA + "a permanent and powerful passive perk")
					.addLore("  ")
					.addLore(ChatColor.DARK_AQUA + "Click to buy for " + ChatColor.GOLD + price + "g")
					.addLore("    ")
					.addLore(ChatColor.YELLOW + "You must be Level " + ChatColor.WHITE + reqlevel + ChatColor.YELLOW + " to purchase this")
					.toItem();
		}
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(!prof.getIsSlotUnlocked(num, type)) { //Buy
			if(type == SlotType.PERK) { //Level check
				if(!prof.getIsSlotUnlocked(1, SlotType.PERK) && num == 2) {
					MessageUtil.sendToPlayer(p, ChatColor.RED + "You must buy the slot before this first!");
					p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					return;
				}
				switch (num) {
				case 1: if(prof.getLevel() < 50) {MessageUtil.sendToPlayer(p, ChatColor.RED + "You are not the required level!"); p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1); return;} break;
				case 2: if(prof.getLevel() < 150) {MessageUtil.sendToPlayer(p, ChatColor.RED + "You are not the required level!"); p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1); return;} break;
				default: throw new IllegalArgumentException("Invalid slot number");
				}
			}
			if(type == SlotType.ABILITY) {
				if(!prof.getIsSlotUnlocked(3, SlotType.ABILITY) && num == 4) {
					MessageUtil.sendToPlayer(p, ChatColor.RED + "You must buy the slot before this first!");
					p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					return;
				}
			}
			if(prof.getGold() < price) {
				MessageUtil.sendToPlayer(p, ChatColor.RED + "You cannot afford this!");
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
			}else {
				ConfirmationGUI.open(PurchaseType.SLOT, this, p);
			}
		}else { //Shouldn't be possible for this code to execute -- equivalent to stacktrace
			MessageUtil.sendToPlayer(p, ChatColor.RED + "You already own this!");
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
		}
	}
	
	public int getCost() {
		return price;
	}
	
	public SlotType getType() {
		return type;
	}
	
	public int getSlotNum() {
		return num;
	}

}
