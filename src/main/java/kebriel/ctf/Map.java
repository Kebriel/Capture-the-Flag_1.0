package kebriel.ctf;

import org.bukkit.Location;

public class Map {
	
	//All private stored fields
	private String name; //The name associated with the Map, case insensitive
	private int id; //Numerical ID associated with the Map, each Map has its own
	//Bukkit locations associated with Red team
	private Location redSpawn;
	private Location redFlag;
	//Bukkit locations associated with Blue team
	private Location blueSpawn;
	private Location blueFlag;
	
	//NPC locations
	private Location redNpc;
	private Location blueNpc;
	
	/*
	 * Map constructor to create new base Map object
	 */
	public Map(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/*
	 * Basic getter for name String
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * Basic setter to set name String
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * Basic getter to get numerical ID
	 */
	public int getId() {
		return id;
	}
	
	/*
	 * Getter for Bukkit location of red spawn
	 */
	public final Location getRedSpawn() {
		return redSpawn;
	}
	
	/*
	 * Setter for Bukkit location of red spawn
	 */
	public void setRedSpawn(Location redSpawn) {
		this.redSpawn = redSpawn;
	}
	
	/*
	 * Getter for Bukkit location of Red Flag
	 */
	public Location getRedFlag() {
		return redFlag;
	}
	
	/*
	 * Setter for Bukkit location of Red Flag
	 */
	public void setRedFlag(Location redFlag) {
		this.redFlag = redFlag;
	}
	
	/*
	 * Getter for Bukkit location of blue spawn
	 */
	public final Location getBlueSpawn() {
		return blueSpawn;
	}
	
	/*
	 * Setter for Bukkit location of red spawn
	 */
	public void setBlueSpawn(Location blueSpawn) {
		this.blueSpawn = blueSpawn;
	}
	
	/*
	 * Getter for Bukkit location of Blue Flag
	 */
	public Location getBlueFlag() {
		return blueFlag;
	}
	
	/*
	 * Setter for Bukkit location of Blue Flag
	 */
	public void setBlueFlag(Location blueFlag) {
		this.blueFlag = blueFlag;
	}
	
	/*
	 * Getter for Bukkit location of red NPC
	 */
	public Location getRedNpc() {
		return redNpc;
	}
	
	/*
	 * Setter for Bukkit location of red NPC
	 */
	public void setRedNpc(Location redNpc) {
		this.redNpc = redNpc;
	}
	
	/*
	 * Getter for Bukkit location of blue NPC
	 */
	public Location getBlueNpc() {
		return blueNpc;
	}

	/*
	 * Setter for Bukkit location of blue NPC
	 */
	public void setBlueNpc(Location blueNpc) {
		this.blueNpc = blueNpc;
	}
	
	
	

}
