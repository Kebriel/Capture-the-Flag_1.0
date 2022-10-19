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

public class AbilityBastion implements IAbility {
	
	@Override
	public int getCost() {
		return 3000;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityBastion();
	}
	
	@Override
	public String getName() {
		return "Bastion";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.IRON_BLOCK, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You recieve " + ChatColor.AQUA + "100%" + ChatColor.YELLOW + " less knockback")
				.addLore(ChatColor.YELLOW + "from attacks")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_bastion")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_bastion";
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
