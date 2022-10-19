package sylaires.ctf.cosmetic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AuraVoid implements ICosmetic {
	
	@Override
	public String getRequiredRank() {
		return "theta";
	}

	@Override
	public int getRequiredLevel() {
		return 0;
	}

	@Override
	public String getRequiredAchievement() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Void Aura";
	}

	@Override
	public ICosmetic getInstance() {
		return new AuraVoid();
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = new ItemBuilder(Material.BEDROCK, ChatColor.GOLD + "Void Aura")
				.addLore(ChatColor.BLUE + "Surround yourself in an aura of")
				.addLore(ChatColor.BLUE + "the void and leave darkness where")
				.addLore(ChatColor.BLUE + "you walk").toItem();
		if(unlocked(p)) {
			if(prof.getIsSelected(getId())) {
				stack = new ItemBuilder(stack).addLore(" ")
						.addLore(ChatColor.GREEN + "Selected")
						.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 0)
						.addFlag(ItemFlag.HIDE_ENCHANTS)
						.toItem();
			}else {
				stack = new ItemBuilder(stack).addLore(" ")
						.addLore(ChatColor.GREEN + "Click to select")
						.toItem();
			}
		}else {
			stack = new ItemBuilder(stack).addLore(" ")
					.addLore(ChatColor.RED + "You must own the " + ChatColor.DARK_AQUA + ChatColor.BOLD + "THETA" + ChatColor.RED + " rank or higher to use this")
					.addLore(ChatColor.YELLOW + "You can buy ranks at " + ChatColor.GOLD + "www.program-zeta.net/store")
					.toItem();
		}
		
		if(getRequiredLevel() != 0) {
			if(prof.getLevel() < getRequiredLevel()) {
				stack = new ItemBuilder(stack).addLore(" ")
				.addLore(ChatColor.RED + "You must be Level " + ChatColor.YELLOW + ChatColor.BOLD + getRequiredLevel() + ChatColor.RED + " or higher to use this")
				.toItem();
			}
		}
		
		return stack;
	}

	@Override
	public String getId() {
		return "aura_void";
	}
	
	@Override
	public boolean unlocked(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(getRequiredRank().equalsIgnoreCase("eta")) {
			if(prof.getDonorRank().equalsIgnoreCase("eta") || prof.getDonorRank().equalsIgnoreCase("theta") || prof.getDonorRank().equalsIgnoreCase("zeta") || prof.getDonorRank().equalsIgnoreCase("zetaplus")) {
				return true;
			}
		}else if(getRequiredRank().equalsIgnoreCase("theta")) {
			if(prof.getDonorRank().equalsIgnoreCase("theta") || prof.getDonorRank().equalsIgnoreCase("zeta") || prof.getDonorRank().equalsIgnoreCase("zetaplus")) {
				return true;
			}
		}else if(getRequiredRank().equalsIgnoreCase("zeta")) {
			if(prof.getDonorRank().equalsIgnoreCase("zeta") || prof.getDonorRank().equalsIgnoreCase("zetaplus")) {
				return true;
			}
		}else if(getRequiredRank().equalsIgnoreCase("zetaplus")) {
			if(prof.getDonorRank().equalsIgnoreCase("zetaplus")) {
				return true;
			}
		}
		if(getRequiredLevel() != 0) {
			if(prof.getLevel() >= getRequiredLevel()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDisplayedRequirement() {
		return ChatColor.RED + "You must own the " + ChatColor.DARK_AQUA + ChatColor.BOLD + "THETA" + ChatColor.RED + " rank or higher to use this";
	}

}
