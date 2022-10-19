package sylaires.ctf.cosmetic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public interface ICosmetic {
	
	public String getRequiredRank(); public int getRequiredLevel(); public String getRequiredAchievement(); public String getDisplayName();
	public ICosmetic getInstance(); public ItemStack getIcon(Player p); public String getId(); public boolean unlocked(Player p);
	public String getDisplayedRequirement();

}
