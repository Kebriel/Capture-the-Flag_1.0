package kebriel.ctf.display;

import java.util.ArrayList;

import kebriel.ctf.cosmetic.CosmeticRegistry;
import kebriel.ctf.cosmetic.CosmeticType;
import kebriel.ctf.cosmetic.ICosmetic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kebriel.ctf.util.ItemBuilder;
import kebriel.ctf.util.MenuBuilder;

public class AuraGUI implements InventoryHolder {
	
	Inventory gui;
	private ArrayList<GUIButton> buttons = new ArrayList<GUIButton>();
	
	private AuraGUI(Player p) {
		ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ", (byte) 2).toItem();
		gui = new MenuBuilder(ChatColor.GOLD + "Auras", 27, this).fillWith(glass).construct();
		
		GUIButtonBack button1 = new GUIButtonBack(this); button1.setItem(22, gui); buttons.add(button1);
		
		GUIButtonClearCosmetic button2 = new GUIButtonClearCosmetic(CosmeticType.AURA, p); button2.setItem(4, gui); buttons.add(button2);
		
		ArrayList<Class<? extends ICosmetic>> auras = new ArrayList<Class<? extends ICosmetic>>();
		for(Class<? extends ICosmetic> aura : CosmeticRegistry.getClasses(CosmeticType.AURA)) {
			auras.add(aura);
		}
		
		int nextSlot = 10;
		
		for(Class<? extends ICosmetic> aura : auras) {
			GUIButtonCosmetic ab = new GUIButtonCosmetic(p, aura, CosmeticType.AURA);
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
	
	public static AuraGUI getNew(Player p) {
		return new AuraGUI(p);
	}
	
	@Override
	public Inventory getInventory() {
		return gui;
	}

}
