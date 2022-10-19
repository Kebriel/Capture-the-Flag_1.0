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

public class ItemFishingRod implements IAbility {
	
	@Override
	public int getCost() {
		return 3500;
	}

	@Override
	public IAbility getInstance() {
		return new ItemFishingRod();
	}
	
	@Override
	public String getName() {
		return "Battle Rod";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.FISHING_ROD, ChatColor.GOLD + getName())
				.addLore(ChatColor.GREEN + "Is not affected by durability")
				.addLore(ChatColor.YELLOW + "Enemies struck with this rod will ")
				.addLore(ChatColor.YELLOW + "receive " + ChatColor.AQUA + "20%" + ChatColor.YELLOW + " extra damage from")
				.addLore(ChatColor.YELLOW + "your next melee hit. Does not stack.")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("item_rod")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "item_rod";
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
