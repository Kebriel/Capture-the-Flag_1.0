package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityGladiator implements IAbility {
	
	@Override
	public int getCost() {
		return 2000;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityGladiator();
	}
	
	@Override
	public String getName() {
		return "Gladiator";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.DIAMOND_CHESTPLATE, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You recieve " + ChatColor.AQUA + "5%" + ChatColor.YELLOW + " less damage per")
				.addLore(ChatColor.YELLOW + "opponent within " + ChatColor.AQUA + "8" + ChatColor.YELLOW + " blocks of you.")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + "2000g")
				.toItem();
		if(prof.getIsUnlocked("ability_gladiator")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_gladiator";
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
