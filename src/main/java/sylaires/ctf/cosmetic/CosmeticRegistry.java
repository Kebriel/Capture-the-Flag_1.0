package sylaires.ctf.cosmetic;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class CosmeticRegistry {
	
	private static ArrayList<Class<?extends ICosmetic>> auras = new ArrayList<Class<? extends ICosmetic>>();
	private static ArrayList<Class<?extends ICosmetic>> trails = new ArrayList<Class<? extends ICosmetic>>();
	private static ArrayList<Class<?extends ICosmetic>> kills = new ArrayList<Class<? extends ICosmetic>>();
	private static ArrayList<Class<?extends ICosmetic>> messages = new ArrayList<Class<? extends ICosmetic>>();
	
	public static void register() {
		auras.add(AuraFlaming.class);
		auras.add(AuraVoid.class);
		auras.add(AuraWings.class);
		auras.add(AuraDemon.class);
		auras.add(AuraCrown.class);
	}
	
	@SuppressWarnings("deprecation")
	public static ICosmetic getInstanceByID(String id) {
		for(Class<? extends ICosmetic> cla : auras) {
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
		for(Class<? extends ICosmetic> cla : trails) {
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
		for(Class<? extends ICosmetic> cla : kills) {
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
		for(Class<? extends ICosmetic> cla : messages) {
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
	
	public static ArrayList<Class<?extends ICosmetic>> getClasses(CosmeticType type) {
		switch(type) {
		case AURA: return auras;
		case TRAIL: return trails;
		case KILL: return kills;
		case MESSAGE: return messages;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<ICosmetic> getInstances(CosmeticType type) throws InstantiationException, IllegalAccessException {
		ArrayList<ICosmetic> instances = new ArrayList<ICosmetic>();
		switch(type) {
		case AURA: for(Class<? extends ICosmetic> cla : auras) {
			instances.add(cla.newInstance());
		} break;
		case TRAIL: for(Class<? extends ICosmetic> cla : trails) {
			instances.add(cla.newInstance());
		} break;
		case KILL: for(Class<? extends ICosmetic> cla : kills) {
			instances.add(cla.newInstance());
		} break;
		case MESSAGE: for(Class<? extends ICosmetic> cla : messages) {
			instances.add(cla.newInstance());
		} break;
		}
		return instances;
	}
	
	@SuppressWarnings("deprecation")
	public static ArrayList<ItemStack> getIcons(CosmeticType type, Player p) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		switch(type) {
		case AURA: for(Class<? extends ICosmetic> cla : auras) {
			try {
				list.add(cla.newInstance().getIcon(p));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} break;
		case TRAIL: for(Class<? extends ICosmetic> cla : trails) {
			try {
				list.add(cla.newInstance().getIcon(p));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} break;
		case KILL: for(Class<? extends ICosmetic> cla : kills) {
			try {
				list.add(cla.newInstance().getIcon(p));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} break;
		case MESSAGE: for(Class<? extends ICosmetic> cla : messages) {
			try {
				list.add(cla.newInstance().getIcon(p));
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
