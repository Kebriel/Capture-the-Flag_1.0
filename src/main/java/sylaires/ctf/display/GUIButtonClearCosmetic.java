package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.cosmetic.CosmeticType;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonClearCosmetic extends GUIButton {
	
	private CosmeticType type;
	private ItemStack stack;
	
	public GUIButtonClearCosmetic(CosmeticType type, Player p) {
		this.type = type;
		
		stack = new ItemBuilder(Material.HOPPER, ChatColor.RED + "Clear Effect")
				.addLore(ChatColor.YELLOW + "Click to clear your current effect").toItem();
		setIcon(stack);
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(!prof.getSelectedCosmetic(type).equalsIgnoreCase("none")) {
			prof.setSelectedCosmetic("none", type);
			p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
			MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Selection wiped!");
			if(type == CosmeticType.AURA) {
				prof.getEffect().disable();
				AuraGUI.getNew(p);
			}else if(type == CosmeticType.TRAIL) {
				ArrowTrailGUI.getNew(p);
			}else if(type == CosmeticType.KILL) {
				KillEffectGUI.getNew(p);
			}else if(type == CosmeticType.MESSAGE) {
				KillMessageGUI.getNew(p);
			}
		}else {
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
			MessageUtil.sendToPlayer(p, ChatColor.RED + "You have nothing selected!");
		}
	}

}
