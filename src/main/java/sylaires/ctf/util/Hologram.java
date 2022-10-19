package sylaires.ctf.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class Hologram {

	/*
	TODO Update all usages of this to nms version per HologramNMS
	 */
	
	private Location loc;
	private String name;
	private List<String> lines = new ArrayList<String>();
	private List<ArmorStand> stands = new ArrayList<ArmorStand>();
	ArmorStand mainStand;
	private boolean show;
	
	public Hologram(Location loc, String name, boolean show) {
		this.loc = loc;
		this.name = name;
		this.show = show;
	}
	
	public void addLine(String line) {
		lines.add(line);
	}
	
	public void addHat(ItemStack hat) {
		mainStand.setHelmet(hat);
	}
	
	public void spawn() {
		ArmorStand stand = loc.getWorld().spawn(loc, ArmorStand.class);
		mainStand = stand;
		stand.setCustomName(name);
		stand.setCustomNameVisible(show);
		stand.setVisible(false);
		stand.setCanPickupItems(false);
		stand.setGravity(false);
		for(String s : lines) {
			Location newloc = null;
			try {
				newloc = new Location(loc.getWorld(), loc.getX(), stands.get(stands.size()-1).getLocation().getY()-0.25, loc.getZ());
			} catch(Exception e) {
				newloc = new Location(loc.getWorld(), loc.getX(), loc.getY()-0.25, loc.getZ());
			}
			ArmorStand stand1 = loc.getWorld().spawn(newloc, ArmorStand.class);
			stand1.setCustomName(s);
			stand1.setCustomNameVisible(true);
			stand1.setVisible(false);
			stand1.setCanPickupItems(false);
			stand1.setGravity(false);
			stands.add(stand1);
		}
		stands.add(stand);
	}
	
	public Location getLocation() {
		return this.loc;
	}
	
	public void remove() {
		for(ArmorStand stand : stands) {
			stand.remove();
		}
		mainStand=null;
	}
	
	public boolean isAlive() {
		if(mainStand == null) {
			return false;
		}else return true;
	}
	
	public ArmorStand get() {
		return mainStand;
	}

}
