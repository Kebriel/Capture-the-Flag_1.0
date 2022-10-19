package sylaires.ctf.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import sylaires.ctf.CTFMain;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class ParticleUtil {
	
	static Plugin plugin = CTFMain.instance;

	public static void playParticles(EnumParticle type, Location loc, double xoffset, double yoffset, double zoffset, double speed, int amount, int data) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type, true, (float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), (float) xoffset, (float) yoffset, (float) zoffset, (float) speed, amount, data);
		for(Player p: Bukkit.getOnlinePlayers()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void playParticles(EnumParticle type, Location loc, double xoffset, double yoffset, double zoffset, double speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type, true, (float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), (float) xoffset, (float) yoffset, (float) zoffset, (float) speed, amount, null);
		for(Player p: Bukkit.getOnlinePlayers()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void playParticlesFor(Player p, EnumParticle type, Location loc, double xoffset, double yoffset, double zoffset, double speed, int amount, int data) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type, true, (float) (loc.getX()), (float) (loc.getY()), (float) (loc.getZ()), (float) xoffset, (float) yoffset, (float) zoffset, (float) speed, amount, data);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

}
