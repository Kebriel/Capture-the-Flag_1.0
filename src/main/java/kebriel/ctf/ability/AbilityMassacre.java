package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class AbilityMassacre implements IAbility {
	
	@Override
	public int getCost() {
		return 2000;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityMassacre();
	}
	
	@Override
	public String getName() {
		return "Massacre";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.IRON_AXE, ChatColor.GOLD + getName())
		.addLore(ChatColor.YELLOW + "You deal " + ChatColor.AQUA + "5%" + ChatColor.YELLOW + " more damage per")
		.addLore(ChatColor.YELLOW + "opponent within " + ChatColor.AQUA + "8" + ChatColor.YELLOW + " blocks of you.")
		.addLore(" ")
		.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + "2000g")
		.addFlag(ItemFlag.HIDE_ATTRIBUTES)
		.toItem();
		if(prof.getIsUnlocked("ability_massacre")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_massacre";
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
