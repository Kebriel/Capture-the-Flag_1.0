package sylaires.ctf.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityFarshot implements IAbility {
	
	@Override
	public int getCost() {
		return 2000;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityFarshot();
	}
	
	@Override
	public String getName() {
		return "Far Shot";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.EYE_OF_ENDER, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Your arrows deal " + ChatColor.AQUA + "25%" + ChatColor.YELLOW + " more damage")
				.addLore(ChatColor.YELLOW + "per block that they fly past the " + ChatColor.AQUA + "15th")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_farshot")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_farshot";
	}

	@Override
	public boolean isFree() {
		return false;
	}

	@Override
	public ItemStack getIcon() {
		return null;
	}

}
