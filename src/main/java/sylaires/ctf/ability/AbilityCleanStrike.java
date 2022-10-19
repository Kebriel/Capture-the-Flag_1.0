package sylaires.ctf.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityCleanStrike implements IAbility {
	
	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityCleanStrike();
	}
	
	@Override
	public String getName() {
		return "Clean Strike";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack stack = new ItemBuilder(Material.QUARTZ, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Your strikes deal " + ChatColor.AQUA + "30%" + ChatColor.YELLOW + " more damage")
				.addLore(ChatColor.YELLOW + "to players with full health")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

	@Override
	public String getId() {
		return "ability_clean";
	}

	@Override
	public boolean isFree() {
		return true;
	}

	@Override
	public ItemStack getIcon(Player p) {
		ItemStack stack = new ItemBuilder(Material.QUARTZ, ChatColor.GOLD + "Clean Strike")
				.addLore(ChatColor.YELLOW + "Your strikes deal " + ChatColor.AQUA + "30%" + ChatColor.YELLOW + " more damage")
				.addLore(ChatColor.YELLOW + "to players with full health")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

}
