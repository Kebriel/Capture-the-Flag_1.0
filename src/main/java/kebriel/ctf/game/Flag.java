package kebriel.ctf.game;

import java.util.ArrayList;

import kebriel.ctf.display.ScoreboardSidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import kebriel.ctf.CTFMain;
import kebriel.ctf.MapManager;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ActionBarUtil;
import kebriel.ctf.util.Hologram;
import kebriel.ctf.util.HologramNMS;
import kebriel.ctf.util.MessageUtil;
import kebriel.ctf.util.TitleUtil;

public class Flag {
	
	private Location spawn;
	private Location current_loc;
	private String team;
	private FlagState state;
	private Hologram holo;
	private Hologram flag;
	private Player holder;
	
	private ArrayList<HologramNMS> lines = new ArrayList<HologramNMS>();
	private ArrayList<Hologram> clickable = new ArrayList<Hologram>();
	
	private BukkitTask action_runnable = null;
	
	public Flag(String team) {
		this.team = team;
		if(team.equalsIgnoreCase("red")) {
			this.spawn = MapManager.getCurrent().getRedFlag();
			this.current_loc = spawn;
			this.state = FlagState.SPAWN;
		}else if(team.equalsIgnoreCase("blue")) {
			this.spawn = MapManager.getCurrent().getBlueFlag();
			this.current_loc = spawn;
			this.state = FlagState.SPAWN;
		}else {
			throw new IllegalArgumentException();
		}
	}
	
	public void spawn() {
		Hologram holo;
		current_loc.getChunk().load();
		if(team.equalsIgnoreCase("red")) {
			holo = new Hologram(current_loc, "" + ChatColor.RED + ChatColor.BOLD + "RED FLAG", true);
		}else {
			holo = new Hologram(current_loc, "" + ChatColor.BLUE + ChatColor.BOLD + "BLUE FLAG", true);
		}
		holo.spawn();
		this.holo = holo;
		
		Hologram click1 = new Hologram(current_loc.clone().add(0.5, 0, 0), team + "clickable", false); click1.spawn(); clickable.add(click1);
		Hologram click2 = new Hologram(current_loc.clone().add(-0.5, 0, 0), team + "clickable", false); click2.spawn(); clickable.add(click2);
		Hologram click3 = new Hologram(current_loc.clone().add(0, 0, 0.5), team + "clickable", false); click3.spawn(); clickable.add(click3);
		Hologram click4 = new Hologram(current_loc.clone().add(0, 0, -0.5), team + "clickable", false); click4.spawn(); clickable.add(click4);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			Location loc = current_loc.clone();
			loc.setY(loc.getY()-0.25);
			HologramNMS nms;
			if(TeamHandler.redTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("red")) {
				if(this.state == FlagState.MISSING) {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Click to recover", p);
				} else {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Your flag", p);
				}
			}else if(TeamHandler.blueTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("blue")){
				if(this.state == FlagState.MISSING) {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Click to recover", p);
				} else {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Your flag", p);
				}
			}else {
				nms = new HologramNMS(loc, ChatColor.YELLOW + "Click to capture", p);
			}
			nms.spawn();
			lines.add(nms);
		}
		Location loc = current_loc.clone();
		loc.setY(loc.getY()-1.75);
		
		ItemStack banner = new ItemStack(Material.BANNER);
		BannerMeta meta = (BannerMeta) banner.getItemMeta();
		if(team.equalsIgnoreCase("red")) {
			meta.setBaseColor(DyeColor.RED);
			flag = new Hologram(loc, "redflag", false);
		}else {
			meta.setBaseColor(DyeColor.BLUE);
			flag = new Hologram(loc, "blueflag", false);
		}
		banner.setItemMeta(meta);
		
