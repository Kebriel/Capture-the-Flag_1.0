package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityFirebrand implements IAbility {
	
	@Override
	public int getCost() {
		return 2500;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityFirebrand();
	}
	
	@Override
	public String getName() {
		return "Firebrand";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.BLAZE_POWDER, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Your sword is replaced by a wooden sword")
				.addLore(ChatColor.YELLOW + "that deals 1 less damage but inflicts")
				.addLore(ChatColor.YELLOW + "fire upon enemies with every hit")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_firebrand")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_firebrand";
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
