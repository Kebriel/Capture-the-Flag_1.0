package sylaires.ctf.display;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.ability.IAbility;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MenuBuilder;

public class ConfirmationGUI implements InventoryHolder {

	private PurchaseType type;
	private IAbility toPurchase;
	private GUIButtonLocked slot;
	private Inventory gui;
	private int slotNum;
	private SlotType slotType;
	private int price;
	
	private ArrayList<GUIButtonOption> buttons = new ArrayList<GUIButtonOption>();
	
	public ConfirmationGUI(PurchaseType type, IAbility toPurchase, Player p, int slotNum, SlotType slotType) {
		this.type = type;
		this.toPurchase = toPurchase;
		this.slotNum = slotNum;
		this.slotType = slotType;
		this.price = toPurchase.getCost();
		
		ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ").toItem();
		gui = new MenuBuilder("Purchase this ability?", 27, this).construct();
		gui.setItem(4, glass); gui.setItem(13, glass); gui.setItem(22, glass);
		
		GUIButtonOption yes = new GUIButtonOption(this, true);
		int[] slots1 = {0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21};
		yes.setItemArray(slots1, gui); buttons.add(yes);
		GUIButtonOption no = new GUIButtonOption(this, false);
		int[] slots2 = {5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26};
		no.setItemArray(slots2, gui); buttons.add(no);
		
		p.openInventory(gui);
	} 
	
	public ConfirmationGUI(PurchaseType type, GUIButtonLocked slot, Player p) {
		this.type = type;
		this.slot = slot;
		this.slotNum = slot.getSlotNum();
		this.slotType = slot.getType();
		this.price = slot.getCost();
		ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE, " ").toItem();
		gui = new MenuBuilder("Purchase this slot?", 27, this).construct();
		gui.setItem(4, glass); gui.setItem(13, glass); gui.setItem(22, glass);
		
		GUIButtonOption yes = new GUIButtonOption(this, true);
		int[] slots1 = {0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21};
		yes.setItemArray(slots1, gui); buttons.add(yes);
		GUIButtonOption no = new GUIButtonOption(this, false);
		int[] slots2 = {5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26};
		no.setItemArray(slots2, gui); buttons.add(no);
		
		p.openInventory(gui);
	} 
	
	@Override
	public Inventory getInventory() {
		return gui;
	}
	
	public PurchaseType getType() {
		return type;
	}
	
	public IAbility toPurchase() {
		return toPurchase;
	}
	
	public GUIButtonLocked slotToPurchase() {
		return slot;
	}
	
	public int getSlotNum() {
		return slotNum;
	}
	
	public SlotType getSlotType() {
		return slotType;
	}
	
	public int getCost() {
		return price;
	}
	
	public GUIButton getButtonBySlot(int slot) {
		for(GUIButtonOption button : buttons) {
			for(int i : button.getSlots()) {
				if(i == slot) {
					return button;
				}
			}
		}
		return null;
	}
	
	public static ConfirmationGUI open(PurchaseType type, GUIButtonLocked slot, Player p) {
		return new ConfirmationGUI(type, slot, p);
	}
	
	public static ConfirmationGUI open(PurchaseType type, IAbility toPurchase, Player p, int slotNum, SlotType slotType) {
		return new ConfirmationGUI(type, toPurchase, p, slotNum, slotType);
	}

}
