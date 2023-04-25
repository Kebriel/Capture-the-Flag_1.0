package kebriel.ctf.display;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_8_R3.WorldServer;
import kebriel.ctf.CTFMain;
import kebriel.ctf.util.Hologram;

public class NPCSpawn {
	
	private String name;
	private Location loc;
	private UUID id;
	private Hologram tag;
	private ArrayList<Hologram> clickable = new ArrayList<Hologram>();
	private EntityPlayer npc;
	private GameProfile profile;
	
	private NPCSpawn(Location loc) {
		
		id = UUID.fromString("c7fddb9a-adfd-4be3-b326-d700df252e88");
		name = "[NPC] " + id.toString().replace("-", "").substring(0, 6);
		this.loc = loc;
	}
	
	public void loadSkin() {
		JSONParser parser = new JSONParser();
		profile = new GameProfile(id, name);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("https://sessionserver.mojang.com/session/minecraft/profile/c7fddb9aadfd4be3b326d700df252e88?unsigned=false").openConnection();
	        JSONArray response = (JSONArray)((JSONObject) parser.parse(new InputStreamReader(connection.getInputStream()))).get("properties");
	        JSONObject a = (JSONObject) response.get(0); 
	        profile.getProperties().put("textures", new Property("textures", (String) a.get("value"), (String) a.get("signature")));
		} catch (Exception e) {
			Bukkit.getLogger().info("[WARN] Query to Mojang for skins failed!");
		}
	}
	
	public void spawn() {
		loc.getChunk().load();
		loadSkin();
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
		npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
		npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
		npc.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), 0);
		tag = new Hologram(loc, "" + ChatColor.GOLD + ChatColor.BOLD + "Abilities", true);
		tag.addLine("Manage abilities");
		tag.spawn();
		Hologram click1 = new Hologram(loc.clone().add(0.5, 0, 0), "clickable", false); click1.spawn(); clickable.add(click1);
		Hologram click2 = new Hologram(loc.clone().add(-0.5, 0, 0), "clickable", false); click2.spawn(); clickable.add(click2);
		Hologram click3 = new Hologram(loc.clone().add(0, 0, 0.5), "clickable", false); click3.spawn(); clickable.add(click3);
		Hologram click4 = new Hologram(loc.clone().add(0, 0, -0.5), "clickable", false); click4.spawn(); clickable.add(click4);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			Scoreboard board = new Scoreboard();
			String teamname = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
			ScoreboardTeam hide = board.createTeam(teamname);
			hide.setNameTagVisibility(EnumNameTagVisibility.NEVER);
			board.addPlayerToTeam(name, teamname);
			PlayerConnection connect = ((CraftPlayer)p).getHandle().playerConnection;
			connect.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
			connect.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connect.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)loc.getYaw()));
			connect.sendPacket(new PacketPlayOutScoreboardTeam(hide, 0));
			
			new BukkitRunnable() {

				@Override
				public void run() {
					connect.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
				}
				
			}.runTaskLater(CTFMain.instance, 20);
		}
	}
	
	public void renderForPlayer(Player p) {
		Scoreboard board = new Scoreboard();
		String teamname = UUID.randomUUID().toString().replace("-", "").substring(0, 7);
		ScoreboardTeam hide = board.createTeam(teamname);
		hide.setNameTagVisibility(EnumNameTagVisibility.NEVER);
		board.addPlayerToTeam(name, teamname);
		PlayerConnection connect = ((CraftPlayer)p).getHandle().playerConnection;
		connect.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
		connect.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
		connect.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)loc.getYaw()));
		connect.sendPacket(new PacketPlayOutScoreboardTeam(hide, 0));
		
		new BukkitRunnable() {
			@Override
			public void run() {
				connect.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
			}
		}.runTaskLater(CTFMain.instance, 10);
	}
	
	public void rerender() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
			renderForPlayer(p);
		}
	}
	
	public void remove() {
		tag.remove();
		for(Hologram holo : clickable) {
			holo.remove();
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
		}
	}
	
	public static NPCSpawn getNew(Location loc) {
		return new NPCSpawn(loc);
	}

}
