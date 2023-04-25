package kebriel.ctf;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import kebriel.ctf.cosmetic.AuraEffect;
import kebriel.ctf.cosmetic.CosmeticType;
import kebriel.ctf.display.SlotType;
import kebriel.ctf.game.CompassTracker;
import kebriel.ctf.game.GlobalLevel;
import kebriel.ctf.game.TeamHandler;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerProfile {
	
	//Private fields representing all saved stats
	private int deaths;
	private int kills;
	private int gold;
	private int games;
	private int wins;
	private int losses;
	private int captures;
	private int level;
	private int xp;
	private int carrier_kills;
	private int chall_num;
	private int assists;
	private long seconds_played;
	private int xpToNext;
	
	private String timePlayed;
	
	//Private fields representing unlockable slots
	private boolean extraAbSlot1;
	private boolean extraAbSlot2;
	private boolean perkSlot1;
	private boolean perkSlot2;
	private boolean invSlot;
	
	//Private fields saving everything currently selected
	private String ab1;
	private String ab2;
	private String ab3;
	private String ab4;
	private String invab;
	private String perk1;
	private String perk2;
	
	private String selected_aura;
	private String selected_trail;
	private String selected_kill;
	private String selected_message;
	
	//Private boolean fields representing whether every ability/item/perk/cosmetic that can be unlocked has been
	private boolean gladiator;
	private boolean massacre;
	private boolean bastion;
	private boolean juggernaut;
	private boolean abxp;
	private boolean fleet;
	private boolean ironskin;
	private boolean farshot;
	private boolean lifeblood;
	private boolean braced;
	private boolean alchemist;
	private boolean berserk;
	private boolean pointblank;
	private boolean leech;
	private boolean firebrand;
	
	private boolean rod;
	private boolean snow;
	private boolean gapple;
	private boolean stick;
	private boolean bandage;
	private boolean pearl;
	private boolean blocks;
	
	private boolean untrackable;
	private boolean haste;
	private boolean reinforce;
	private boolean royalty;
	
	//Unsaved double representing calculated values
	private double kdr;
	private double wlr;
	
	//Temporary stats saved each game
	private int deaths_this_game;
	private int kills_this_game;
	private int gold_this_game;
	private int captures_this_game;
	private long seconds_this_session;
	
	//Private field saving whether or not this player has been marked as dead
	private boolean isDead;
	
	//A private field representing the currently selected Aura cosmetic
	private AuraEffect effect = null;
	
	//A private field to save the player's CompassTracker object
	private CompassTracker compassTracker;
	
	//Basic info associated with the player
	private Player p;
	private UUID puid;
	private String pname;
	
	//Global info fetched from the database
	private String donor_rank;
	private String staff_rank;
	
	//Track last hitters
	private ArrayList<UUID> last_hitters = new ArrayList<UUID>();
	
	//Track abilities
	private boolean berserked;
	
	/*
	 * Original constructor taking a Player as a parameter. Configures base data from there.
	 */
	public PlayerProfile(Player p) {
		//Instantiate all initial fields
		this.p = p;
		puid = p.getUniqueId();
		pname = p.getName();
		
		loadFromDB(); //Load and set data from CTF database

		//Track playtime by second
		new BukkitRunnable() { //Async repeating task that tallies up playtime once the Player's profile is loaded

			@Override
			public void run() {
				if(p.isOnline()) {
					seconds_played++;
					seconds_this_session++;
				}
			}
			
		}.runTaskTimerAsynchronously(CTFMain.instance, 0, 20);
	}
	
	/*
	 * Alternate constructor to load data of an OfflinePlayer
	 */
	public PlayerProfile(OfflinePlayer p) {
		//Instantiate necessary fields
		puid = p.getUniqueId();
		pname = p.getName();
		
		loadFromDB(); //Load offline player's data
	}
	
	public UUID getPUID() {
		return puid;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public String getPName() {
		return pname;
	}
	
	public int getKills() {
		return kills;
	}
	
	public void addKill() {
		kills++;
		kills_this_game++;
	}
	
	public int getKillsThisGame() {
		return kills_this_game;
	}
	
	public int getDeathsThisGame() {
		return deaths_this_game;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public void addDeath() {
		deaths++;
		deaths_this_game++;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void addGold(int amount, boolean ingame) {
		gold+=amount;
		if(ingame) gold_this_game+=amount;
	}
	
	public void subtractGold(int amount, boolean ingame) {
		this.gold-=amount;
		if(ingame) gold_this_game-=amount;
	}
	
	public int getGoldThisGame() {
		return gold_this_game;
	}
	
	public int getGames() {
		return games;
	}
	
	public void addGame() {
		games++;
	}
	
	public int getWins() {
		return wins;
	}
	
	public void addWin() {
		wins++;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public void addLoss() {
		losses++;
	}
	
	public int getCaptures() {
		return captures;
	}
	
	public int getCapturesThisGame() {
		return captures_this_game;
	}
	
	public void setLevel(int amount) {
		level = amount;
	}
	
	public void addCapture() {
		captures_this_game++;
		captures++;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void addLevel() {
		level++;
	}
	
	public int getXp() {
		return xp;
	}
	
	public void setXP(int amount) {
		xp = amount;
	}
	
	public void addXp(int amount) {
		xp+=amount;
		if(p != null) GlobalLevel.checkXP(p);
	}
	
	public int getCarrierKills() {
		return carrier_kills;
	}
	
	public void addCarrierKill() {
		carrier_kills++;
	}
	
	public int getChallengesCompleted() {
		return chall_num;
	}
	
	public void addChallenge() {
		chall_num++;
	}
	
	public int getAssists() {
		return assists;
	}
	
	public void addAssist() {
		assists++;
	}
	
	public long getSecondsPlayedThisSession() {
		return seconds_this_session;
	}
	
	public String getTimePlayed() {
		long dy = TimeUnit.SECONDS.toDays(seconds_played);
		final long yr = dy / 365;
		dy %= 365;
		final long mn = dy / 30;
		dy %= 30;
		final long wk = dy / 7;
		dy %= 7;
		final long hr = TimeUnit.SECONDS.toHours(seconds_played)
				- TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(seconds_played));
		final long min = TimeUnit.SECONDS.toMinutes(seconds_played)
				- TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds_played));
		final long sec = seconds_played - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(1000*seconds_played));
		if(yr == 0) {
			if(mn == 0) {
				if(wk == 0) {
					if(dy == 0) {
						if(hr == 0) {
							if(min == 0) {
								timePlayed = sec + "s";
							}else {
								timePlayed = min + "m " + sec + "s";
							}
						}else {
							timePlayed = hr + "h " + min + "m " + sec + "s";
						}
					}else {
						timePlayed = dy + "d " + hr + "h " + min + "m " + sec + "s";
					}
				}else {
					timePlayed = wk + "w " + dy + "d " + hr + "h " + min + "m " + sec + "s";
				}
			}else {
				timePlayed = mn + "mon " + wk + "w " + dy + "d " + hr + "h " + min + "m " + sec + "s";
			}
		}else { //Full
			timePlayed = yr + "yr " + mn + "mon " + wk + "w " + dy + "d " + hr + "h " + min + "m " + sec + "s";
		}
		return timePlayed;
	}
	
	public double getKdr() {
		if(deaths == 0 && kills != 0) return kills;
		if(deaths != 0 && kills == 0) return 0.00;
		if(deaths == 0 && kills == 0) {
			return 0.00;
		}else {
			this.kdr = (double) kills/deaths;
			DecimalFormat format = new DecimalFormat("#.00");
			kdr = Double.valueOf(format.format(kdr));
		}
		return kdr;
	}
	
	public double getWlr() {
		if(losses == 0 && wins != 0) return wins;
		if(losses != 0 && wins == 0) return 0.00;
		if(losses == 0 && wins == 0) {
			return 0.00;
		}else {
			this.wlr = (double) wins/losses;
			DecimalFormat format = new DecimalFormat("#.00");
			wlr = Double.valueOf(format.format(wlr));
		}
		return wlr;
	}
	
	public void loadFromDB() {
		new BukkitRunnable() { //Async stat-fetching from database

			@Override
			public void run() {
				String uuid = getTableId();
				try {
					DatabaseMetaData dbm = CTFMain.instance.getMainDBConnect().getMetaData();
					ResultSet tables = dbm.getTables(null, null, uuid, null);
					if(tables.next()) { //Player has data, let's fetch it!
						Statement statement = CTFMain.instance.getMainDBConnect().createStatement();
						try {
							ResultSet results = statement.executeQuery("SELECT donor_rank FROM " + uuid + ";"); results.next();
							donor_rank = results.getString("donor_rank");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN donor_rank VARCHAR(10);");
							statement.executeUpdate("UPDATE " + uuid + " SET donor_rank = 'none';");
							donor_rank = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT staff_rank FROM " + uuid + ";"); results.next();
							staff_rank = results.getString("staff_rank");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN staff_rank VARCHAR(10);");
							statement.executeUpdate("UPDATE " + uuid + " SET staff_rank = 'none';");
							staff_rank = "none";
						}
					}else { //Player has no profile, time to make them one
						Statement statement = CTFMain.instance.getMainDBConnect().createStatement();
						statement.execute("CREATE TABLE " + uuid + " (donor_rank VARCHAR(10), staff_rank VARCHAR(10));");
						statement.executeUpdate("INSERT INTO " + uuid + " (donor_rank, staff_rank) VALUES ('none', 'none');");
						donor_rank = "none"; staff_rank = "none";
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					DatabaseMetaData dbm = CTFMain.instance.getDBConnection().getMetaData();
					ResultSet tables = dbm.getTables(null, null, uuid, null);
					if(tables.next()) { //Player has data, let's fetch it!
						Statement statement = CTFMain.instance.getDBConnection().createStatement();
						try {
							ResultSet results = statement.executeQuery("SELECT deaths FROM " + uuid + ";"); results.next();
							deaths = results.getInt("deaths");
						} catch(SQLException ex) { //Doesn't exist?
							ex.printStackTrace();
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN deaths INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET deaths = 0;");
							deaths = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT kills FROM " + uuid + ";"); results.next();
							kills = results.getInt("kills");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN kills INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET kills = 0;");
							kills = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT gold FROM " + uuid + ";"); results.next();
							gold = results.getInt("gold");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN gold INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET gold = 0;");
							gold = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT games FROM " + uuid + ";"); results.next();
							games = results.getInt("games");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN games INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET games = 0;");
							games = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT wins FROM " + uuid + ";"); results.next();
							wins = results.getInt("wins");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN wins INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET wins = 0;");
							wins = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT losses FROM " + uuid + ";"); results.next();
							losses = results.getInt("losses");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN losses INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET losses = 0;");
							losses = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT level FROM " + uuid + ";"); results.next();
							level = results.getInt("level");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN level INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET level = 1;");
							level = 200;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT xp FROM " + uuid + ";"); results.next();
							xp = results.getInt("xp");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN xp INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET xp = 0;");
							xp = 100;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT carrier_kills FROM " + uuid + ";"); results.next();
							carrier_kills = results.getInt("carrier_kills");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN carrier_kills INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET carrier_kills = 0;");
							carrier_kills = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT chall_num FROM " + uuid + ";"); results.next();
							chall_num = results.getInt("chall_num");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN chall_num INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET chall_num = 0;");
							chall_num  = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT captures FROM " + uuid + ";"); results.next();
							captures = results.getInt("captures");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN captures INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET captures = 0;");
							captures = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT seconds_played FROM " + uuid + ";"); results.next();
							seconds_played = results.getLong("seconds_played");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN seconds_played BIGINT(255);");
							statement.executeUpdate("UPDATE " + uuid + " SET seconds_played = 0;");
							seconds_played = 0;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT assists FROM " + uuid + ";"); results.next();
							assists = results.getInt("assists");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN assists INT;");
							statement.executeUpdate("UPDATE " + uuid + " SET assists = 0;");
							assists = 0;
						}
						
						
						
						try {
							ResultSet results = statement.executeQuery("SELECT extraabslot1 FROM " + uuid + ";"); results.next();
							extraAbSlot1 = results.getBoolean("extraabslot1");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN extraabslot1 BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET extraabslot1 = false;");
							extraAbSlot1 = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT extraabslot2 FROM " + uuid + ";"); results.next();
							extraAbSlot2 = results.getBoolean("extraabslot2");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN extraabslot2 BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET extraabslot2 = false;");
							extraAbSlot2 = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT invslot1 FROM " + uuid + ";"); results.next();
							invSlot = results.getBoolean("invslot1");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN invslot1 BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET invslot1 = false;");
							invSlot = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perkslot1 FROM " + uuid + ";"); results.next();
							perkSlot1 = results.getBoolean("perkslot1");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perkslot1 BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET perkslot1 = false;");
							perkSlot1 = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perkslot2 FROM " + uuid + ";"); results.next();
							perkSlot2 = results.getBoolean("perkslot2");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perkslot2 BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET perkslot2 = false;");
							perkSlot2 = false;
						}
						
						try {
							ResultSet results = statement.executeQuery("SELECT ab1 FROM " + uuid + ";"); results.next();
							ab1 = results.getString("ab1");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab1 VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET ab1 = 'none';");
							ab1 = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab2 FROM " + uuid + ";"); results.next();
							ab2 = results.getString("ab2");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab2 VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET ab2 = 'none';");
							ab2 = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab3 FROM " + uuid + ";"); results.next();
							ab3 = results.getString("ab3");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab3 VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET ab3 = 'none';");
							ab3 = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab4 FROM " + uuid + ";"); results.next();
							ab4 = results.getString("ab4");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab4 VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET ab4 = 'none';");
							ab4 = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT invab FROM " + uuid + ";"); results.next();
							invab = results.getString("invab");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN invab VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET invab = 'none';");
							invab = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perk1 FROM " + uuid + ";"); results.next();
							perk1 = results.getString("perk1");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perk1 VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET perk1 = 'none';");
							perk1 = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perk2 FROM " + uuid + ";"); results.next();
							perk2 = results.getString("perk2");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perk2 VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET perk2 = 'none';");
							perk2 = "none";
						}
						
						try {
							ResultSet results = statement.executeQuery("SELECT ab_gladiator FROM " + uuid + ";"); results.next();
							gladiator = results.getBoolean("ab_gladiator");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_gladiator BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_gladiator = false;");
							gladiator = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_massacre FROM " + uuid + ";"); results.next();
							massacre = results.getBoolean("ab_massacre");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_massacre BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_massacre = false;");
							massacre = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_bastion FROM " + uuid + ";"); results.next();
							bastion = results.getBoolean("ab_bastion");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_bastion BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_bastion = false;");
							bastion = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_juggernaut FROM " + uuid + ";"); results.next();
							juggernaut = results.getBoolean("ab_juggernaut");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_juggernaut BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_juggernaut = false;");
							juggernaut = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_xp FROM " + uuid + ";"); results.next();
							abxp = results.getBoolean("ab_xp");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_xp BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_xp = false;");
							abxp = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_fleet FROM " + uuid + ";"); results.next();
							fleet = results.getBoolean("ab_fleet");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_fleet BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_fleet = false;");
							fleet = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_ironskin FROM " + uuid + ";"); results.next();
							ironskin = results.getBoolean("ab_ironskin");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_ironskin BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_ironskin = false;");
							ironskin = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_farshot FROM " + uuid + ";"); results.next();
							farshot = results.getBoolean("ab_farshot");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_farshot BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_farshot = false;");
							farshot = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_lifeblood FROM " + uuid + ";"); results.next();
							lifeblood = results.getBoolean("ab_lifeblood");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_lifeblood BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_lifeblood = false;");
							lifeblood = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_braced FROM " + uuid + ";"); results.next();
							braced = results.getBoolean("ab_braced");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_braced BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_braced = false;");
							braced = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_alchemist FROM " + uuid + ";"); results.next();
							alchemist = results.getBoolean("ab_alchemist");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_alchemist BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_alchemist = false;");
							alchemist = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_berserk FROM " + uuid + ";"); results.next();
							berserk = results.getBoolean("ab_berserk");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_berserk BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_berserk = false;");
							berserk = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_blank FROM " + uuid + ";"); results.next();
							pointblank = results.getBoolean("ab_blank");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_blank BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_blank = false;");
							pointblank = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_leech FROM " + uuid + ";"); results.next();
							leech = results.getBoolean("ab_leech");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_leech BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_leech = false;");
							leech = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT ab_firebrand FROM " + uuid + ";"); results.next();
							firebrand = results.getBoolean("ab_firebrand");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN ab_firebrand BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET ab_firebrand = false;");
							firebrand = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_rod FROM " + uuid + ";"); results.next();
							rod = results.getBoolean("item_rod");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_rod BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_rod = false;");
							rod = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_snow FROM " + uuid + ";"); results.next();
							snow = results.getBoolean("item_snow");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_snow BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_snow = false;");
							snow = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_gapple FROM " + uuid + ";"); results.next();
							gapple = results.getBoolean("item_gapple");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_gapple BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_gapple = false;");
							gapple = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_stick FROM " + uuid + ";"); results.next();
							stick = results.getBoolean("item_stick");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_stick BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_stick = false;");
							stick = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_bandage FROM " + uuid + ";"); results.next();
							bandage = results.getBoolean("item_bandage");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_bandage BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_bandage = false;");
							bandage = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_pearl FROM " + uuid + ";"); results.next();
							pearl = results.getBoolean("item_pearl");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_pearl BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_pearl = false;");
							pearl = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT item_blocks FROM " + uuid + ";"); results.next();
							blocks = results.getBoolean("item_blocks");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN item_blocks BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET item_blocks = false;");
							blocks = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perk_untrackable FROM " + uuid + ";"); results.next();
							untrackable = results.getBoolean("perk_untrackable");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perk_untrackable BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET perk_untrackable = false;");
							untrackable = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perk_haste FROM " + uuid + ";"); results.next();
							haste = results.getBoolean("perk_haste");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perk_haste BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET perk_haste = false;");
							haste = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perk_reinforce FROM " + uuid + ";"); results.next();
							reinforce = results.getBoolean("perk_reinforce");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perk_reinforce BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET perk_reinforce = false;");
							reinforce = false;
						}
						try {
							ResultSet results = statement.executeQuery("SELECT perk_royalty FROM " + uuid + ";"); results.next();
							royalty = results.getBoolean("perk_royalty");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN perk_royalty BOOL;");
							statement.executeUpdate("UPDATE " + uuid + " SET perk_royalty = false;");
							royalty = false;
						}
						
						try {
							ResultSet results = statement.executeQuery("SELECT selected_aura FROM " + uuid + ";"); results.next();
							selected_aura = results.getString("selected_aura");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN selected_aura VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET selected_aura = 'none';");
							selected_aura = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT selected_trail FROM " + uuid + ";"); results.next();
							selected_trail = results.getString("selected_trail");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN selected_trail VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET selected_trail = 'none';");
							selected_trail = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT selected_kill FROM " + uuid + ";"); results.next();
							selected_kill = results.getString("selected_kill");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN selected_kill VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET selected_kill = 'none';");
							selected_kill = "none";
						}
						try {
							ResultSet results = statement.executeQuery("SELECT selected_message FROM " + uuid + ";"); results.next();
							selected_message = results.getString("selected_message");
						} catch(SQLException ex) { //Doesn't exist?
							statement.executeUpdate("ALTER TABLE " + uuid + " ADD COLUMN selected_message VARCHAR(20);");
							statement.executeUpdate("UPDATE " + uuid + " SET selected_message = 'none';");
							selected_message = "none";
						}
					}else { //Player has no profile, time to make them one
						Statement statement = CTFMain.instance.getDBConnection().createStatement();
						statement.execute("CREATE TABLE " + uuid + " (deaths INT, kills INT, gold INT, games INT, wins INT, losses INT, level INT, xp INT, carrier_kills INT, chall_num INT, captures INT, seconds_played BIGINT(255), assists INT, extraabslot1 BOOL, extraabslot2 BOOL, invslot1 BOOL, perkslot1 BOOL, perkslot2 BOOL, ab1 VARCHAR(20), ab2 VARCHAR(20), ab3 VARCHAR(20), ab4 VARCHAR(20), invab VARCHAR(20), perk1 VARCHAR(20), perk2 VARCHAR(20)"
								+ ", ab_gladiator BOOL, ab_massacre BOOL, ab_bastion BOOL, ab_juggernaut BOOL, ab_xp BOOL, ab_fleet BOOL, ab_ironskin BOOL, ab_farshot BOOL, ab_lifeblood BOOL, ab_braced BOOL, ab_alchemist BOOL, ab_berserk BOOL, ab_blank BOOL, ab_leech BOOL, ab_firebrand BOOL, item_rod BOOL, item_snow BOOL, item_gapple BOOL, item_stick BOOL, item_bandage BOOL, item_pearl BOOL, item_blocks BOOL, perk_untrackable BOOL, perk_haste BOOL, perk_reinforce BOOL, perk_royalty BOOL, selected_aura VARCHAR(20), selected_trail VARCHAR(20), selected_kill VARCHAR(20), selected_message VARCHAR(20));");
						statement.executeUpdate("INSERT INTO " + uuid + " (deaths, kills, gold, games, wins, losses, level, xp, carrier_kills, chall_num, captures, seconds_played, assists, extraabslot1, extraabslot2, invslot1, perkslot1, perkslot2, ab1, ab2, ab3, ab4, invab, perk1, perk2, ab_gladiator, ab_massacre, ab_bastion, ab_juggernaut, ab_xp, ab_fleet, ab_ironskin, ab_farshot, ab_lifeblood, ab_braced, ab_alchemist, ab_berserk, ab_blank, ab_leech, ab_firebrand, item_rod, item_snow, item_gapple, item_stick, item_bandage, item_pearl, item_blocks, perk_untrackable, perk_haste, perk_reinforce, perk_royalty, selected_aura, selected_trail, selected_kill, selected_message) "
								+ "VALUES (0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, false, false, false, false, false, 'none', 'none', 'none', 'none', 'none', 'none', 'none', false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, 'none', 'none', 'none', 'none');");
						deaths = 0; kills = 0; gold = 0; games = 0; wins = 0; losses = 0; level = 1; xp = 0; carrier_kills = 0; chall_num = 0; captures = 0; seconds_played = 0; assists = 0; extraAbSlot1 = false; extraAbSlot2 = false; invSlot = false;
						perkSlot1 = false; perkSlot2 = false; ab1 = "none"; ab2 = "none"; ab3 = "none"; ab4 = "none"; invab = "none"; perk1 = "none"; perk2 = "none"; gladiator = false; massacre = false; bastion = false; juggernaut = false; abxp = false; 
						fleet = false; ironskin = false; farshot = false; lifeblood = false; braced = false; alchemist = false; berserk = false; pointblank = false; leech = false; firebrand = false; rod = false; snow = false; gapple = false; stick = false;
						bandage = false; pearl = false; blocks = false; untrackable = false; haste = false; reinforce = false; royalty = false; selected_aura = "none"; selected_trail = "none"; selected_kill = "none"; selected_message = "none";
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}.runTaskAsynchronously(CTFMain.instance); //Async database access
		if(ab1 == null) ab1 = "none";
		if(ab1 == null) ab2 = "none";
		if(ab1 == null) ab3 = "none";
		if(ab1 == null) ab4 = "none";
		if(ab1 == null) invab = "none";
		if(ab1 == null) perk1 = "none";
		if(ab1 == null) perk2 = "none";
	}
	
	public void saveToDB() {
		new BukkitRunnable() { //Async database saving, called on player leave, server shutdown and autosave
			@Override
			public void run() {
				String uuid = getTableId();
				try {
					DatabaseMetaData dbm = CTFMain.instance.getDBConnection().getMetaData();
					ResultSet tables = dbm.getTables(null, null, uuid, null);
					if(tables.next()) { //Player has data, let's save it
						Statement statement = CTFMain.instance.getDBConnection().createStatement();
						statement.executeUpdate("UPDATE " + uuid + " SET kills = " + kills + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET deaths = " + deaths + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET gold = " + gold + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET games = " + games + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET wins = " + wins + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET losses = " + losses + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET level = " + level + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET xp = " + xp + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET carrier_kills = " + carrier_kills + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET chall_num = " + chall_num + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET captures = " + captures + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET seconds_played = " + seconds_played + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET assists = " + assists + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET extraabslot1 = " + extraAbSlot1 + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET extraabslot2 = " + extraAbSlot2 + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET invslot1 = " + invSlot + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET perkslot1 = " + perkSlot1 + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET perkslot2 = " + perkSlot2 + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab1 = '" + ab1 + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET ab2 = '" + ab2 + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET ab3 = '" + ab3 + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET ab4 = '" + ab4 + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET invab = '" + invab + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET perk1 = '" + perk1 + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET perk2 = '" + perk2 + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_gladiator = " + gladiator + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_massacre = " + massacre + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_bastion = " + bastion + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_juggernaut = " + juggernaut + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_xp = " + abxp + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_fleet = " + fleet + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_ironskin = " + ironskin + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_farshot = " + farshot + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_lifeblood = " + lifeblood + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_braced = " + braced + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_alchemist = " + alchemist + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_berserk = " + berserk + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_blank = " + pointblank + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_leech = " + leech + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET ab_firebrand = " + firebrand + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_rod = " + rod + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_snow = " + snow + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_gapple = " + gapple + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_stick = " + stick + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_bandage = " + bandage + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_pearl = " + pearl + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET item_blocks = " + blocks + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET perk_untrackable = " + untrackable + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET perk_haste = " + haste + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET perk_reinforce = " + reinforce + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET perk_royalty = " + royalty + ";");
						statement.executeUpdate("UPDATE " + uuid + " SET selected_aura = '" + selected_aura + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET selected_trail = '" + selected_trail + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET selected_kill = '" + selected_kill + "';");
						statement.executeUpdate("UPDATE " + uuid + " SET selected_message = '" + selected_message + "';");
					}else { //Player has no data, create a table for them -- as this is the save method, this, in theory, should never be called
						Statement statement = CTFMain.instance.getDBConnection().createStatement();
						statement.execute("CREATE TABLE " + uuid + " (deaths INT, kills INT, gold INT, games INT, wins INT, losses INT, level INT, xp INT, carrier_kills INT, chall_num INT, captures INT, seconds_played BIGINT(255), assists INT, extraabslot1 BOOL, extraabslot2 BOOL, invslot1 BOOL, perkslot1 BOOL, perkslot2 BOOL, ab1 VARCHAR(20), ab2 VARCHAR(20), ab3 VARCHAR(20), ab4 VARCHAR(20), invab VARCHAR(20), perk1 VARCHAR(20), perk2 VARCHAR(20)"
								+ ", ab_gladiator BOOL, ab_massacre BOOL, ab_bastion BOOL, ab_juggernaut BOOL, ab_xp BOOL, ab_fleet BOOL, ab_ironskin BOOL, ab_farshot BOOL, ab_lifeblood BOOL, ab_braced BOOL, ab_alchemist BOOL, ab_berserk BOOL, ab_blank BOOL, ab_leech BOOL, ab_firebrand BOOL, item_rod BOOL, item_snow BOOL, item_gapple BOOL, item_stick BOOL, item_bandage BOOL, item_pearl BOOL, item_blocks BOOL, perk_untrackable BOOL, perk_haste BOOL, perk_reinforce BOOL, perk_royalty BOOL, selected_aura VARCHAR(20), selected_trail VARCHAR(20), selected_kill VARCHAR(20), selected_message VARCHAR(20));");
						statement.executeUpdate("INSERT INTO " + uuid + " (deaths, kills, gold, games, wins, losses, level, xp, carrier_kills, chall_num, captures, seconds_played, assists, extraabslot1, extraabslot2, invslot1, perkslot1, perkslot2, ab1, ab2, ab3, ab4, invab, perk1, perk2, ab_gladiator, ab_massacre, ab_bastion, ab_juggernaut, ab_xp, ab_fleet, ab_ironskin, ab_farshot, ab_lifeblood, ab_braced, ab_alchemist, ab_berserk, ab_blank, ab_leech, ab_firebrand, item_rod, item_snow, item_gapple, item_stick, item_bandage, item_pearl, item_blocks, perk_untrackable, perk_haste, perk_reinforce, perk_royalty, selected_aura, selected_trail, selected_kill, selected_message) "
								+ "VALUES (0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, false, false, false, false, false, 'none', 'none', 'none', 'none', 'none', 'none', 'none', false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, 'none', 'none', 'none', 'none');");
						deaths = 0; kills = 0; gold = 0; games = 0; wins = 0; losses = 0; level = 1; xp = 0; carrier_kills = 0; chall_num = 0; captures = 0; seconds_played = 0; assists = 0; extraAbSlot1 = false; extraAbSlot2 = false; invSlot = false;
						perkSlot1 = false; perkSlot2 = false; ab1 = "none"; ab2 = "none"; ab3 = "none"; ab4 = "none"; invab = "none"; perk1 = "none"; perk2 = "none"; gladiator = false; massacre = false; bastion = false; juggernaut = false; abxp = false; 
						fleet = false; ironskin = false; farshot = false; lifeblood = false; braced = false; alchemist = false; berserk = false; pointblank = false; leech = false; firebrand = false; rod = false; snow = false; gapple = false; stick = false;
						bandage = false; pearl = false; blocks = false; untrackable = false; haste = false; reinforce = false; royalty = false; selected_aura = "none"; selected_trail = "none"; selected_kill = "none"; selected_message = "none";
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}.runTaskAsynchronously(CTFMain.instance);
	}

	public int getXpToNext() {
		return xpToNext;
	}

	public void setXpToNext(int xpToNext) {
		this.xpToNext = xpToNext;
	}

	public CompassTracker getCompassTracker() {
		return compassTracker;
	}

	public void setCompassTracker(CompassTracker tracker) {
		compassTracker = tracker;
	}

	//Resets temporary game stats
	public void gameReset() {
		compassTracker = null;
		gold_this_game = 0;
		deaths_this_game = 0;
		kills_this_game = 0;
		captures_this_game = 0;
	}
	
	public boolean getIsSelected(String abilityID) {
		try {
			if(ab1.equalsIgnoreCase(abilityID)) return true;
			if(ab2.equalsIgnoreCase(abilityID)) return true;
			if(ab3.equalsIgnoreCase(abilityID)) return true;
			if(ab4.equalsIgnoreCase(abilityID)) return true;
			if(invab.equalsIgnoreCase(abilityID)) return true;
			if(perk1.equalsIgnoreCase(abilityID)) return true;
			if(perk2.equalsIgnoreCase(abilityID)) return true;
			if(selected_aura.equalsIgnoreCase(abilityID)) return true;
			if(selected_trail.equalsIgnoreCase(abilityID)) return true;
			if(selected_kill.equalsIgnoreCase(abilityID)) return true;
			if(selected_message.equalsIgnoreCase(abilityID)) return true;
		}catch(NullPointerException e) {
			return false;
		}
		return false;
	}
	
	public String getSelected(int num, SlotType type) {
		if(type == SlotType.ABILITY) {
			switch(num) {
			case 1: return ab1;
			case 2: return ab2;
			case 3: return ab3;
			case 4: return ab4;
			default: return null;
			}
		}else if(type == SlotType.INVENTORY) {
			return invab;
		}else if(type == SlotType.PERK) {
			if(num == 1) return perk1; else if(num == 2){return perk2;}
		}
		return null;
	}
	
	public String getSelectedCosmetic(CosmeticType type) {
		switch(type) {
		case AURA: return selected_aura; 
		case TRAIL: return selected_trail; 
		case KILL: return selected_kill;
		case MESSAGE: return selected_message;
		}
		return null;
	}
	
	public void setSelectedCosmetic(String id, CosmeticType type) {
		switch(type) {
		case AURA: selected_aura = id; break;
		case TRAIL: selected_trail = id; break;
		case KILL: selected_kill = id; break;
		case MESSAGE: selected_message = id; break;
		}
	}
	
	public void unlockSlot(SlotType type, int num) {
		if(type == SlotType.ABILITY) {
			switch(num) {
			case 3: extraAbSlot1 = true; return;
			case 4: extraAbSlot2 = true; return;
			default: throw new IllegalArgumentException("Invalid slot number"); 
			}
		}
		if(type == SlotType.INVENTORY) {
			invSlot = true;
			return;
		}
		if(type == SlotType.PERK) {
			switch(num) {
			case 1: perkSlot1 = true; return;
			case 2: perkSlot2 = true; return;
			default: throw new IllegalArgumentException("Invalid slot number");
			}
		}
	}
	
	public boolean getIsSlotUnlocked(int num, SlotType type) {
		if(type == SlotType.ABILITY) {
			switch(num) {
			case 3: return extraAbSlot1;
			case 4: return extraAbSlot2;
			default: return false;
			}
		}else if(type == SlotType.INVENTORY) {
			return invSlot;
		}else if(type == SlotType.PERK) {
			if(num == 1) return perkSlot1; else if(num == 2){return perkSlot2;}
		}
		return false;
	}
	
	public void setSelected(String id, int slot, SlotType type) {
		if(type == SlotType.ABILITY) {
			switch(slot) {
			case 1: ab1 = id; break;
			case 2: ab2 = id; break;
			case 3: ab3 = id; break;
			case 4: ab4 = id; break;
			default: throw new IllegalArgumentException("Invalid slot!");
			}
		}else if(type == SlotType.INVENTORY) {
			invab = id;
		}else if(type == SlotType.PERK) {
			if(slot == 1) perk1 = id; else {perk2 = id;}
		}
	}
	
	public boolean getIsUnlocked(String id) {
		switch (id) {
		case "ability_gladiator": return gladiator;
		case "ability_massacre": return massacre;
		case "ability_bastion": return bastion;
		case "ability_juggernaut": return juggernaut;
		case "ability_xp": return abxp;
		case "ability_fleet": return fleet;
		case "ability_ironskin": return ironskin;
		case "ability_farshot": return farshot;
		case "ability_lifeblood": return lifeblood;
		case "ability_braced": return braced;
		case "ability_alchemist": return alchemist;
		case "ability_berserk": return berserk;
		case "ability_blank": return pointblank;
		case "ability_leech": return leech;
		case "ability_firebrand": return firebrand;
		case "item_rod": return rod;
		case "item_snow": return snow;
		case "item_gapple": return gapple;
		case "item_stick": return stick;
		case "item_bandage": return bandage;
		case "item_pearl": return pearl;
		case "item_blocks": return blocks;
		case "perk_untrackable": return untrackable;
		case "perk_haste": return haste;
		case "perk_reinforce": return reinforce;
		case "perk_royalty": return royalty;
		}
		return false;
	}
	
	public void unlock(String id) {
		switch(id) {
		case "ability_gladiator": gladiator = true; break;
		case "ability_massacre": massacre = true; break;
		case "ability_bastion": bastion = true; break;
		case "ability_juggernaut": juggernaut = true; break;
		case "ability_xp": abxp = true; break;
		case "ability_fleet": fleet = true; break;
		case "ability_ironskin": ironskin = true; break;
		case "ability_farshot": farshot = true; break;
		case "ability_lifeblood": lifeblood = true; break;
		case "ability_braced": braced = true; break;
		case "ability_alchemist": alchemist = true; break;
		case "ability_berserk": berserk = true; break;
		case "ability_blank": pointblank = true; break;
		case "ability_leech": leech = true; break;
		case "ability_firebrand": firebrand = true; break;
		case "item_rod": rod = true; break;
		case "item_snow": snow = true; break;
		case "item_gapple": gapple = true; break;
		case "item_stick": stick = true; break;
		case "item_bandage": bandage = true; break;
		case "item_pearl": pearl = true; break;
		case "item_blocks": blocks = true; break;
		case "perk_untrackable": untrackable = true; break;
		case "perk_haste": haste = true; break;
		case "perk_reinforce": reinforce = true; break;
		case "perk_royalty": royalty = true; break;
		}
	}
	
	public String getColoredName() {
		if(TeamHandler.redTeam.contains(p.getUniqueId())) {
			return ChatColor.RED + p.getName() + ChatColor.RESET;
		}else if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
			return ChatColor.BLUE + p.getName() + ChatColor.RESET;
		}else {
			return p.getName();
		}
	}

	public String getStaffRank() {
		return staff_rank;
	}
	
	public String getDonorRank() {
		return donor_rank;
	}
	
	public String getRankPrefix() {
		String prefix = "";
		if(!staff_rank.equalsIgnoreCase("none")) { //Staff tags override donor tags
			if(staff_rank.equals("owner")) {
				prefix = "" + ChatColor.RED + ChatColor.BOLD + "OWNER";
			} else if(staff_rank.equals("ownerdev")) {
				prefix = "" + ChatColor.RED + ChatColor.BOLD + "OWNER/DEV" + ChatColor.RESET;
			}else if(staff_rank.equals("builder")) {
				prefix = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "BUILDER";
			}else if(staff_rank.equals("mod")) {
				prefix = "" + ChatColor.GREEN + ChatColor.BOLD + "MOD";
			}else if(staff_rank.equals("headmod")) {
				prefix = "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "HEAD MOD";
			}else if(staff_rank.equals("developer")) {
				prefix = "" + ChatColor.RED + ChatColor.BOLD + "DEVELOPER";
			}
		}else {
			if(donor_rank.equals("eta")) {
				prefix = "" + ChatColor.YELLOW + ChatColor.BOLD + "ETA";
			}else if(donor_rank.equals("theta")) {
				prefix = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "THETA";
			}else if(donor_rank.equals("zeta")) {
				prefix = "" + ChatColor.GOLD + ChatColor.BOLD + "ZETA";
			}else if(donor_rank.equals("zetaplus")) {
				prefix = "" + ChatColor.GOLD + ChatColor.BOLD + "ZETA" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "+"; //TODO -- Custom Pluses
			}
		}
		return prefix;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
		if(!isDead) { //Vanish failsafe -- TODO make sure mods don't get unvanished!
			p.setGameMode(GameMode.SURVIVAL);
		}else {
			p.setGameMode(GameMode.SPECTATOR);
		}
	}

	public AuraEffect getEffect() {
		return effect;
	}

	public void setEffect(AuraEffect effect) {
		if(getEffect() != null) this.effect.disable(); //Disable previous if exists
		this.effect = effect;
	}
	
	public String getTableId() {
		return "player" + puid.toString().replaceAll("-", "_");
	}

	public String getFriendId() {
		return "friend" + puid.toString().replaceAll("-", "_");
	}
	
	public ArrayList<UUID> getLastHitters() {
		return last_hitters;
	}
	
	public void clearLastHitters() {
		last_hitters.clear();
	}
	
	public void addHitter(UUID id) {
		if(!last_hitters.contains(id)) last_hitters.add(id);
	}
	
	//Returns success
	public boolean removeHitter(UUID id) {
		if(last_hitters.contains(id)) {
			last_hitters.remove(id);
			return true;
		}
		return false;
	}

	public boolean isBerserked() {
		return berserked;
	}

	public void setBerserked(boolean berserked) {
		this.berserked = berserked;
	}

}
