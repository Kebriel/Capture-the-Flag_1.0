package sylaires.ctf.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityKevlar implements IAbility {
	
	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityKevlar();
	}
	
	@Override
	public String getName() {
		return "Kevlar";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack stack = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Your chestplate is replaced by a chainmail chestplate")
				.addLore(ChatColor.YELLOW + "You receive " + ChatColor.AQUA + "45%" + ChatColor.YELLOW + " less damage from")
				.addLore(ChatColor.YELLOW + "ranged attacks")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

	@Override
	public String getId() {
		return "ability_kevlar";
	}

	@Override
	public boolean isFree() {
		return true;
	}

	@Override
	public ItemStack getIcon(Player p) {
		ItemStack stack = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE, ChatColor.GOLD + "Kevlar")
				.addLore(ChatColor.YELLOW + "Your chestplate is replaced by a chainmail chestplate")
				.addLore(ChatColor.YELLOW + "You receive " + ChatColor.AQUA + "45%" + ChatColor.YELLOW + " less damage from")
				.addLore(ChatColor.YELLOW + "ranged attacks")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

}
