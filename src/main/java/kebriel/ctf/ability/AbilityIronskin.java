package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityIronskin implements IAbility {
	
	@Override
	public int getCost() {
		return 2500;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityIronskin();
	}
	
	@Override
	public String getName() {
		return "Ironskin";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.ANVIL, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "You have permanent " + ChatColor.AQUA + "Resistance I" + ChatColor.YELLOW + " and")
				.addLore(ChatColor.AQUA + "Slowness I")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_ironskin")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_ironskin";
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
