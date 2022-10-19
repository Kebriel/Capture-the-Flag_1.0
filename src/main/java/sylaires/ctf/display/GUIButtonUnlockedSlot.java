package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.ability.AbilityRegistry;
import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonUnlockedSlot extends GUIButton {

	ItemStack stack;
	SlotType type;
	private int num;
	
	public GUIButtonUnlockedSlot(GUISlot slot) {
		super();
		Player p = slot.getPlayer();
		PlayerProfile prof = ProfileManager.getProfile(p);
		type = slot.getType();
		num = slot.getSlot();
		setSlot(num);
		if(type == SlotType.ABILITY) {
			if(!prof.getSelected(num, SlotType.ABILITY).equalsIgnoreCase("none")) {
				stack = AbilityRegistry.getInstanceByID(prof.getSelected(num, type)).getIcon(p);
				stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.AQUA + "Click to select an ability").toItem();
			}else {
				stack = new ItemBuilder(Material.DIAMOND_BLOCK, ChatColor.GREEN + "Ability Slot " + slot.getSlot())
						.addLore(ChatColor.AQUA + "Click to select an ability")
						.toItem();
			}
		}else if(type == SlotType.INVENTORY) {
			if(!prof.getSelected(num, SlotType.INVENTORY).equalsIgnoreCase("none")) {
				stack = AbilityRegistry.getInstanceByID(prof.getSelected(num, type)).getIcon(p);
				stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.AQUA + "Click to select an extra item").toItem();
			}else {
				stack = new ItemBuilder(Material.GOLD_BLOCK, ChatColor.YELLOW + "Extra Inventory Slot")
						.addLore(ChatColor.AQUA + "Click to select an extra tool or")
						.addLore(ChatColor.AQUA + "consumable to start with each life")
						.toItem();
			}
		}else if(type == SlotType.PERK) {
			if(!prof.getSelected(num, SlotType.PERK).equalsIgnoreCase("none")) {
				stack = AbilityRegistry.getInstanceByID(prof.getSelected(num, type)).getIcon(p);
				stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.AQUA + "Click to select a special perk").toItem();
			}else {
				stack = new ItemBuilder(Material.REDSTONE_BLOCK, ChatColor.GOLD + "Perk Slot " + slot.getSlot())
						.addLore(ChatColor.AQUA + "Click to select a passive perk")
						.toItem();
			}
		}
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(type == SlotType.ABILITY) {
			AbilityGUI.getNew(p, 0, num);
		}else if(type == SlotType.INVENTORY) {
			ItemGUI.getNew(p, num);
		}else if(type == SlotType.PERK) {
			PerkGUI.getNew(p, num);
		}
	}

}