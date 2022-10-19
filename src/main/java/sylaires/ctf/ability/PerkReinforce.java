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

public class PerkReinforce implements IAbility {
	
	@Override
	public int getCost() {
		return 10000;
	}

	@Override
	public IAbility getInstance() {
		return new PerkReinforce();
	}
	
	@Override
	public String getName() {
		return "Call Reinforcements";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.IRON_DOOR, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Whenever you fall below half health ")
				.addLore(ChatColor.YELLOW + "while holding a flag, up to two teammates")
				.addLore(ChatColor.YELLOW + "are given the opportunity to instantly")
				.addLore(ChatColor.YELLOW + "teleport to you. When a player teleports")
				.addLore(ChatColor.YELLOW + "you receive " + ChatColor.AQUA + "Speed II (10s)" + ChatColor.YELLOW + " and")
				.addLore(ChatColor.RED + "Regeneration II (10s)")
				.addLore(" ")
				.addLore(ChatColor.GOLD + "This has a " + ChatColor.WHITE + "20s" + ChatColor.GOLD + " cooldown")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("perk_reinforce")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "perk_reinforce";
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
