package sylaires.ctf.display;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.ability.IAbility;
import sylaires.ctf.game.TeamHandler;
import sylaires.ctf.util.ItemBuilder;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class GUIButtonAbility extends GUIButton {
	
	private ItemStack stack;
	private IAbility ability;
	private boolean selected;
	private int slot;
	private SlotType type;
	
	@SuppressWarnings("deprecation")
	public GUIButtonAbility(Class<? extends IAbility> ability, Player p, int slotnum, SlotType type) throws InstantiationException, IllegalAccessException {
		PlayerProfile prof = ProfileManager.getProfile(p);
		this.ability = ability.newInstance();
		this.type = type;
		if(this.ability.isFree()) {
			stack = this.ability.getIcon();
		}else {
			stack = this.ability.getIcon(p);
		}
		slot = slotnum;
		selected = false;
		if(prof.getIsSelected(this.ability.getId())) {
			stack = new ItemBuilder(stack).addEnchantment(Enchantment.ARROW_DAMAGE, 0).addFlag(ItemFlag.HIDE_ENCHANTS)
					.setLore(stack.getItemMeta().getLore().size()-1, ChatColor.GREEN + "Selected")
					.toItem();
			selected = true;
		}
		setIcon(stack);
	}
	
	public ItemStack getItem() {
		return stack;
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(selected) {
			if(type == SlotType.ABILITY) {
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Ability already selected!");
			}else if(type == SlotType.INVENTORY) {
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Item already selected!");
			}else if(type == SlotType.PERK) {
				MessageUtil.sendToPlayer(p, ChatColor.RED + "Perk already selected!");
			}
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
		}else {
			if(ability.isFree()) {
				prof.setSelected(ability.getId(), slot, type);
				AbilityGUI.getNew(p, 0, slot);
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
			}else { //Select or purchase
				int price = ability.getCost();
				if(prof.getIsUnlocked(ability.getId())) { //We own it, select
					prof.setSelected(ability.getId(), slot, type);
					if(type == SlotType.ABILITY) {
						AbilityGUI.getNew(p, 0, slot);
					}else if(type == SlotType.INVENTORY) {
						ItemGUI.getNew(p, slot);
					}else if(type == SlotType.PERK) {
						PerkGUI.getNew(p, slot);
					}
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
					
					//Handle items
					PlayerInventory inv = p.getInventory();
					if(CTFMain.theState == GameState.PLAYING) {
						TeamHandler.applyPrefixes(p);
						if(!prof.getIsSelected("item_rod")) {
							inv.remove(Material.FISHING_ROD);
						}
						if(!prof.getIsSelected("item_snow")) {
							inv.remove(Material.SNOW_BALL);
						}
						if(!prof.getIsSelected("ability_juggernaut")) {
							ItemStack chest = inv.getChestplate();
							chest.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
						}else {
							ItemStack chest = inv.getChestplate();
							chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
						}
						if(!prof.getIsSelected("item_gapple")) {
							inv.remove(Material.GOLDEN_APPLE);
						}
						if(!prof.getIsSelected("item_stick")) {
							inv.remove(Material.STICK);
						}
						if(!prof.getIsSelected("item_bandage")) {
							inv.remove(Material.PAPER);
						}
						if(!prof.getIsSelected("item_pearl")) {
							inv.remove(Material.ENDER_PEARL);
						}
						if(!prof.getIsSelected("item_blocks")) {
							inv.remove(Material.GLASS);
						}
					}
					
				}else {
					if(prof.getGold() < price) { //Not enough money
						MessageUtil.sendToPlayer(p, ChatColor.RED + "You cannot afford this!");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					}else { //They have enough, do they want to?
						ConfirmationGUI.open(PurchaseType.ABILITY, ability, p, slot, type);
					}
				}
			}
			ScoreboardSidebar.update(p);
		}
	}

}
