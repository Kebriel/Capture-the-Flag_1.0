package sylaires.ctf.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class MenuBuilder {
	
	Inventory menu;
	private int size;
	
	public MenuBuilder(String displayName, int size) {
		menu = Bukkit.createInventory(null, size, displayName);
		this.size = size;
	}

	public MenuBuilder(String displayName, int size, InventoryHolder holder) {
		menu = Bukkit.createInventory(holder, size, displayName);
		this.size = size;
	}
	
	public Inventory construct() {
		return menu;
	}
	
	public MenuBuilder fillWith(ItemStack contents) {
		for(int slot = 0; slot < size; slot++) {
			menu.setItem(slot, contents);
		}
		return this;
	}
	
	public MenuBuilder putItem(ItemStack item, int slot) {
		menu.setItem(slot, item);
		return this;
	}
	
	

}
