package sylaires.ctf.display;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.game.TeamHandler;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MenuBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class OverviewGUI implements InventoryHolder {

	private Inventory inv;
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	
	@SuppressWarnings("deprecation")
	private OverviewGUI(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack glass = null;
		if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
			glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 11);
		}else if(TeamHandler.redTeam.contains(p.getUniqueId())) {
			glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
		}else {
			glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
		}
		glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", glass.getData().getData()).toItem();
		this.inv = new MenuBuilder("Overview", 54, this).fillWith(glass).construct();
		ItemStack glass1 = new ItemBuilder(Material.STAINED_GLASS_PANE, " ").toItem();
		inv.setItem(11, glass1); inv.setItem(12, glass1);
		inv.setItem(14, glass1); inv.setItem(15, glass1);
		inv.setItem(16, glass1); inv.setItem(20, glass1);
		inv.setItem(24, glass1); inv.setItem(29, glass1);
		inv.setItem(33, glass1); inv.setItem(38, glass1);
		inv.setItem(39, glass1); inv.setItem(41, glass1);
		inv.setItem(42, glass1); inv.setItem(43, glass1);
		
		GUISlot ab1 = new GUISlot(SlotType.ABILITY, 1, true, p);
		GUIButton button1 = ab1.getButton(); button1.setItem(10, inv);
		
		GUISlot ab2 = new GUISlot(SlotType.ABILITY, 2, true, p);
		GUIButton button2 = ab2.getButton(); button2.setItem(19, inv);
		
		GUISlot ab3 = new GUISlot(SlotType.ABILITY, 3, prof.getIsSlotUnlocked(3, SlotType.ABILITY), p);
		GUIButton button3 = ab3.getButton(); button3.setItem(28, inv);
		
		GUISlot ab4 = new GUISlot(SlotType.ABILITY, 4, prof.getIsSlotUnlocked(4, SlotType.ABILITY), p);
		GUIButton button4 = ab4.getButton(); button4.setItem(37, inv);
		
		GUISlot invslot = new GUISlot(SlotType.INVENTORY, 1, prof.getIsSlotUnlocked(1, SlotType.INVENTORY), p);
		GUIButton button5 = invslot.getButton(); button5.setItem(30, inv);
		
		GUISlot perk1 = new GUISlot(SlotType.PERK, 1, prof.getIsSlotUnlocked(1, SlotType.PERK), p);
		GUIButton button6 = perk1.getButton(); button6.setItem(25, inv);
		
		GUISlot perk2 = new GUISlot(SlotType.PERK, 2, prof.getIsSlotUnlocked(2, SlotType.PERK), p);
		GUIButton button7 = perk2.getButton(); button7.setItem(34, inv);
		
		buttons.add(button1); buttons.add(button2); buttons.add(button3); buttons.add(button4);
		buttons.add(button5); buttons.add(button6); buttons.add(button7);
		
		ItemStack sword = null;
		if(prof.getIsSelected("ability_firebrand")) {
			sword = new ItemBuilder(Material.WOOD_SWORD, ChatColor.GOLD + "Firebrand")
					.addLore(ChatColor.YELLOW + "Inflicts fire upon enemies")
					.addLore(" ")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.toItem();
		}else {
			sword = new ItemBuilder(Material.STONE_SWORD, ChatColor.WHITE + "Basic Stone Sword")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.toItem();
		}
		inv.setItem(21, sword);
				
		ItemStack bow = new ItemBuilder(Material.BOW, ChatColor.WHITE + "Basic Bow")
				.addLore(ChatColor.GREEN + "Is not affected by durability")
				.toItem(); inv.setItem(23, bow);
		ItemStack arrows = null;
		if(prof.getIsSelected("ability_quiver")) {
			arrows = new ItemBuilder(Material.ARROW, ChatColor.WHITE + "Basic Arrows")
					.setAmount(10)
					.addLore(ChatColor.GREEN + "Cannot be retrieved after")
					.addLore(ChatColor.GREEN + "being shot")
					.addLore("  ")
					.addLore(ChatColor.AQUA + "Extra arrows from " + ChatColor.GOLD + "Expanded Quiver")
					.toItem(); inv.setItem(32, arrows);
		}else {
			arrows = new ItemBuilder(Material.ARROW, ChatColor.WHITE + "Basic Arrows")
			.setAmount(5)
			.addLore(ChatColor.GREEN + "Cannot be retrieved after")
			.addLore(ChatColor.GREEN + "being shot")
			.toItem(); inv.setItem(32, arrows);
		}
		ItemStack chest = null;
		if(prof.getIsSelected("ability_kevlar")) {
			chest = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, ChatColor.GOLD + "Kevlar Mail")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.addLore(" ")
					.addLore(ChatColor.YELLOW + "You receive " + ChatColor.AQUA + "45%" + ChatColor.YELLOW + " less damage from")
					.addLore(ChatColor.YELLOW + "ranged attacks")
					.toItem(); 
		}else {
			chest = new ItemBuilder(Material.IRON_CHESTPLATE, ChatColor.WHITE + "Iron Chestplate")
			.addLore(ChatColor.GREEN + "Is not affected by durability")
			.toItem(); 
		}
		if(prof.getIsSelected("ability_juggernaut")) {
			chest = new ItemBuilder(chest).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItem();
		}
		inv.setItem(22, chest);
		
		ItemStack helm = null;
		ItemStack legs = null;
		ItemStack boots = null;
		
		if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
			helm = new ItemBuilder(Material.LEATHER_HELMET, ChatColor.BLUE + "Leather Helmet")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.dyeLeather(Color.BLUE)
					.toItem();
			legs = new ItemBuilder(Material.LEATHER_LEGGINGS, ChatColor.BLUE + "Leather Leggings")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.dyeLeather(Color.BLUE)
					.toItem();
			boots = new ItemBuilder(Material.LEATHER_BOOTS, ChatColor.BLUE + "Leather Boots")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.dyeLeather(Color.BLUE)
					.toItem();
		}else if(TeamHandler.redTeam.contains(p.getUniqueId())) {
			helm = new ItemBuilder(Material.LEATHER_HELMET, ChatColor.RED + "Leather Helmet")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.dyeLeather(Color.RED)
					.toItem();
			legs = new ItemBuilder(Material.LEATHER_LEGGINGS, ChatColor.RED + "Leather Leggings")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.dyeLeather(Color.RED)
					.toItem();
			boots = new ItemBuilder(Material.LEATHER_BOOTS, ChatColor.RED + "Leather Boots")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.dyeLeather(Color.RED)
					.toItem();
		}else {
			helm = new ItemBuilder(Material.LEATHER_HELMET, ChatColor.WHITE + "Leather Helmet")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.toItem();
			legs = new ItemBuilder(Material.LEATHER_LEGGINGS, ChatColor.WHITE + "Leather Leggings")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.toItem();
			boots = new ItemBuilder(Material.LEATHER_BOOTS, ChatColor.WHITE + "Leather Boots")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.toItem();
		}
		
		if(prof.getIsSelected("perk_royalty")) {
			helm = new ItemBuilder(Material.GOLD_HELMET, ChatColor.GOLD + "Royal Mantle")
					.addLore(ChatColor.GREEN + "Is not affected by durability")
					.toItem();
		}
		inv.setItem(13, helm);
		inv.setItem(31, legs);
		inv.setItem(40, boots);
		
		p.openInventory(inv);
	}
	
	public GUIButton getButtonBySlot(int slot) {
		for(GUIButton button : buttons) {
			if(button.getSlot() == slot) {
				return button;
			}
		}
		return null;
	}
	
	public static OverviewGUI getNew(Player p) {
		return new OverviewGUI(p);
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	

}