		flag.spawn();
		flag.addHat(banner);
		//Render animation if 'missing'
		if(this.state == FlagState.MISSING) {
			new BukkitRunnable() {

				@Override
				public void run() {
					if(holo.isAlive()) { //If it's dropped
						Location newLoc = flag.getLocation();
						newLoc.setYaw(newLoc.getYaw()+2);
						flag.get().teleport(newLoc);
					}else {
						this.cancel();
					}
				}
				
			}.runTaskTimer(CTFMain.instance, 0, 1);
		}
	}
	
	public void pickup(Player p) { //A player takes it
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(prof.isDead()) return;
		if(p.getGameMode() == GameMode.SPECTATOR) return;
		if(prof.getIsSelected("ability_fleet")) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0, true, false));
			if(TeamHandler.redTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("red")) p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 0, true, false));
			if(TeamHandler.blueTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("blue")) p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 0, true, false));
		}
		
		if(this.state == FlagState.SPAWN) { //A player took it from a spawn
			this.remove();
			this.state = FlagState.MISSING;
			this.holder = p;
			this.renderFlagCarrier(p);
			
			if(team.equalsIgnoreCase("red")) { //Red flag was taken
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(TeamHandler.redTeam.contains(player.getUniqueId())) {
						MessageUtil.sendToPlayer(player, ChatColor.RED + "Your flag was taken by " + ChatColor.BLUE + p.getName() + ChatColor.RED + "!");
						player.playSound(player.getLocation(), Sound.GHAST_SCREAM2, 1.2f, 1.5f);
						TitleUtil.sendTitleToPlayer(player, 0, 50, 5, ChatColor.RED + "Flag Taken!", ChatColor.RED + "Your flag was taken by " + ChatColor.BLUE + p.getName());
					}else {
						if(player.getUniqueId() != p.getUniqueId()) {
							MessageUtil.sendToPlayer(player, ChatColor.RED + "Red Flag " + ChatColor.GREEN + "was taken by " + ChatColor.BLUE + p.getName() + ChatColor.GREEN + "!");
							player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.5f, 1.5f);
							TitleUtil.sendTitleToPlayer(player, 0, 50, 5, ChatColor.GREEN + "Flag Taken!", ChatColor.RED + "Red Flag " + ChatColor.GREEN + "was taken by " + ChatColor.BLUE + p.getName());
						}else {
							MessageUtil.sendToPlayer(player, ChatColor.GREEN + "You took " + ChatColor.RED + "Red Flag" + ChatColor.GREEN + "!");
							player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1.1f, 1.5f);
							TitleUtil.sendTitleToPlayer(player, 0, 50, 5, "" + ChatColor.GREEN + ChatColor.BOLD + "Flag Taken!", ChatColor.GREEN + "Return it to your base!");
						}
					}
				}
			}else { //Blue
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(TeamHandler.blueTeam.contains(player.getUniqueId())) {
						MessageUtil.sendToPlayer(player, ChatColor.RED + "Your flag was taken by " + ChatColor.RED + ChatColor.BOLD + p.getName() + ChatColor.RED + "!");
						player.playSound(player.getLocation(), Sound.GHAST_SCREAM2, 1.2f, 1.5f);
						TitleUtil.sendTitleToPlayer(player, 0, 50, 5, ChatColor.RED + "Flag Taken!", ChatColor.RED + "Your flag was taken by " + ChatColor.RED + ChatColor.BOLD + p.getName());
					}else {
						if(player.getUniqueId() != p.getUniqueId()) {
							MessageUtil.sendToPlayer(player, ChatColor.BLUE + "Blue Flag " + ChatColor.GREEN + "was taken by " + ChatColor.RED + p.getName() + ChatColor.GREEN + "!");
							player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.5f, 1.5f);
							TitleUtil.sendTitleToPlayer(player, 0, 50, 5, ChatColor.GREEN + "Flag Taken!", ChatColor.BLUE + "Blue Flag " + ChatColor.GREEN + "was taken by " + ChatColor.RED + p.getName());
						}else {
							MessageUtil.sendToPlayer(player, ChatColor.GREEN + "You took " + ChatColor.BLUE + "Blue Flag" + ChatColor.GREEN + "!");
							player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 1.1f, 1.5f);
							TitleUtil.sendTitleToPlayer(player, 0, 50, 5, "" + ChatColor.GREEN + ChatColor.BOLD + "Flag Taken!", ChatColor.GREEN + "Return it to your base!");
						}
					}
				}
			}
		}else { //They found it on the ground
			this.remove();
			this.holder = p;
			renderFlagCarrier(p);
			if(TeamHandler.redTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("red")) { //Red got its flag
				this.state = FlagState.RECOVERED;
				MessageUtil.sendToLobby(ChatColor.RED + "Red Flag" + ChatColor.GREEN + " was picked up by " + ChatColor.RED + ChatColor.BOLD + p.getName());
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
			}else if(TeamHandler.blueTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("blue")) { //Blue got its flag
				this.state = FlagState.RECOVERED;
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
				MessageUtil.sendToLobby(ChatColor.BLUE + "Blue Flag" + ChatColor.GREEN + " was picked up by " + ChatColor.BLUE + p.getName());
			}else {
				this.state = FlagState.MISSING;
				if(team.equalsIgnoreCase("red") && TeamHandler.blueTeam.contains(p.getUniqueId())) {
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
					MessageUtil.sendToLobby("" + ChatColor.RED + ChatColor.BOLD + "Red Flag" + ChatColor.RED + " was picked up by " + ChatColor.BLUE + p.getName());
				}else if(team.equalsIgnoreCase("blue") && TeamHandler.redTeam.contains(p.getUniqueId())){
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
					MessageUtil.sendToLobby(ChatColor.BLUE + "Blue Flag" + ChatColor.RED + " was picked up by " + ChatColor.RED + ChatColor.BOLD + p.getName());
				}
			}
			
		}
		ScoreboardSidebar.gameBoard();
		
		new BukkitRunnable() {
			int timer = 0;
			Player pl = holder;
			@Override
			public void run() {
				if(getState() == FlagState.MISSING || getState() == FlagState.RECOVERED) {
					timer++;
					if(holder == null) {
						this.cancel();
						return;
					}
					if(holder.getUniqueId() != pl.getUniqueId()) {
						this.cancel();
						return;
					}
					if(timer == 180) {
						returnFlag();
						if(team.equalsIgnoreCase("red")) {
							MessageUtil.sendToLobby(ChatColor.AQUA + "The " + ChatColor.RED + "Red Flag " + ChatColor.AQUA + "has respawned!");
						}else {
							MessageUtil.sendToLobby(ChatColor.AQUA + "The " + ChatColor.BLUE + "Blue Flag " + ChatColor.AQUA + "has respawned!");
						}
						this.cancel();
						return;
					}
				}else {
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 20);
	}
	
	public void returnFlag() {
		this.state = FlagState.SPAWN;
		this.current_loc = spawn;
		spawn();
		ScoreboardSidebar.gameBoard();
		this.derenderFlagCarrier();
	}
	
	public void drop() { //A player died or left with it
		this.current_loc = this.holder.getLocation();
		this.derenderFlagCarrier();
		this.state = FlagState.MISSING;
		this.spawn();
		if(team.equalsIgnoreCase("red")) {
			MessageUtil.sendToLobby(ChatColor.GRAY + "The " + ChatColor.RED + "Red Flag" + ChatColor.GRAY + " was dropped!");
		}else {
			MessageUtil.sendToLobby(ChatColor.GRAY + "The " + ChatColor.BLUE + "Blue Flag" + ChatColor.GRAY + " was dropped!");
		}
		ScoreboardSidebar.gameBoard();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(flag.isAlive() && getState() == FlagState.MISSING) {
					returnFlag();
				}
			}
			
		}.runTaskLater(CTFMain.instance, 2400);
	}
	
	public void capture() { //Called if enter spawn with this flag
		this.state = FlagState.SPAWN;
		this.current_loc = spawn;
		spawn();
		this.derenderFlagCarrier();
	}
	
	public void renderForPlayer(Player p) {
		if(holder == null) {
			Location loc = current_loc.clone();
			loc.setY(loc.getY()-0.25);
			HologramNMS nms;
			if(TeamHandler.redTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("red")) {
				if(this.state == FlagState.MISSING) {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Click to recover", p);
				} else {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Your flag", p);
				}
			}else if(TeamHandler.blueTeam.contains(p.getUniqueId()) && team.equalsIgnoreCase("blue")){
				if(this.state == FlagState.MISSING) {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Click to recover", p);
				} else {
					nms = new HologramNMS(loc, ChatColor.GREEN + "Your flag", p);
				}
			}else {
				nms = new HologramNMS(loc, ChatColor.YELLOW + "Click to capture", p);
			}
			nms.spawn();
			lines.add(nms);
		}
	}
	
	public void rerender() {
		for(HologramNMS line : lines) {
			line.delete();
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			renderForPlayer(p);
		}
	}
	
	public void remove() {
		this.holo.remove();
		flag.remove();
		for(HologramNMS nms : lines) {
			nms.delete();
		}
		for(Hologram holo : clickable) {
			holo.remove();
		}
		lines.clear();
	}
	
	public void renderFlagCarrier(Player p) {
		ItemStack banner = new ItemStack(Material.BANNER);
		BannerMeta meta = (BannerMeta) banner.getItemMeta();
		if(team.equalsIgnoreCase("red")) {
			meta.setBaseColor(DyeColor.RED);
		}else {
			meta.setBaseColor(DyeColor.BLUE);
		}
		banner.setItemMeta(meta);
		action_runnable = new BukkitRunnable() {
			@Override
			public void run() {
				if(holder != null) {
					if(team.equalsIgnoreCase("red")) {
						new ActionBarUtil("" + ChatColor.GREEN + "Currently holding: " + ChatColor.RED + ChatColor.BOLD + "RED FLAG").print(p);
					}else {
						new ActionBarUtil("" + ChatColor.GREEN + "Currently holding: " + ChatColor.BLUE + ChatColor.BOLD + "BLUE FLAG").print(p);
					}
				}else {
					this.cancel();
				}
			}
		}.runTaskTimer(CTFMain.instance, 0, 20);
		new BukkitRunnable() {
			@Override
			public void run() {
				if(holder != null) {
						for(Player ps : Bukkit.getOnlinePlayers()) {
							PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(
									((CraftPlayer)holder).getHandle().getId(), 4, CraftItemStack.asNMSCopy(banner));
									((CraftPlayer)ps).getHandle().playerConnection.sendPacket(packet);
						}
				}else {
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 1);
		}
	
	public void derenderFlagCarrier() {
		if(action_runnable != null) {
			action_runnable.cancel();
		}
		new ActionBarUtil(" ").print(holder);
			for(Player p : Bukkit.getOnlinePlayers()) {
				PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment(
				((CraftPlayer)holder).getHandle().getId(), 4, CraftItemStack.asNMSCopy(holder.getInventory().getHelmet()));
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet2);
			}
		this.holder = null;
	}
	
	public Location getCurrentLoc() {
		return current_loc;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setCurrentLoc(Location loc) {
		this.current_loc = loc;
	}
	
	public String getTeam() {
		return team;
	}

	public FlagState getState() {
		return state;
	}
	
	public void wipeHolder() {
		if(holder != null) {
			derenderFlagCarrier();
		}
		this.holder = null;
	}
	
	public Player getHolder() {
		return holder;
	}
}
