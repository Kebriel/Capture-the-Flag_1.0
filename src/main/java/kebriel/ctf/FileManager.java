package kebriel.ctf;

import java.io.File;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {
	
	//PLEASE NOTE: Program Zeta file handling may be migrated fully to SQL Database in the future!
	
	private File file; //Base Java file class as a private field
	private FileConfiguration config; //Bukkit's YAML support via FileConfiguration field
	
	/*
	 * Private constructor for singleton
	 */
	private FileManager(String filename) {
		//Instantiate File object based on provided string and local data folder
		file = new File(CTFMain.instance.getDataFolder(), filename + ".yml");
		
		//If the file doesn't exist, create it
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch(Exception e) { //Failsafe error throws, should never be and has never been called
				Bukkit.getLogger().info("[CTF] **ERROR**");
				Bukkit.getLogger().info("File could not be created.");
				Bukkit.getLogger().info("Class: FileManager - Method: Constructor");
				Bukkit.getLogger().info("[CTF] **ERROR**");
			}
		}
		
		//Convert File object into usable 'config' object
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	/*
	 * Ambiguous method to load and return a desired object based on given path
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String path) {
		return (T) config.get(path);
	}
	
	/*
	 * Set a desired path to the given object value
	 */
	public void set(String path, Object value) {
		config.set(path, value);
		save();
	}
	
	/*
	 * A simple boolean to return whether or not the file contains the given path
	 */
	public boolean contains(String path) {
		return config.contains(path);
	}
	
	/*
	 * Private method called locally to write all changes to the file. Calling publicly is unnecessary. 
	 */
	private void save() {
		try {
			config.save(file);
		} catch(Exception e) { //Another failsafe, should never be called
			Bukkit.getLogger().info("[CTF/ERROR] FileManager: Method save() was unable to save a file!");
		}
	}
	
	/*
	 * Returns a String Set containing all keys in the file
	 */
	public Set<String> getKeys() {
		return config.getKeys(false);
	}
	
	/*
	 * Method to create a ConfigurationSection object, calls save(). Unused, deprecated.
	 */
	@Deprecated
	public ConfigurationSection createSection(String path) {
		ConfigurationSection section = config.createSection(path);
		save();
		return section;
	}
	
	/*
	 * Static singleton method returns new instance based on given filename 
	 */
	public static FileManager getDataFile(String name) {
		FileManager data = new FileManager(name);
		return data;
	}

}
