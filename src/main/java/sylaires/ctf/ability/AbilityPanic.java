package sylaires.ctf.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityPanic implements IAbility {

	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityPanic();
	}
	
	@Override
	public String getName() {
		return "Panic";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack stack = new ItemBuilder(Material.DISPENSER, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Receive " + ChatColor.AQUA + "Speed I (5s)" + ChatColor.YELLOW + " when you")
				.addLore(ChatColor.YELLOW + "fall below " + ChatColor.RED + "3❤")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

	@Override
	public String getId() {
		return "ability_panic";
	}

	@Override
	public boolean isFree() {
		return true;
	}

	@Override
	public ItemStack getIcon(Player p) {
		ItemStack stack = new ItemBuilder(Material.DISPENSER, ChatColor.GOLD + "Panic")
				.addLore(ChatColor.YELLOW + "Receive " + ChatColor.AQUA + "Speed I (5s)" + ChatColor.YELLOW + " when you")
				.addLore(ChatColor.YELLOW + "fall below " + ChatColor.RED + "3❤")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

}
