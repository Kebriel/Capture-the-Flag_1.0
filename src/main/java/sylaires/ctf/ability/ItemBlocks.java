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

public class ItemBlocks implements IAbility {
	
	@Override
	public int getCost() {
		return 3000;
	}

	@Override
	public IAbility getInstance() {
		return new ItemBlocks();
	}
	
	@Override
	public String getName() {
		return "Ghost Blocks";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.GLASS, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Spawn with " + ChatColor.AQUA + "16" + ChatColor.YELLOW + " glass blocks")
				.addLore(ChatColor.YELLOW + "that cannot be broken but disappear after " + ChatColor.AQUA + "3s")
				.addLore(" ")
				.addLore(ChatColor.YELLOW + "You gain " + ChatColor.AQUA + "1" + ChatColor.YELLOW + " every five seconds")
				.addLore(ChatColor.YELLOW + "and " + ChatColor.AQUA + "5" + ChatColor.YELLOW + " per kill")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.setAmount(16)
				.toItem();
		if(prof.getIsUnlocked("item_blocks")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "item_blocks";
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
