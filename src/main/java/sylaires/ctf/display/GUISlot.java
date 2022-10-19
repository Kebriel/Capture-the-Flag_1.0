package sylaires.ctf.display;

import org.bukkit.entity.Player;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUISlot {
	
	private SlotType type;
	private int slot;
	private boolean unlocked;
	private GUIButton button;
	private Player p;
	
	public GUISlot(SlotType type, int slot, boolean unlocked, Player p) {
		this.type = type;
		this.slot = slot;
		this.p = p;
		this.unlocked = unlocked;
		
		if(!unlocked) {
			GUIButtonLocked locked = new GUIButtonLocked(this); button = locked;
		}else {
			GUIButtonUnlockedSlot unlockedslot = new GUIButtonUnlockedSlot(this); 
			button = unlockedslot; 
		}
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}
	
	public void setUnlocked(boolean state) {
		unlocked = state;
	}
	
	public GUIButton getButton() {
		return button;
	}
	
	public SlotType getType() {
		return type;
	}

}
 