package kebriel.ctf;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MapManager {

	/*
	TODO Replace static fields and methods with getters and constructor
	 */
	
	//A list of Map objects filled with the loadMaps() method
	public static ArrayList<Map> maps = new ArrayList<Map>();
	
	//A private Map object representing the map currently being played on
	private static Map current_map;
	
	/*
	 * This static method acts as a pseudo-constructor, in the sense that it loads maps from file 
	 * and prepares the game to run. More specifics outlined below
	 */
	public static void loadMaps() {
		//Load maps.yml -- As per FileManager, if the YAML is not found it will be created in the proper directory
		FileManager file = FileManager.getDataFile("maps");
		//Iterate through all recorded maps -- these are defined via command or manually (cmd recommended) 
		for(String s : file.getKeys()) {
			if(StringUtils.isNumeric(s)) { //Obligatory numeric check of current String
				int id; //Base ID object, defaults to zero as per Java rules
				//Try-Catch acting as an Integer check. We previously guaranteed Numeric value, but it could be double or float
				try {
					id = Integer.parseInt(s);
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
				}
				String name = file.get(id + ".name"); //Get saved root name
				Map map = new Map(id, name); //Create an instance for this Map object -- This is of course done for every entry in the KeyList
				
				/*Repeated try-catch to load each individual location from YAML into Bukkit API for in-game use. 
				 * Failure is a result of it being improperly defined
				 */
				try {
					Location redSpawn = new Location(CTFMain.instance.getWorld(), file.get(id + ".redSpawn.X"),
							file.get(id + ".redSpawn.Y"),
							file.get(id + ".redSpawn.Z"));
					map.setRedSpawn(redSpawn);
				}catch(Exception ex) {
					Bukkit.getLogger().info("[CTF/ERR] A spawn was not found! Please set it so that the game can continue!");
					CTFMain.theState = GameState.TERMINATED;
				}
				try {
					Location redFlag = new Location(CTFMain.instance.getWorld(), file.get(id + ".redFlag.X"),
							file.get(id + ".redFlag.Y"),
							file.get(id + ".redFlag.Z"));
					double yaw = file.get(id + ".redFlag.yaw");
					redFlag.setYaw((float) yaw);
					map.setRedFlag(redFlag);
				}catch(Exception ex) {
					Bukkit.getLogger().info("[CTF/ERR] A spawn was not found! Please set it so that the game can continue!");
					CTFMain.theState = GameState.TERMINATED;
					ex.printStackTrace();
				}
				try {
					Location blueSpawn = new Location(CTFMain.instance.getWorld(), file.get(id + ".blueSpawn.X"),
							file.get(id + ".blueSpawn.Y"),
							file.get(id + ".blueSpawn.Z"));
					map.setBlueSpawn(blueSpawn);
				}catch(Exception ex) {
					Bukkit.getLogger().info("[CTF/ERR] A spawn was not found! Please set it so that the game can continue!");
					CTFMain.theState = GameState.TERMINATED;
				}
				try {
					Location blueFlag = new Location(CTFMain.instance.getWorld(), file.get(id + ".blueFlag.X"),
							file.get(id + ".blueFlag.Y"),
							file.get(id + ".blueFlag.Z"));
					double yaw = file.get(id + ".blueFlag.yaw");
					blueFlag.setYaw((float) yaw);
					map.setBlueFlag(blueFlag);
				}catch(Exception ex) {
					Bukkit.getLogger().info("[CTF/ERR] A spawn was not found! Please set it so that the game can continue!");
					CTFMain.theState = GameState.TERMINATED;
				}
				try {
					Location redNpc = new Location(CTFMain.instance.getWorld(), file.get(id + ".redNpc.X"),
							file.get(id + ".redNpc.Y"),
							file.get(id + ".redNpc.Z"));
					double yaw = file.get(id + ".redNpc.yaw");
					redNpc.setYaw((float) yaw);
					map.setRedNpc(redNpc);
				}catch(Exception ex) {
					Bukkit.getLogger().info("[CTF/ERR] An NPC's location was not found! Please set it so that the game can continue!");
					CTFMain.theState = GameState.TERMINATED;
				}
				try {
					Location blueNpc = new Location(CTFMain.instance.getWorld(), file.get(id + ".blueNpc.X"),
							file.get(id + ".blueNpc.Y"),
							file.get(id + ".blueNpc.Z"));
					double yaw = file.get(id + ".blueNpc.yaw");
					blueNpc.setYaw((float) yaw);
					map.setBlueNpc(blueNpc);
				}catch(Exception ex) {
					Bukkit.getLogger().info("[CTF/ERR] An NPC's location was not found! Please set it so that the game can continue!");
					CTFMain.theState = GameState.TERMINATED;
				}
				//Add the newly-filled Map object to the list
				maps.add(map);
			}
		}
	}
	
	/*
	 * A simple static getter that lets you find a Map object by ID
	 */
	public static Map getMap(int id) {
		for(Map map : maps) {
			if(map.getId() == id) {
				return map;
			}
		}
		return null; //If it failed to find
	}
	
	/*
	 * A simple static getter that lets you find a Map object by name 
	 */
	public static Map getMap(String name) {
		for(Map map : maps) {
			if(map.getName().equalsIgnoreCase(name)) {
				return map;
			}
		}
		return null; //If it failed to find
	}
	
	/*
	 * A getter that returns the currently active Map object
	 */
	public static Map getCurrent() {
		return current_map;
	}
	
	/*
	 * A utility setter method that sets the currently active Map, replacing the previous
	 */
	public static void setCurrent(Map map) {
		current_map = map;
	}
	
	/*
	 * A static save method that will write Map data into the YAML file
	 */
	public static void saveMapToFile(Map map) {
		FileManager file = FileManager.getDataFile("maps");
		int id = map.getId();
		file.set(id + "", ""); //A failsafe to create an entry for the new Map
		file.set(id + ".name", map.getName()); //Writes the 'name' String into the file
		
		//Try-catches that systematically writes each possible coordinate, and silently handles null values if said coordinates have yet to be set
		try { 
			file.set(id + ".redSpawn.X", map.getRedSpawn().getX());
			file.set(id + ".redSpawn.Y", map.getRedSpawn().getY());
			file.set(id + ".redSpawn.Z", map.getRedSpawn().getZ());
		} catch (NullPointerException e) {} //That var isn't set yet
		
		try {
			file.set(id + ".redFlag.X", map.getRedFlag().getX());
			file.set(id + ".redFlag.Y", map.getRedFlag().getY());
			file.set(id + ".redFlag.Z", map.getRedFlag().getZ());
			file.set(id + ".redFlag.yaw", Math.floor(map.getRedFlag().getYaw()));
		} catch(NullPointerException e) {}
		
		try {
			file.set(id + ".blueSpawn.X", map.getBlueSpawn().getX());
			file.set(id + ".blueSpawn.Y", map.getBlueSpawn().getY());
			file.set(id + ".blueSpawn.Z", map.getBlueSpawn().getZ());
		} catch(NullPointerException e) {}
		
		try {
			file.set(id + ".blueFlag.X", map.getBlueFlag().getX());
			file.set(id + ".blueFlag.Y", map.getBlueFlag().getY());
			file.set(id + ".blueFlag.Z", map.getBlueFlag().getZ());
			file.set(id + ".blueFlag.yaw", Math.floor(map.getBlueFlag().getYaw()));
		} catch(NullPointerException e) {}
		
		try {
			file.set(id + ".redNpc.X", map.getRedNpc().getX());
			file.set(id + ".redNpc.Y", map.getRedNpc().getY());
			file.set(id + ".redNpc.Z", map.getRedNpc().getZ());
			file.set(id + ".redNpc.yaw", Math.floor(map.getRedNpc().getYaw()));
		} catch(NullPointerException e) {}
		
		try {
			file.set(id + ".blueNpc.X", map.getBlueNpc().getX());
			file.set(id + ".blueNpc.Y", map.getBlueNpc().getY());
			file.set(id + ".blueNpc.Z", map.getBlueNpc().getZ());
			file.set(id + ".blueNpc.yaw", Math.floor(map.getBlueNpc().getYaw()));
		} catch(NullPointerException e) {}
	}
	
	/*
	 * A static method that creates a temporary 'Map' object, which can then be saved permanently using the above method.
	 * Returns the created Map object
	 */
	public static Map createMap(int id, String name) {
		FileManager file = FileManager.getDataFile("maps");
		file.set("" + id, "");
		file.set(id + ".name", name);
		Map map = new Map(id, name);
		maps.add(map);
		
		return map;
	}
	
	/*
	 * A static boolean that will remove the desired Map object, and also remove any records with the correct ID from 
	 * file.
	 */
	public static boolean deleteMapByID(int id) { //Return success 
		for(Map map : maps) {
			if(map.getId() == id) {
				FileManager file = FileManager.getDataFile("maps");
				file.set(id + "", null);
				maps.remove(map);
				return true;
			}
		}
		return false;
	}

}
