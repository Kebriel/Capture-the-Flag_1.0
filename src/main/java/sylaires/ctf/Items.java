package sylaires.ctf;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import sylaires.ctf.game.TeamHandler;
import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class Items {
	
	/*
	 * TODO Replace all public static ItemStack fields with appropriate getters that generate and return a new 
	 * identical item
	 */
	
	/*
	 * TODO #2 Replace this class with ItemManager class, loaded locally for each PlayerProfile object, that will handle 
	 * all items non-statically for players
	 */
	
	//Single public fields to access a universal item -- prevents item copying
	public static ItemStack statsItem;
	public static ItemStack abilitiesItem;
	public static ItemStack blueQueue;
	public static ItemStack clearQueue;
	public static ItemStack redQueue;
	public static ItemStack cosmetics;
	public static ItemStack settings;
	
	/*
	 * Convenient static method to give a specific player all lobby items
	 */
	public static void lobbyItems(Player p) {
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setArmorContents(null);
		p.setMaxHealth(20.0);
		p.setHealth(p.getMaxHealth());
		
		statsItem = new ItemBuilder(Material.SKULL_ITEM, "" + ChatColor.GREEN + ChatColor.ITALIC + "/stats", (byte) 3)
				.getSkull(p)
				.addLore(ChatColor.AQUA + "Show your game stats!")
				.toItem();
		inv.setItem(0, statsItem);
		
		abilitiesItem = new ItemBuilder(Material.BEACON, ChatColor.GOLD + "Abilities & Perks")
				.addLore(ChatColor.AQUA + "Select abilities and customize")
				.addLore(ChatColor.AQUA + "your loadout!")
				.toItem();
		inv.setItem(1, abilitiesItem);
		
		blueQueue = new ItemBuilder(Material.WOOL, ChatColor.BLUE + "Blue Team", (byte) 11)
				.addLore(ChatColor.AQUA + "Enter the Queue for the " + ChatColor.BLUE + "Blue Team")
				.toItem();
		inv.setItem(3, blueQueue);
		
		clearQueue = new ItemBuilder(Material.WOOL, ChatColor.YELLOW + "Clear Queue", (byte) 0)
				.addLore(ChatColor.AQUA + "Clear your Queue from a team")
				.toItem();
		inv.setItem(4, clearQueue);
		
		redQueue = new ItemBuilder(Material.WOOL, ChatColor.RED + "Red Team", (byte) 14)
				.addLore(ChatColor.AQUA + "Enter the Queue for the " + ChatColor.RED + "Red Team")
				.toItem();
		inv.setItem(5, redQueue);
		
		cosmetics = new ItemBuilder(Material.EMERALD, ChatColor.LIGHT_PURPLE + "Cosmetics")
				.addLore(ChatColor.AQUA + "Browse game and lobby cosmetics!")
				.toItem();
		inv.setItem(7, cosmetics);
		
		settings = new ItemBuilder(Material.DIODE, ChatColor.ITALIC + "/settings")
				.addLore(ChatColor.AQUA + "Configure various settings to your liking")
				.toItem();
		inv.setItem(8, settings);
	}
	
	/*
	 * Bloated method that will determine the items you receive based on selections -- TODO Replace!
	 */
	public static void gameItems(Player p) {
		PlayerInventory inv = p.getInventory();
		PlayerProfile prof = ProfileManager.getProfile(p);
		inv.clear();
		p.setMaxHealth(20.0);

		//Adjust health if relevant perk is selected
		if(prof.getIsSelected("ability_lifeblood")) p.setMaxHealth(24.0);
		p.setHealth(p.getMaxHealth());
		
		ItemStack sword = null;

		//Handle player's sword, either special sword from perk for regular stone sword
		if(prof.getIsSelected("ability_firebrand")) {
			sword = new ItemBuilder(Material.WOOD_SWORD, ChatColor.GOLD + "Firebrand").setUnreakable().addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.toItem();
		}else {
			sword =  new ItemBuilder(Material.STONE_SWORD).setUnreakable().addFlag(ItemFlag.HIDE_UNBREAKABLE).toItem();
		}
		inv.setItem(0, sword);
		
		ItemStack bow = new ItemBuilder(Material.BOW).setUnreakable().addFlag(ItemFlag.HIDE_UNBREAKABLE).toItem();
		inv.setItem(1, bow);
		
		ItemStack arrows = null;
		if(prof.getIsSelected("ability_quiver")) { //Handle arrows, receive more if using quiver perk
			arrows = new ItemBuilder(Material.ARROW).setAmount(10).toItem();
		}else {
			arrows = new ItemBuilder(Material.ARROW).setAmount(5).toItem();
		}
		inv.setItem(7, arrows);

		//Give and set up compass tracker
		if(prof.getCompassTracker() != null) {
			prof.getCompassTracker().setItem();
		}
		
		ItemStack helm = null;
		ItemStack leg = null;
		ItemStack boot = null;
		
		ItemStack chest = null;
		if(prof.getIsSelected("ability_kevlar")) { //Determine whether or not to give player kevlar or regular chestplate
			chest = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, ChatColor.GOLD + "Kevlar Mail")
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.addLore(ChatColor.YELLOW + "You receive " + ChatColor.AQUA + "45%" + ChatColor.YELLOW + " less damage from")
					.addLore(ChatColor.YELLOW + "ranged attacks")
					.toItem(); 
		}else {
			chest = new ItemBuilder(Material.IRON_CHESTPLATE).setUnreakable().addFlag(ItemFlag.HIDE_UNBREAKABLE).toItem();
		}

		//Adds protection on top of chestplate, as juggernaut is stackable with kevlar
		if(prof.getIsSelected("ability_juggernaut")) {
			chest = new ItemBuilder(chest).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1).toItem();
		}
		inv.setChestplate(chest);
		
		//Extra items from abilities
		if(prof.getIsSelected("item_rod")) {
			ItemStack rod = new ItemBuilder(Material.FISHING_ROD, ChatColor.GOLD + "Battle Rod").setUnreakable().addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.toItem();
			inv.addItem(rod);
		}
		if(prof.getIsSelected("item_snow")) {
			ItemStack snow = new ItemBuilder(Material.SNOW_BALL, ChatColor.GOLD + "Snowball").setAmount(16).toItem();
			inv.addItem(snow);
		}
		if(prof.getIsSelected("item_gapple")) {
			ItemStack gapple = new ItemBuilder(Material.GOLDEN_APPLE, ChatColor.GOLD + "Golden Apple").toItem();
			inv.addItem(gapple);
		}
		if(prof.getIsSelected("item_stick")) {
			ItemStack stick = new ItemBuilder(Material.STICK, ChatColor.GOLD + "Knockback Stick").addEnchantment(Enchantment.KNOCKBACK, 2)
					.toItem();
			inv.addItem(stick);
		}
		if(prof.getIsSelected("item_bandage")) {
			ItemStack bandage = new ItemBuilder(Material.PAPER, ChatColor.GOLD + "Medical Bandage").toItem();
			inv.addItem(bandage);
		}
		if(prof.getIsSelected("item_pearl")) {
			ItemStack pearl = new ItemBuilder(Material.ENDER_PEARL, ChatColor.GOLD + "Unstable Pearl").toItem();
			inv.addItem(pearl);
		}
		if(prof.getIsSelected("item_blocks")) {
			ItemStack block = new ItemBuilder(Material.GLASS, ChatColor.GOLD + "Ghost Block").setAmount(16).toItem();
			inv.addItem(block);
		}
		
		//Perks
		if(prof.getIsSelected("perk_haste")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600, 0, true, false), true);
		}

		//Handle armor color according to team
		if(TeamHandler.redTeam.contains(p.getUniqueId())) {
			helm = new ItemBuilder(Material.LEATHER_HELMET)
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.dyeLeather(Color.RED)
					.toItem();
			leg = new ItemBuilder(Material.LEATHER_LEGGINGS)
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.dyeLeather(Color.RED)
					.toItem();
			boot = new ItemBuilder(Material.LEATHER_BOOTS)
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.dyeLeather(Color.RED)
					.toItem();
		}else if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
			helm = new ItemBuilder(Material.LEATHER_HELMET)
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.dyeLeather(Color.BLUE)
					.toItem();
			leg = new ItemBuilder(Material.LEATHER_LEGGINGS)
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.dyeLeather(Color.BLUE)
					.toItem();
			boot = new ItemBuilder(Material.LEATHER_BOOTS)
					.setUnreakable()
					.addFlag(ItemFlag.HIDE_UNBREAKABLE)
					.dyeLeather(Color.BLUE)
					.toItem();
		}else {
			return;
		}

		//Change helmet if royalty perk is selected
		if(prof.getIsSelected("perk_royalty")) {
			helm = new ItemBuilder(Material.GOLD_HELMET, ChatColor.GOLD + "Royal Mantle")
					.setUnreakable().addFlag(ItemFlag.HIDE_UNBREAKABLE).toItem();
		}

		inv.setHelmet(helm);
		inv.setLeggings(leg);
		inv.setBoots(boot);
	}

}
