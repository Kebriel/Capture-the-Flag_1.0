package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonOption extends GUIButton {
	
	private ItemStack stack;
	private ConfirmationGUI gui;
	//True equals 'Yes', false equals 'No'
	private boolean type;
	private int[] slots;
	
	public GUIButtonOption(ConfirmationGUI gui, boolean type) {
		String cost = "" + gui.getCost() + "g";
		if(type) {
			stack = new ItemBuilder(Material.STAINED_CLAY, "" + ChatColor.GREEN + ChatColor.BOLD + "YES", (byte) 5)
					.addLore(ChatColor.AQUA + "This will cost you " + ChatColor.GOLD + cost)
					.toItem();
		}else {
			stack = new ItemBuilder(Material.STAINED_CLAY, "" + ChatColor.RED + ChatColor.BOLD + "NO", (byte) 14)
					.toItem();
		}
		
		this.type = type;
		this.gui = gui;
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	public ConfirmationGUI getGUI() {
		return gui;
	}
	
	public void setItemArray(int[] slots, Inventory inv) {
		this.slots = slots;
		for(int i : slots) {
			inv.setItem(i, getItem());
		}
	}
	
	public int[] getSlots() {
		return slots;
	}
	
	public boolean getType() {
		return type;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(getType()) { //They clicked yes
			PlayerProfile prof = ProfileManager.getProfile(p);
			if(gui.getType() == PurchaseType.ABILITY) { //Handle ability purchasing
				prof.subtractGold(gui.toPurchase().getCost(), false);
				prof.unlock(gui.toPurchase().getId());
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				prof.setSelected(gui.toPurchase().getId(), gui.getSlotNum(), gui.getSlotType());
				if(gui.getSlotType() == SlotType.ABILITY) {
					MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Ability purchased!");
					AbilityGUI.getNew(p, 0, gui.getSlotNum());
				}else if(gui.getSlotType() == SlotType.INVENTORY) {
					MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Item purchased!");
					ItemGUI.getNew(p, gui.getSlotNum());
				}else if(gui.getSlotType() == SlotType.PERK) {
					MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Perk purchased!");
					PerkGUI.getNew(p, gui.getSlotNum());
				}
				
				if(gui.getSlotType() == SlotType.ABILITY) {
					AbilityGUI.getNew(p, 0, gui.getSlotNum());
				}else if(gui.getSlotType() == SlotType.INVENTORY) {
					ItemGUI.getNew(p, gui.getSlotNum());
				}else if(gui.getSlotType() == SlotType.PERK) {
					PerkGUI.getNew(p, gui.getSlotNum());
				}
				ScoreboardSidebar.update(p);
			}else { //Handle slot purchasing
				prof.subtractGold(gui.slotToPurchase().getCost(), false);
				MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Slot purchased!");
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.3f, 1);
				prof.unlockSlot(gui.getSlotType(), gui.getSlotNum());
				OverviewGUI.getNew(p);
				ScoreboardSidebar.update(p);
			}
		}else { //They clicked no
			if(gui.getType() == PurchaseType.ABILITY) { //They refused to purchase an ability
				if(gui.getSlotType() == SlotType.ABILITY) {
					AbilityGUI.getNew(p, 0, gui.getSlotNum());
				}else if(gui.getSlotType() == SlotType.INVENTORY) {
					ItemGUI.getNew(p, gui.getSlotNum());
				}else if(gui.getSlotType() == SlotType.PERK) {
					PerkGUI.getNew(p, gui.getSlotNum());
				}
			}else { //They refused to purchase a slot
				OverviewGUI.getNew(p); //Send them back to overview
			}
		}
	}

}
