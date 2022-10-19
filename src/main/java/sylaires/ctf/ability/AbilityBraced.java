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

public class AbilityBraced implements IAbility {
	
	@Override
	public int getCost() {
		return 2000;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityBraced();
	}
	
	@Override
	public String getName() {
		return "Braced";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.FEATHER, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You receive " + ChatColor.AQUA + "50%" + ChatColor.YELLOW + " less fall damage")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_braced")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_braced";
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
