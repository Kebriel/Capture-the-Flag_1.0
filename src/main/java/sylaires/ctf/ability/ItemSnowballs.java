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

public class ItemSnowballs implements IAbility {
	
	@Override
	public int getCost() {
		return 4000;
	}

	@Override
	public IAbility getInstance() {
		return new ItemSnowballs();
	}
	
	@Override
	public String getName() {
		return "Snowballs";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.SNOW_BALL, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Spawn with a stack of snowballs that")
				.addLore(ChatColor.YELLOW + "inflict " + ChatColor.GRAY + "Slowness I (3s)")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.setAmount(16)
				.toItem();
		if(prof.getIsUnlocked("item_snow")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "item_snow";
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
