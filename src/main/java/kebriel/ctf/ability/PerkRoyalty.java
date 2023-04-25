package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PerkRoyalty implements IAbility {
	
	@Override
	public int getCost() {
		return 8000;
	}

	@Override
	public IAbility getInstance() {
		return new PerkRoyalty();
	}
	
	@Override
	public String getName() {
		return "Royalty";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.GOLD_HELMET, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You and all players on your team receive")
				.addLore(ChatColor.AQUA + "+1 XP" + ChatColor.YELLOW + " and " + ChatColor.GOLD + "+1g" + ChatColor.YELLOW + " from kills and assists,")
				.addLore(ChatColor.YELLOW + "and " + ChatColor.AQUA + "+10 XP" + ChatColor.YELLOW + " and " + ChatColor.GOLD + "+10g" + ChatColor.YELLOW + " from all other sources")
				.addLore(" ")
				.addLore(ChatColor.YELLOW + "You will wear a gold helmet instead of leather")
				.addLore("  ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("perk_royalty")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "perk_royalty";
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
