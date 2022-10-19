package sylaires.ctf.ability;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public interface IAbility {
	
	public int getCost(); public IAbility getInstance(); public ItemStack getIcon(); public ItemStack getIcon(Player p); public String getId(); public boolean isFree();
	public String getName();

}
