package kebriel.ctf.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ICosmetic {
	
	public String getRequiredRank(); public int getRequiredLevel(); public String getRequiredAchievement(); public String getDisplayName();
	public ICosmetic getInstance(); public ItemStack getIcon(Player p); public String getId(); public boolean unlocked(Player p);
	public String getDisplayedRequirement();

}
