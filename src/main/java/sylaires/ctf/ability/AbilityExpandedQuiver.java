package sylaires.ctf.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityExpandedQuiver implements IAbility {
	
	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityExpandedQuiver();
	}
	
	@Override
	public String getName() {
		return "Expanded Quiver";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack stack = new ItemBuilder(Material.ARROW, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Start with " + ChatColor.AQUA + "5" + ChatColor.YELLOW + " extra arrows")
				.addLore(ChatColor.YELLOW + "each life")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

	@Override
	public String getId() {
		return "ability_quiver";
	}

	@Override
	public boolean isFree() {
		return true;
	}

	@Override
	public ItemStack getIcon(Player p) {
		ItemStack stack = new ItemBuilder(Material.ARROW, ChatColor.GOLD + "Expanded Quiver")
				.addLore(ChatColor.YELLOW + "Start with " + ChatColor.AQUA + "5" + ChatColor.YELLOW + " extra arrows")
				.addLore(ChatColor.YELLOW + "each life")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

}
