package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.util.ItemBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonBack extends GUIButton {
	
	private ItemStack stack;
	private InventoryHolder holder;
	
	public GUIButtonBack(InventoryHolder holder) {
		super();
		this.holder = holder;
		stack = new ItemBuilder(Material.WORKBENCH, ChatColor.YELLOW + "Go Back")
				.addLore(ChatColor.RED + "Click to return to the previous menu").toItem();
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(holder instanceof AbilityGUI) {
			OverviewGUI.getNew(p);
		}else if(holder instanceof ItemGUI) {
			OverviewGUI.getNew(p);
		}else if(holder instanceof PerkGUI) {
			OverviewGUI.getNew(p);
		}else if(holder instanceof AuraGUI) {
			CosmeticGUI.getNew(p);
		}else if(holder instanceof ArrowTrailGUI) {
			CosmeticGUI.getNew(p);
		}else if(holder instanceof KillEffectGUI) {
			CosmeticGUI.getNew(p);
		}else if(holder instanceof KillMessageGUI) {
			CosmeticGUI.getNew(p);
		}
	}

}
