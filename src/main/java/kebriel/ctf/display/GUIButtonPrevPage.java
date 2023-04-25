package kebriel.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import kebriel.ctf.util.ItemBuilder;

public class GUIButtonPrevPage extends GUIButton {
	
	private InventoryHolder gui;
	private ItemStack stack;
	
	public GUIButtonPrevPage(InventoryHolder gui) {
		this.gui = gui;
		
		stack = new ItemBuilder(Material.ARROW, ChatColor.YELLOW + "Previous Page")
				.addLore(ChatColor.WHITE + "Click to go to the previous page")
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
				if(agui.getCurrentPage() != 0) {
					agui.openPage(agui.getCurrentPage()-1);
				}
			}
		}
	}

}
