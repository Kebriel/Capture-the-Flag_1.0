package kebriel.ctf.util;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import kebriel.ctf.CTFMain;

public class HologramNMS {
	
	private Location loc;
	private String name = null;
	private EntityArmorStand stand;
	Player p; //The player who will see it
	
	public HologramNMS(Location loc, String name, Player p) {
		this.loc = loc;
		this.name = name;
		this.p = p;
		stand = new EntityArmorStand(((CraftWorld)CTFMain.instance.getWorld()).getHandle());
	}
	
	public HologramNMS(Location loc, Player p) {
		this.loc = loc;
		this.p = p;
		stand = new EntityArmorStand(((CraftWorld)CTFMain.instance.getWorld()).getHandle());
	}
	
	public void spawn() {
		stand.setCustomName(name);
		stand.setCustomNameVisible(true);
		stand.setInvisible(true);
		stand.setPosition(Math.floor(loc.getX()) + 0.5, Math.floor(loc.getY()), Math.floor(loc.getZ()) + 0.5);
		
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(stand);
		
		//Async packet sending
		new BukkitRunnable() {

			@Override
			public void run() {
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			}
			
		}.runTaskLaterAsynchronously(CTFMain.instance, 1);
	}
	
	public void teleport(Location loc) {
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(
				stand.getId(), (int) Math.floor(loc.getX()), (int) Math.floor(loc.getY()), (int) Math.floor(loc.getZ()), (byte) loc.getYaw(), (byte) (loc.getPitch()), false);
		//Async packet sending
		new BukkitRunnable() {
			@Override
			public void run() {
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			}
		}.runTaskLaterAsynchronously(CTFMain.instance, 1);
	}
	
	public void delete() {
		PacketPlayOutEntityDestroy remove = new PacketPlayOutEntityDestroy(stand.getId());
		//Async packet sending
				new BukkitRunnable() {
					@Override
					public void run() {
						((CraftPlayer)p).getHandle().playerConnection.sendPacket(remove);
					}
				}.runTaskLaterAsynchronously(CTFMain.instance, 1);
	}
	
	public EntityArmorStand getEntity() {
		return stand;
	}
	
	public Location getLoc() {
		return loc;
	}
	
	public Player getSeer() {
		return this.p;
	}

}
