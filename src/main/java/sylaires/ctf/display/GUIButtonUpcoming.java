package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonUpcoming extends GUIButton {
	
	private ItemStack stack;
	
	public GUIButtonUpcoming(InventoryHolder holder) {
		super();
		stack = new ItemBuilder(Material.BARRIER, ChatColor.RED + "Coming soon...")
				.addLore(ChatColor.AQUA + "We're currently working on this content!").toItem();
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		MessageUtil.sendToPlayer(p, ChatColor.RED + "This is not yet implemented! Please check out our " + ChatColor.GOLD + "Forums" + ChatColor.RED + " to view upcoming features.");
	}

}
