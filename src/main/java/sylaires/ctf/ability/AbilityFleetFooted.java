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

public class AbilityFleetFooted implements IAbility {
	
	@Override
	public int getCost() {
		return 2500;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityFleetFooted();
	}
	
	@Override
	public String getName() {
		return "Fleet-Footed";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.RABBIT_FOOT, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You gain " + ChatColor.AQUA + "Speed I (10s)" + ChatColor.YELLOW + " when you pickup a flag")
				.addLore(ChatColor.YELLOW + "You gain " + ChatColor.GREEN + "Jump I (10s)" + ChatColor.YELLOW + " as well if it's your flag")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_fleet")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_fleet";
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
