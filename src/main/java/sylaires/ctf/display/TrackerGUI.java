package sylaires.ctf.display;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import sylaires.ctf.util.MenuBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class TrackerGUI implements InventoryHolder {
	
	Inventory gui;
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	
	private TrackerGUI(Player p) { //Opening a page of it
		Inventory gui = new MenuBuilder("Tracker", 9, this).construct();
		
		GUIButtonTrackable blue = new GUIButtonTrackable(this, "blue", p);
		buttons.add(blue); blue.setItem(3, gui);
		
		GUIButtonTrackable red = new GUIButtonTrackable(this, "red", p);
		buttons.add(red); red.setItem(5, gui);
		
		p.openInventory(gui);
		this.gui = gui;
	}
	
	public GUIButton getButtonBySlot(int slot) {
		for(GUIButton button : buttons) {
			if(button.getSlot() == slot) {
				return button;
			}
		}
		return null;
	}
	
	public static TrackerGUI getNew(Player p) {
		return new TrackerGUI(p);
	}

	@Override
	public Inventory getInventory() {
		return gui;
	}
	
	

}
