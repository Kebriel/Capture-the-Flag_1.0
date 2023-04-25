package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AbilityPointBlank implements IAbility {
	
	@Override
	public int getCost() {
		return 3000;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityPointBlank();
	}
	
	@Override
	public String getName() {
		return "Point Blank";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.STAINED_CLAY, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Your arrows deal " + ChatColor.AQUA + "30%" + ChatColor.YELLOW + " more knockback")
				.addLore(ChatColor.YELLOW + "to players within " + ChatColor.AQUA + "8" + ChatColor.YELLOW + " blocks of you")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("ability_blank")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "ability_blank";
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
