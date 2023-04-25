package kebriel.ctf.display;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIButton {
	
	private int slot;
	private ItemStack item;
	private Inventory inv;
	
	public GUIButton(ItemStack stack) {
		item = stack;
	}
	
	public GUIButton() {
	}
	
	public void setItem(int slot, Inventory inv) {
		this.slot = slot;
		this.inv = inv;
		inv.setItem(slot, item);
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setIcon(ItemStack stack) {
		item = stack;
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	public void run(InventoryClickEvent e) {
	}

}
