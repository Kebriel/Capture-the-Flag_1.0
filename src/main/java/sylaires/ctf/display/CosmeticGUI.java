package sylaires.ctf.display;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.cosmetic.CosmeticType;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MenuBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class CosmeticGUI implements InventoryHolder {
	
	private Inventory inv;
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	
	private CosmeticGUI(Player p) {
		ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", (byte) 2).toItem();
		ItemStack glass1 = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", (byte) 4).toItem();
		this.inv = new MenuBuilder(ChatColor.LIGHT_PURPLE + "Cosmetics", 54, this).fillWith(glass).construct();
		
		inv.setItem(11, glass1); inv.setItem(12, glass1); inv.setItem(14, glass1); inv.setItem(15, glass1);
		inv.setItem(19, glass1); inv.setItem(20, glass1); inv.setItem(21, glass1); inv.setItem(22, glass1); 
		inv.setItem(23, glass1); inv.setItem(24, glass1); inv.setItem(25, glass1); inv.setItem(29, glass1);
		inv.setItem(30, glass1); inv.setItem(32, glass1); inv.setItem(33, glass1); inv.setItem(37, glass1);
		inv.setItem(38, glass1); inv.setItem(39, glass1); inv.setItem(40, glass1); inv.setItem(41, glass1);
		inv.setItem(42, glass1); inv.setItem(43, glass1);
		
		GUIButtonMenu auras = new GUIButtonMenu(CosmeticType.AURA, p); auras.setItem(10, inv); buttons.add(auras);
		GUIButtonMenu trails = new GUIButtonMenu(CosmeticType.TRAIL, p); trails.setItem(13, inv); buttons.add(trails);
		GUIButtonMenu kill = new GUIButtonMenu(CosmeticType.KILL, p); kill.setItem(16, inv); buttons.add(kill);
		GUIButtonMenu message = new GUIButtonMenu(CosmeticType.MESSAGE, p); message.setItem(28, inv); buttons.add(message);
		
		GUIButtonUpcoming upcoming1 = new GUIButtonUpcoming(this); upcoming1.setItem(31, inv); buttons.add(upcoming1);
		GUIButtonUpcoming upcoming2 = new GUIButtonUpcoming(this); upcoming2.setItem(34, inv); buttons.add(upcoming2);
		
		p.openInventory(inv);
	}

	public GUIButton getButtonBySlot(int slot) {
		for(GUIButton button : buttons) {
			if(button.getSlot() == slot) {
				return button;
			}
		}
		return null;
	}
	
	public static CosmeticGUI getNew(Player p) {
		return new CosmeticGUI(p);
	}
	
	@Override
	public Inventory getInventory() {
		return inv;
	}

}
