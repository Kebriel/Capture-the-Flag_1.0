package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemPearl implements IAbility {
	
	@Override
	public int getCost() {
		return 4000;
	}

	@Override
	public IAbility getInstance() {
		return new ItemSnowballs();
	}
	
	@Override
	public String getName() {
		return "Unstable Pearl";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.ENDER_PEARL, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Spawn with an ender pearl that will")
				.addLore(ChatColor.YELLOW + "inflict half your health worth of damage")
				.addLore(ChatColor.YELLOW + "upon landing.")
				.addLore(" ")
				.addLore(ChatColor.YELLOW + "Cannot be thrown if you are holding a flag")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("item_pearl")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "item_pearl";
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
