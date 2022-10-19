package sylaires.ctf.display;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.ability.AbilityRegistry;
import sylaires.ctf.ability.AbilityType;
import sylaires.ctf.ability.IAbility;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MenuBuilder;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityGUI implements InventoryHolder {
	
	Inventory gui;
	private Player p;
	private int slotnum;
	private int page;
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	private ArrayList<Class<? extends IAbility>> remaining = new ArrayList<Class<? extends IAbility>>();
	
	@SuppressWarnings("deprecation")
	private AbilityGUI(Player p, int page, int slotnum) throws InstantiationException, IllegalAccessException {
		this.p = p;
		this.slotnum = slotnum;
		this.page = page;
		ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", (byte) 3).toItem();
		Inventory gui = new MenuBuilder("Abilities Page " + (page+1), 54, this).fillWith(glass).construct();
		this.gui = gui;
		
		GUIButtonBack button1 = new GUIButtonBack(this); button1.setItem(49, gui); buttons.add(button1);
		
		GUIButtonNextPage button2 = new GUIButtonNextPage(this); button2.setItem(53, gui); buttons.add(button2);
		GUIButtonPrevPage button3 = new GUIButtonPrevPage(this); button3.setItem(45, gui); buttons.add(button3);
		
		ArrayList<Class<? extends IAbility>> free = new ArrayList<Class<? extends IAbility>>();
		ArrayList<Class<? extends IAbility>> priced = new ArrayList<Class<? extends IAbility>>();
		
		if(page < 0) return;
		
		for(Class<? extends IAbility> ability : AbilityRegistry.getClasses(AbilityType.ABILITY)) {
			remaining.add(ability);
		}
		
		//The number of abilities to remove from the list based on page
		int remove = page*28;
		//Reverse order of abilities in list
		Collections.reverse(remaining);
		//Delete appropriate number from the list based on page
		for(int i = remove-1; i >= 0; i--) {
			try {
				remaining.set(i, null);
			}catch(IndexOutOfBoundsException e) {
			}
		}
		
		Collections.reverse(remaining);
		if(!remaining.isEmpty()) {
			try {
				for(Class<? extends IAbility> ability : remaining) {
					if(ability.newInstance().isFree()) {
						free.add(ability);
					}else {
						priced.add(ability);
					}
				}
				
				int nextSlot = 10;
				for(Class<? extends IAbility> ability : free) {
					GUIButtonAbility ab = new GUIButtonAbility(ability, p, slotnum, SlotType.ABILITY);
					buttons.add(ab);
					if(nextSlot == 17) nextSlot = 19;
					if(nextSlot == 26) nextSlot = 28;
					if(nextSlot == 35) nextSlot = 37;
					ab.setItem(nextSlot, gui);
					nextSlot++;
				}
				
				for(Class<? extends IAbility> ability : priced) {
					GUIButtonAbility ab = new GUIButtonAbility(ability, p, slotnum, SlotType.ABILITY);
					buttons.add(ab);
					if(nextSlot == 17) nextSlot = 19;
					if(nextSlot == 26) nextSlot = 28;
					if(nextSlot == 35) nextSlot = 37;
					if(nextSlot == 43) {
						break;
					}
					ab.setItem(nextSlot, gui);
					nextSlot++;
				}
				if(nextSlot < 43) {
					if(nextSlot == 17) nextSlot = 19;
					if(nextSlot == 26) nextSlot = 28;
					if(nextSlot == 35) nextSlot = 37;
					GUIButtonUpcoming button = new GUIButtonUpcoming(this);
					buttons.add(button);
					button.setItem(nextSlot, gui);
				}
			} catch(NullPointerException e) {
				return;
			}
		}else {
			return;
		}
		
		p.openInventory(gui);
	}
	
	public int getCurrentPage() {
		return page;
	}
	
	public GUIButton getButtonBySlot(int slot) {
		for(GUIButton button : buttons) {
			if(button.getSlot() == slot) {
				return button;
			}
		}
		return null;
	}
	
	public void addRemaing(ArrayList<Class<? extends IAbility>> remaining) {
		this.remaining = remaining;
	}
	
	public AbilityGUI openPage(int page) {
		getNew(p, page, slotnum);
		return this;
	}
	
	public static AbilityGUI getNew(Player p, int page, int slotnum) {
		try {
			return new AbilityGUI(p, page, slotnum);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Inventory getInventory() {
		return gui;
	}
	
	

}
