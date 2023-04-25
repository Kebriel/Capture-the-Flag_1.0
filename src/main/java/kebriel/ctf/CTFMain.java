package kebriel.ctf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import kebriel.ctf.cosmetic.CosmeticRegistry;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.util.MainDB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import kebriel.ctf.ability.AbilityRegistry;
import kebriel.ctf.command.CosmeticCommand;
import kebriel.ctf.command.GameCommand;
import kebriel.ctf.command.HiddenCommand;
import kebriel.ctf.command.MapCommand;
import kebriel.ctf.command.ShoutCommand;
import kebriel.ctf.command.StatsCommand;
import kebriel.ctf.command.VoteCommand;
import kebriel.ctf.listener.AbilityProcDamage;
import kebriel.ctf.listener.BlockPlace;
import kebriel.ctf.listener.Bow;
import kebriel.ctf.listener.Chat;
import kebriel.ctf.listener.Consume;
import kebriel.ctf.listener.FlagListener;
import kebriel.ctf.listener.GenericInteract;
import kebriel.ctf.listener.InvClick;
import kebriel.ctf.listener.Join;
import kebriel.ctf.listener.Misc;
import kebriel.ctf.listener.NpcClick;
import kebriel.ctf.listener.PlayerDeath;
import kebriel.ctf.listener.Projectile;
import kebriel.ctf.listener.QueueMenu;
import kebriel.ctf.listener.Quit;
import kebriel.ctf.listener.Teleport;

public class CTFMain extends JavaPlugin {
	
	//Plugin instance for use in other classes, may be replaced with a getter in the future
	public static CTFMain instance;
	
	//Private fields for database interaction
	private Connection connection;
    private String host, database, username, password;
    private int port;
    
    private MainDB db;
    
    //Public static field saving the current GameState
    public static GameState theState;
    
    //Arbitrary and temporary field to save the number of players required to start
    private int minPlayers = 2;
    
    //The location of the main hub, loaded and instantiated later
    private Location hub;
	
	@Override
	public void onEnable() {
		
		//Instantiate main instance after Plugin is enabled
		instance = this;
		
		//Define database fields with specifics
		host = "localhost";
        port = 3306;
        database = "ctf";
        username = "root";
        password = "root"; 
        
        //Open connection to database and throw exceptions if it fails
        try {
			openConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        //Instantiate wrapper class for main Zeta database, allowing global stats to be accessed by this plugin
        db = new MainDB();
        
        //Load all commands and listeners
		cmds();
		listeners();
		
		Autosave.init(); //Begin Autosave process
		MapManager.loadMaps(); //Load all maps
		LobbyTimer.init(); //Begin the LobbyTimer countdown so game can start
		//Register ability, item and perk classes 
		AbilityRegistry.registerAbilities(); 
		AbilityRegistry.registerItems();
		AbilityRegistry.registerPerks();
		//Register cosmetic classes
		CosmeticRegistry.register();
		
		//Set appropriate GameRules in case of new world
		Bukkit.getWorld("world").setGameRuleValue("randomTickSpeed", "0");
		Bukkit.getWorld("world").setGameRuleValue("keepInventory", "true");
		Bukkit.getWorld("world").setGameRuleValue("doFireTick", "false");
		Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", "false");
		Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorld("world").setGameRuleValue("mobGriefing", "false");
		
		//Load the Hub location from YAML file
		FileManager file = FileManager.getDataFile("hub");
		try {
			hub = new Location(getWorld(), file.get("x"), file.get("y"), file.get("z"));
		}catch(Exception e) {
			Bukkit.getLogger().info("Hub location was not found! Please set and reload.");
			hub = getWorld().getSpawnLocation();
		}
		
	}
	
	/*
	 * Local method used by Commands to set the Hub location, also writes to file
	 */
	public void setHub(Location loc) {
		FileManager file = FileManager.getDataFile("hub");
		
		file.set("x", loc.getBlockX()+0.5);
		file.set("y", Double.valueOf(loc.getBlockY()));
		file.set("z", loc.getBlockZ()+0.5);
		
		hub = loc;
	}

	public Location getHub() {
		return hub;
	}

	public int getReqPlayers() {
		return minPlayers;
	}
	
	@Override
	public void onDisable() {
		//Individual try-catch's for each process, as failure of one will result in abort otherwise
		try {
			FlagManager.derenderFlags();
		} catch(NullPointerException e) {}
		Bukkit.getLogger().info("[CTF] Flags successfully derendered!");
		try {
			GameTimer.redNpc.remove();
		} catch(NullPointerException e) {}
		try {
			GameTimer.blueNpc.remove();
		} catch(NullPointerException e) {}
		Bukkit.getLogger().info("[CTF] NPCs successfully derendered!");
	}
	
	/*
	 * Utility method to get Connection object of the main database
	 */
	public Connection getMainDBConnect() {
		return db.getDBConnection();
	}
	
	/*
	 * Utility method to access the World object cleanly for use in other classes
	 */
	public World getWorld() {
		return Bukkit.getWorlds().get(0);
	}
	
	/*
	 * Private wrapper method to load commands - called in onEnable()
	 */
	private void cmds() {
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("map").setExecutor(new MapCommand());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("game").setExecutor(new GameCommand());
		getCommand("hiddencom").setExecutor(new HiddenCommand());
		getCommand("shout").setExecutor(new ShoutCommand());
		getCommand("cosmetic").setExecutor(new CosmeticCommand());
	}
	
	/*
	 * Private wrapper method to load listeners - called in onEnable()
	 */
	private void listeners() {
		Bukkit.getPluginManager().registerEvents(new Join(), this);
		Bukkit.getPluginManager().registerEvents(new Quit(), this);
		Bukkit.getPluginManager().registerEvents(new Misc(), this);
		Bukkit.getPluginManager().registerEvents(new QueueMenu(), this);
		Bukkit.getPluginManager().registerEvents(new FlagListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
		Bukkit.getPluginManager().registerEvents(new InvClick(), this);
		Bukkit.getPluginManager().registerEvents(new GenericInteract(), this);
		Bukkit.getPluginManager().registerEvents(new NpcClick(), this);
		Bukkit.getPluginManager().registerEvents(new AbilityProcDamage(), this);
		Bukkit.getPluginManager().registerEvents(new Bow(), this);
		Bukkit.getPluginManager().registerEvents(new Consume(), this);
		Bukkit.getPluginManager().registerEvents(new Projectile(), this);
		Bukkit.getPluginManager().registerEvents(new Teleport(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
		Bukkit.getPluginManager().registerEvents(new Chat(), this);
	}
	
	/*
	 * Obligatory method to directly open a connection to the database, based on provided fields
	 */
	 private void openConnection() throws SQLException, ClassNotFoundException {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	      
	        synchronized (this) {
	            if (connection != null && !connection.isClosed()) {
	                return;
	            }
	            connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
	        }
	    }
	 
	public Connection getDBConnection() {
		return connection;
	}

}
