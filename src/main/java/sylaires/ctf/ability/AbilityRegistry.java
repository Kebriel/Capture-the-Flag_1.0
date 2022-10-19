package sylaires.ctf.ability;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class AbilityRegistry {
	
	private static ArrayList<Class<?extends IAbility>> abilities = new ArrayList<Class<? extends IAbility>>();
	private static ArrayList<Class<?extends IAbility>> items = new ArrayList<Class<? extends IAbility>>();
	private static ArrayList<Class<?extends IAbility>> perks = new ArrayList<Class<? extends IAbility>>();
	
	public static void registerAbilities() {
		abilities.add(AbilityPanic.class);
		abilities.add(AbilityExpandedQuiver.class);
		abilities.add(AbilityParry.class);
		abilities.add(AbilityKevlar.class);
		abilities.add(AbilityCleanStrike.class);
		abilities.add(AbilityMending.class);
		abilities.add(AbilityAntidote.class);
		
		abilities.add(AbilityGladiator.class);
		abilities.add(AbilityMassacre.class);
		abilities.add(AbilityBastion.class);
		abilities.add(AbilityJuggernaut.class);
		abilities.add(AbilityXP.class);
		abilities.add(AbilityFleetFooted.class);
		abilities.add(AbilityIronskin.class);
		abilities.add(AbilityFarshot.class);
		abilities.add(AbilityLifeblood.class);
		abilities.add(AbilityBraced.class);
		abilities.add(AbilityAlchemist.class);
		abilities.add(AbilityBerserk.class);
		abilities.add(AbilityPointBlank.class);
		abilities.add(AbilityLeech.class);
		abilities.add(AbilityFirebrand.class);
	}
	
	public static void registerItems() {
		items.add(ItemFishingRod.class);
		items.add(ItemSnowballs.class);
		items.add(ItemGapple.class);
		items.add(ItemKBStick.class);
		items.add(ItemBandage.class);
		items.add(ItemPearl.class);
		items.add(ItemBlocks.class);
	}
	
	public static void registerPerks() {
		perks.add(PerkUntrackable.class);
		perks.add(PerkHaste.class);
		perks.add(PerkRoyalty.class);
	}
	
	@SuppressWarnings("deprecation")
	public static IAbility getInstanceByID(String id) {
		for(Class<? extends IAbility> cla : abilities) {
			try {
				if(cla.newInstance().getId().equalsIgnoreCase(id)) {
					return cla.newInstance();
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		for(Class<? extends IAbility> cla : items) {
			try {
				if(cla.newInstance().getId().equalsIgnoreCase(id)) {
					return cla.newInstance();
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		for(Class<? extends IAbility> cla : perks) {
			try {
				if(cla.newInstance().getId().equalsIgnoreCase(id)) {
					return cla.newInstance();
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static ArrayList<Class<?extends IAbility>> getClasses(AbilityType type) {
		switch(type) {
		case ABILITY: return abilities;
		case ITEM: return items;
		case PERK: return perks;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<IAbility> getInstances(AbilityType type) throws InstantiationException, IllegalAccessException {
		ArrayList<IAbility> instances = new ArrayList<IAbility>();
		switch(type) {
		case ABILITY: for(Class<? extends IAbility> cla : abilities) {
			instances.add(cla.newInstance());
		} break;
		case ITEM: for(Class<? extends IAbility> cla : items) {
			instances.add(cla.newInstance());
		} break;
		case PERK: for(Class<? extends IAbility> cla : perks) {
			instances.add(cla.newInstance());
		} break;
		}
		return instances;
	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<ItemStack> getIcons(AbilityType type) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		switch(type) {
		case ABILITY: for(Class<? extends IAbility> cla : abilities) {
			try {
				list.add(cla.newInstance().getIcon());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} break;
		case ITEM: for(Class<? extends IAbility> cla : items) {
			try {
				list.add(cla.newInstance().getIcon());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} break;
		case PERK: for(Class<? extends IAbility> cla : perks) {
			try {
				list.add(cla.newInstance().getIcon());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} break;
		}
		return list;
	}

}
