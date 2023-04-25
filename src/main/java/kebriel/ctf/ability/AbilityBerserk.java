package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class AbilityBerserk implements IAbility {
	
	@Override
	public int getCost() {
		return 2500;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityBerserk();
	}
	
	@Override
	public String getName() {
		return "Berserk";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack;
		stack = new ItemBuilder(Material.DIAMOND_SWORD, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Gain a " + ChatColor.AQUA + "50%" + ChatColor.YELLOW + " damage boost for")
				.addLore(ChatColor.AQUA + "3s " + ChatColor.YELLOW + "after getting a kill. Does not stack")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.addFlag(ItemFlag.HIDE_ATTRIBUTES)
				.toItem();
		if(prof.getIsUnlocked("ability_berserk")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_berserk";
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
