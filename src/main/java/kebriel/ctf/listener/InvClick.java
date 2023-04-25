package kebriel.ctf.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.PlayerInventory;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.display.AbilityGUI;
import kebriel.ctf.display.ArrowTrailGUI;
import kebriel.ctf.display.AuraGUI;
import kebriel.ctf.display.ConfirmationGUI;
import kebriel.ctf.display.CosmeticGUI;
import kebriel.ctf.display.GUIButton;
import kebriel.ctf.display.ItemGUI;
import kebriel.ctf.display.KillEffectGUI;
import kebriel.ctf.display.KillMessageGUI;
import kebriel.ctf.display.OverviewGUI;
import kebriel.ctf.display.PerkGUI;
import kebriel.ctf.display.TrackerGUI;

public class InvClick implements Listener {
	
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(e.getRawSlot() >= 100 && e.getRawSlot() <= 103) {
			e.setCancelled(true);
		}
		if(e.getInventory() instanceof PlayerInventory) {
			if(CTFMain.theState == GameState.PLAYING) {
				return;
			}
		}else {
			e.setCancelled(true);
		}
		if(e.getInventory().getHolder() != null) {
			InventoryHolder gui = e.getInventory().getHolder();
			if(gui instanceof OverviewGUI) {
				OverviewGUI ogui = (OverviewGUI) e.getInventory().getHolder();
				GUIButton clicked = ogui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof AbilityGUI) {
				AbilityGUI agui = (AbilityGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof ItemGUI) {
				ItemGUI agui = (ItemGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof PerkGUI) {
				PerkGUI agui = (PerkGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof TrackerGUI) {
				TrackerGUI agui = (TrackerGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof CosmeticGUI) {
				CosmeticGUI agui = (CosmeticGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof AuraGUI) {
				AuraGUI agui = (AuraGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof ArrowTrailGUI) {
				ArrowTrailGUI agui = (ArrowTrailGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof KillEffectGUI) {
				KillEffectGUI agui = (KillEffectGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof KillMessageGUI) {
				KillMessageGUI agui = (KillMessageGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}else if(gui instanceof ConfirmationGUI) {
				ConfirmationGUI agui = (ConfirmationGUI) e.getInventory().getHolder();
				GUIButton clicked = agui.getButtonBySlot(e.getSlot());
				if(clicked != null) {
					clicked.run(e);
				}
			}
		}
	}

}
