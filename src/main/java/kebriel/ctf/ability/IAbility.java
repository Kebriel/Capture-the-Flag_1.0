package kebriel.ctf.ability;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IAbility {
	
	public int getCost(); public IAbility getInstance(); public ItemStack getIcon(); public ItemStack getIcon(Player p); public String getId(); public boolean isFree();
	public String getName();

}
