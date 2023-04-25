package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityJuggernaut implements IAbility {
	
	@Override
	public int getCost() {
		return 3500;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityJuggernaut();
	}
	
	@Override
	public String getName() {
		return "Juggernaut";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.DIAMOND, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Spawn with " + ChatColor.AQUA + "Protection I" + ChatColor.YELLOW + " on your chestplate")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_juggernaut")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_juggernaut";
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
