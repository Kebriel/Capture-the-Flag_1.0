package kebriel.ctf.cosmetic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;

public class AuraCrown implements ICosmetic {
	
	@Override
	public String getRequiredRank() {
		return "";
	}

	@Override
	public int getRequiredLevel() {
		return 500;
	}

	@Override
	public String getRequiredAchievement() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Crowned";
	}

	@Override
	public ICosmetic getInstance() {
		return new AuraCrown();
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = new ItemBuilder(Material.DIAMOND_HELMET, ChatColor.GOLD + "Crowned")
				.addLore(ChatColor.BLUE + "You are crowned in a mantle of")
				.addLore(ChatColor.BLUE + "fiery gold").toItem();
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
			if(getRequiredLevel() == 0) {
				stack = new ItemBuilder(stack).addLore(" ")
						.addLore(ChatColor.RED + "You must own the " + ChatColor.YELLOW + ChatColor.BOLD + "ETA" + ChatColor.RED + " rank or higher to use this")
						.addLore(ChatColor.YELLOW + "You can buy ranks at " + ChatColor.GOLD + "www.program-zeta.net/store")
						.toItem();
			}
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
		return "aura_crown";
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
		return ChatColor.RED + "You must be Level " + ChatColor.YELLOW + ChatColor.BOLD + getRequiredLevel() + ChatColor.RED + " or higher to use this";
	}

}
