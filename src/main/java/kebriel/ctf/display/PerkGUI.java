package kebriel.ctf.display;

import java.util.ArrayList;

import kebriel.ctf.util.MenuBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kebriel.ctf.ability.AbilityRegistry;
import kebriel.ctf.ability.AbilityType;
import kebriel.ctf.ability.IAbility;
import kebriel.ctf.util.ItemBuilder;

public class PerkGUI implements InventoryHolder {
	
	Inventory gui;
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	
	private PerkGUI(Player p, int slotnum) throws InstantiationException, IllegalAccessException { //Opening a page of it
		ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", (byte) 14).toItem();
		Inventory gui = new MenuBuilder("Perks", 36, this).fillWith(glass).construct();
		this.gui = gui;
		
		GUIButtonBack button1 = new GUIButtonBack(this); button1.setItem(31, gui); buttons.add(button1);
		
		ArrayList<Class<? extends IAbility>> priced = new ArrayList<Class<? extends IAbility>>();
		
		for(Class<? extends IAbility> ability : AbilityRegistry.getClasses(AbilityType.PERK)) {
			priced.add(ability);
		}
		
		int nextSlot = 10;
		
		for(Class<? extends IAbility> ability : priced) {
			GUIButtonAbility ab = new GUIButtonAbility(ability, p, slotnum, SlotType.PERK);
			buttons.add(ab);
			if(nextSlot == 17) nextSlot = 19;
			if(nextSlot == 26) nextSlot = 28;
			if(nextSlot == 35) nextSlot = 37;
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
		
		p.openInventory(gui);
	}
	
	public GUIButton getButtonBySlot(int slot) {
		for(GUIButton button : buttons) {
			if(button.getSlot() == slot) {
				return button;
			}
		}
		return null;
	}
	
	public static PerkGUI getNew(Player p, int slotnum) {
		try {
			return new PerkGUI(p, slotnum);
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
