package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonNextPage extends GUIButton {
	
	private InventoryHolder gui;
	private ItemStack stack;
	
	public GUIButtonNextPage(InventoryHolder gui) {
		this.gui = gui;
		
		stack = new ItemBuilder(Material.ARROW, ChatColor.YELLOW + "Next Page")
				.addLore(ChatColor.WHITE + "Click to go to the next page")
				.toItem();
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		if(gui instanceof AbilityGUI) {
			AbilityGUI agui = (AbilityGUI) gui;
			if(e.getClick() == ClickType.LEFT) {
				agui.openPage(agui.getCurrentPage()+1);
			}
		}
	}

}
