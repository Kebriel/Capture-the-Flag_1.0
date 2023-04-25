package kebriel.ctf.ability;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemBandage implements IAbility {
	
	@Override
	public int getCost() {
		return 3500;
	}

	@Override
	public IAbility getInstance() {
		return new ItemBandage();
	}
	
	@Override
	public String getName() {
		return "Medical Bandage";
	}

	@Override
	public ItemStack getIcon(Player p) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		ItemStack stack = null;
		stack = new ItemBuilder(Material.PAPER, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Spawn with a bandage that will heal")
				.addLore(ChatColor.RED + "1❤ " + ChatColor.YELLOW + "and give " + ChatColor.GRAY + "Resistance I (5s)")
				.addLore(ChatColor.YELLOW + "and " + ChatColor.RED + "Regeneration I (5s)" + ChatColor.YELLOW + " upon consumption")
				.addLore(" ")
				.addLore(ChatColor.RED + "Click to purchase for " + ChatColor.GOLD + getCost() + "g")
				.toItem();
		if(prof.getIsUnlocked("item_bandage")) {
			stack = new ItemBuilder(stack).setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Click to select").toItem();
		}
		return stack;
	}

	@Override
	public String getId() {
		return "item_bandage";
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
