package kebriel.ctf.ability;

import kebriel.ctf.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class AbilityParry implements IAbility {
	
	@Override
	public int getCost() {
		return 0;
	}

	@Override
	public IAbility getInstance() {
		return new AbilityParry();
	}
	
	@Override
	public String getName() {
		return "Parry";
	}

	@Override
	public ItemStack getIcon() {
		ItemStack stack = new ItemBuilder(Material.IRON_SWORD, ChatColor.GOLD + getName())
				.addLore(ChatColor.YELLOW + "Blocking will reduce incoming damage by " + ChatColor.AQUA + "30%")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.addFlag(ItemFlag.HIDE_ATTRIBUTES)
				.toItem();
		return stack;
	}

	@Override
	public String getId() {
		return "ability_parry";
	}

	@Override
	public boolean isFree() {
		return true;
	}

	@Override
	public ItemStack getIcon(Player p) {
		ItemStack stack = new ItemBuilder(Material.IRON_SWORD, ChatColor.GOLD + "Parry")
				.addLore(ChatColor.YELLOW + "Blocking will reduce incoming damage by " + ChatColor.AQUA + "30%")
				.addLore(" ")
				.addLore(ChatColor.GREEN + "Click to select")
				.addFlag(ItemFlag.HIDE_ATTRIBUTES)
				.toItem();
		return stack;
	}

}
