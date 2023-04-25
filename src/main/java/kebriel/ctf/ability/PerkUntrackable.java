package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PerkUntrackable implements IAbility {
	
	@Override
	public int getCost() {
		return 8000;
	}

	@Override
	public IAbility getInstance() {
		return new PerkUntrackable();
	}
	
	@Override
	public String getName() {
		return "Stealth";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.COAL, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You cannot be tracked while holding a flag")
				.addLore(ChatColor.YELLOW + "and players cannot see your nametag")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("perk_untrackable")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "perk_untrackable";
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
