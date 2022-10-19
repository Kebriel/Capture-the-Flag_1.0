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

public class AbilityLeech implements IAbility {
	
	@Override
	public int getCost() {
		return 3500;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityLeech();
	}
	
	@Override
	public String getName() {
		return "Leech";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.SPIDER_EYE, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Your attacks have a " + ChatColor.AQUA + "20%" + ChatColor.YELLOW + " chance")
				.addLore(ChatColor.YELLOW + "to heal you " + ChatColor.RED + "1❤")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_leech")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_leech";
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
