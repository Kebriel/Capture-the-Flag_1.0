package sylaires.ctf.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityAntidote implements IAbility {
	
	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityAntidote();
	}
	
	@Override
	public String getName() {
		return "Antidote";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack stack = new ItemBuilder(Material.FERMENTED_SPIDER_EYE, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You are immune to player-inflicted debuffs")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

	@Override
	public String getId() {
		return "ability_antidote";
	}

	@Override
	public boolean isFree() {
		return true;
	}

	@Override
	public ItemStack getIcon(Player p) {
		ItemStack stack = new ItemBuilder(Material.FERMENTED_SPIDER_EYE, ChatColor.GOLD + "Antidote")
				.addLore(ChatColor.YELLOW + "You are immune to player-inflicted debuffs")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.toItem();
		return stack;
	}

}
