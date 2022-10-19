package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.cosmetic.AuraEffect;
import sylaires.ctf.cosmetic.CosmeticType;
import sylaires.ctf.cosmetic.ICosmetic;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonCosmetic extends GUIButton {
	
	private CosmeticType type;
	private ICosmetic instance;
	private ItemStack stack;
	private boolean selected;
	
	@SuppressWarnings("deprecation")
	public GUIButtonCosmetic(Player p, Class<? extends ICosmetic> instance, CosmeticType type) {
		try {
			this.instance = instance.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		this.type = type;
		this.stack = this.instance.getIcon(p);
		
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(prof.getIsSelected(this.instance.getId())) selected = true;
		setIcon(stack);
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(selected) {
			if(type == CosmeticType.AURA) {
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Aura already selected!");
			}else if(type == CosmeticType.TRAIL) {
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Arrow trail already selected!");
			}else if(type == CosmeticType.KILL) {
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Kill effect already selected!");
			}else if(type == CosmeticType.MESSAGE) {
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Kill message already selected!");
			}
		}else {
			if(instance.unlocked(p)) {
				prof.setSelectedCosmetic(instance.getId(), type);
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
				if(type == CosmeticType.AURA) {
					AuraGUI.getNew(p);
					prof.setEffect(new AuraEffect(p, instance.getId()));
				}else if(type == CosmeticType.TRAIL) {
					ArrowTrailGUI.getNew(p);
				}else if(type == CosmeticType.KILL) {
					KillEffectGUI.getNew(p);
				}else if(type == CosmeticType.MESSAGE) {
					KillMessageGUI.getNew(p);
				}
			}else {
				MessageUtil.sendToPlayer(p, instance.getDisplayedRequirement());
				p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
			}
		}
	}

}
