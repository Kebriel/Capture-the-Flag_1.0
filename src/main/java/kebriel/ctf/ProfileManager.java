package kebriel.ctf;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileManager {
	
	//Private static cache of all currently loaded profiles
	private static HashMap<UUID, PlayerProfile> profiles = new HashMap<UUID, PlayerProfile>();

	//Search against cache for a profile instead of interacting with DB unnecessarily
	public static PlayerProfile getProfile(Player p) {
		return profiles.get(p.getUniqueId());
	}
	
	public static PlayerProfile getProfile(UUID id) {
		return profiles.get(id);
	}
	
	public static PlayerProfile getProfile(OfflinePlayer p) {
		try {
			return profiles.get(p.getUniqueId());
		} catch(NullPointerException e) {
			return null;
		}
	}
	
	public static void addProfile(PlayerProfile prof) {
		if(!profiles.containsKey(prof.getPUID())) { //Safeguard against duplicates
			profiles.put(prof.getPUID(), prof);
		}else {
			Bukkit.getLogger().info("[CTF-WARN] An attempt was made to add a duplicate player profile!");
		}
	}
	
	public static ArrayList<PlayerProfile> getProfiles() {
		return new ArrayList<>(profiles.values());
	}
	
	public static boolean doesHaveProf(Player p) {
		if(profiles.containsKey(p.getUniqueId())) {
			return true;
		}else {
			return false;
		}
	}
	
	public static void removeProfile(Player p) { //Clear cached profile
		if(profiles.keySet().contains(p.getUniqueId())) {
			profiles.remove(p.getUniqueId());
		}
	}
	
	public static void queryFromDatabase(OfflinePlayer p) {
		String uuid = p.getUniqueId().toString();
		uuid = uuid.replaceAll("-", "_");
		uuid = "player" + uuid;
		final String uuid1 = uuid;
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					DatabaseMetaData dbm;
					dbm = CTFMain.instance.getDBConnection().getMetaData();
					ResultSet tables = dbm.getTables(null, null, uuid1, null);
					if(tables.next()) { //They have a profile!
						PlayerProfile prof = new PlayerProfile(p);
						ProfileManager.addProfile(prof); //Profile successfully loaded, it can now be accessed in cache
					}else {
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(CTFMain.instance);
	}
}
