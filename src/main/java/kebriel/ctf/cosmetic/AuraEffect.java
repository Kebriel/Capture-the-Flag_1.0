package kebriel.ctf.cosmetic;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EnumParticle;
import kebriel.ctf.CTFMain;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ParticleUtil;

public class AuraEffect {
	
	BukkitTask run;
	private Location loc;
	
	public AuraEffect(Player p, String id) {
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(id.equalsIgnoreCase("aura_flaming")) {
			run = new BukkitRunnable() {
				Random rand = new Random();
				int delay = 60;
				float radius1 = 1.4f;
				float angle1 = 0.0f;
				float yoffset1 = 0;
				float radius2 = 1.2f;
				float angle2 = 0.0f;
				float yoffset2 = 0;
				float radius3 = 1f;
				float angle3 = 0.0f;
				float yoffset3 = 0;
				
				boolean downwards1 = false;
				boolean upwards1 = false;
				
				boolean downwards2 = false;
				boolean upwards2 = false;
				
				boolean downwards3 = false;
				boolean upwards3 = false;
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if(!prof.isDead()) {
						try {
							if(p.getLocation().getX() != loc.getX() || p.getLocation().getY() != loc.getY() || p.getLocation().getZ() != loc.getZ()) {
								if(p.isOnGround()) {
									//They moved, stop applying aura
									ParticleUtil.playParticles(EnumParticle.FLAME, p.getLocation(), 0.4, 0.3, 0.4, 0, 1);
									ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, p.getLocation(), 0.4, 0.3, 0.4, 0, 1);
								}
							}else {
								if(p.isOnGround()) {
									Location loc1 = p.getLocation();
									double x1 = (radius1 * Math.sin(angle1));
									double z1 = (radius1 * Math.cos(angle1));
									int random1 = rand.nextInt(100);
									if(!downwards1 && !upwards1) {
										if(random1 == 1) { //Begin cycle
											upwards1 = true;
										}
										
										if(yoffset1 >= 1) { //Only do this much extra when not curving
											yoffset1 += (rand.nextDouble()*-1); 
										}else {
											yoffset1 += rand.nextDouble(); 
										}
										if(yoffset1 >= 0.6) {
											yoffset1 = 0;
										}
									}
									if(upwards1) {
										yoffset1+=0.08;
									}
									if(downwards1) {
										yoffset1-=0.08;
									}
									if(yoffset1 > 2) { //Go back down
										upwards1 = false;
										downwards1 = true;
										yoffset1 = 2;
									}
									if(yoffset1 < 0) { //End cycle
										yoffset1 = 0;
										downwards1 = false;
									}
									angle1 += 0.1;
									loc1.add(x1, yoffset1, z1);
				                    ParticleUtil.playParticles(EnumParticle.FLAME, loc1, 0, 0, 0, 0, 1);
				                    if(delay <= 30) {
				                    	Location loc2 = p.getLocation();
										double x2 = (radius2 * Math.sin(angle2));
										double z2 = (radius2 * Math.cos(angle2));
										int random2 = rand.nextInt(100);
										if(!downwards2 && !upwards2) {
											if(random2 == 1) { //Begin cycle
												upwards2 = true;
											}
											
											if(yoffset2 >= 1) { //Only do this much extra when not curving
												yoffset2 += (rand.nextDouble()*-1); 
											}else {
												yoffset2 += rand.nextDouble(); 
											}
											if(yoffset2 >= 0.6) {
												yoffset2 = 0;
											}
										}
										if(upwards2) {
											yoffset2+=0.08;
										}
										if(downwards2) {
											yoffset2-=0.08;
										}
										if(yoffset2 > 2) { //Go back down
											upwards2 = false;
											downwards2 = true;
											yoffset2 = 2;
										}
										if(yoffset2 < 0) { //End cycle
											yoffset2 = 0;
											downwards2 = false;
										}
										angle2 += 0.1;
										loc2.add(x2, yoffset2, z2);
					                    ParticleUtil.playParticles(EnumParticle.FLAME, loc2, 0, 0, 0, 0, 1);
				                    }
				                    if(delay == 0) {
				                    	Location loc3 = p.getLocation();
										double x3 = (radius3 * Math.sin(angle3));
										double z3 = (radius3 * Math.cos(angle3));
										int random3 = rand.nextInt(100);
										if(!downwards3 && !upwards3) {
											if(random3 == 1) { //Begin cycle
												upwards3 = true;
											}
											
											if(yoffset3 >= 1) { //Only do this much extra when not curving
												yoffset3 += (rand.nextDouble()*-1); 
											}else {
												yoffset3 += rand.nextDouble(); 
											}
											if(yoffset3 >= 0.6) {
												yoffset3 = 0;
											}
										}
										if(upwards3) {
											yoffset3+=0.08;
										}
										if(downwards3) {
											yoffset3-=0.08;
										}
										if(yoffset3 > 2) { //Go back down
											upwards3 = false;
											downwards3 = true;
											yoffset3 = 2;
										}
										if(yoffset3 < 0) { //End cycle
											yoffset3 = 0;
											downwards3 = false;
										}
										angle3 += 0.1;
										loc3.add(x3, yoffset3, z3);
					                    ParticleUtil.playParticles(EnumParticle.FLAME, loc3, 0, 0, 0, 0, 1);
					                    
				                    }else {
				                    	delay--;
				                    }
								}
							}
						} catch(NullPointerException e) {}
						loc = p.getLocation();
					}
				}
				
			}.runTaskTimer(CTFMain.instance, 0, 1);
		}else if(id.equalsIgnoreCase("aura_void")) {
			run = new BukkitRunnable() {
				int timer = 240;
				
				float radius = 0.2f;
				float angle1 = 0;
				float angle2 = 0;
				float angle3 = 0;
				float angle4 = 0;
				
				boolean inwards = false;
				boolean outwards = false;
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if(!prof.isDead()) {
						try {
							if(p.getLocation().getX() != loc.getX() || p.getLocation().getY() != loc.getY() || p.getLocation().getZ() != loc.getZ()) {
								if(p.isOnGround()) {
									//They moved, stop applying aura
									ParticleUtil.playParticles(EnumParticle.REDSTONE, p.getLocation(), 0.0001, 0.0001, 0.0001, 1, 0);
									ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, p.getLocation(), 0.2, 0, 0.2, 0.01, 1);
								}
							}else {
								if(p.isOnGround()) {
									Location loc1 = p.getLocation();
									Location loc2 = p.getLocation();
									Location loc3 = p.getLocation();
									Location loc4 = p.getLocation();
									
									ParticleUtil.playParticles(EnumParticle.SUSPENDED_DEPTH, p.getLocation(), 0.5, 1, 0.5, 0, 3);
									ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, p.getLocation(), 0.5, 1, 0.5, 0, 1);
									
									if(radius <= 0.2f) {
										outwards = true;
										inwards = false;
									}
									if(radius >= 1.5f) {
										outwards = false;
										inwards = true;
										float rad = 1.5f;
										for(double a = 0; a < 100; a+=1) {
											Location circle = p.getLocation();
											double x = (rad*Math.sin(a));
											double z = (rad*Math.cos(a));
											circle.add(x, 0, z);
											ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, circle, 0, 0, 0, 0, 1);
											ParticleUtil.playParticles(EnumParticle.REDSTONE, circle, 0.0001, 0.0001, 0.0001, 1, 0);
										}
									}
									if(inwards) {
										radius-=0.01f;
									}
									if(outwards) {
										radius+=0.01f;
									}
									
									if(timer <= 240) {
										double x = (radius*Math.sin(angle4));
										double z = (radius*Math.cos(angle4));
										loc4.add(x, 0, z);
										angle4+=0.1;
										ParticleUtil.playParticles(EnumParticle.REDSTONE, loc4, 0.0001, 0.0001, 0.0001, 1, 0);
									}
									if(timer <= 160) {
										double x = (radius*Math.sin(angle1));
										double z = (radius*Math.cos(angle1));
										loc1.add(x, 0, z);
										angle1+=0.1;
										ParticleUtil.playParticles(EnumParticle.REDSTONE, loc1, 0.255, 0.255, 0.255, 1, 0);
									}
									if(timer <= 80) {
										double x = (radius*Math.sin(angle2));
										double z = (radius*Math.cos(angle2));
										loc2.add(x, 0, z);
										angle2+=0.1;
										ParticleUtil.playParticles(EnumParticle.REDSTONE, loc2, 0.170, 0.170, 0.170, 1, 0);
									}
									if(timer <= 0) {
										double x = (radius*Math.sin(angle3));
										double z = (radius*Math.cos(angle3));
										loc3.add(x, 0, z);
										angle3+=0.1;
										ParticleUtil.playParticles(EnumParticle.REDSTONE, loc3, 0.77, 0.77, 0.77, 1, 0);
									}
									if(timer > 0) {
										timer--;
									}
								}
							}
						}catch(NullPointerException e) {}
						loc = p.getLocation();
					}
				}
				
			}.runTaskTimer(CTFMain.instance, 0, 1);
		}else if(id.equalsIgnoreCase("aura_wings")) {
			run = new BukkitRunnable() {
				int timer = 10;
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if(!prof.isDead()) {
						try {
							if(p.getLocation().getX() != loc.getX() || p.getLocation().getY() != loc.getY() || p.getLocation().getZ() != loc.getZ()) {
								if(p.isOnGround()) {
									//They moved, stop applying aura
									ParticleUtil.playParticles(EnumParticle.ENCHANTMENT_TABLE, p.getLocation(), 0.7, 0.8, 0.7, 0.2, 4);
									ParticleUtil.playParticles(EnumParticle.SPELL_INSTANT, p.getLocation(), 0.2, 0, 0.2, 0, 1);
								}
							}else {
								if(p.isOnGround()) {
									
									timer--;
									if(timer == 0) {
										timer = 10;
										rightWing(p, EnumParticle.ENCHANTMENT_TABLE, EnumParticle.REDSTONE);
										leftWing(p, EnumParticle.ENCHANTMENT_TABLE, EnumParticle.REDSTONE);
									}
								}
							}
						}catch(NullPointerException e) {}
						loc = p.getLocation();
					}
				}
				
			}.runTaskTimer(CTFMain.instance, 0, 1);
		}else if(id.equalsIgnoreCase("aura_crown")) {
			run = new BukkitRunnable() {
				int timer = 3;
				boolean upwards = false;
				boolean downwards = false;
				float angle = 0;
				float radius = 0.8f;
				float yoffset = 0;
				@Override
				public void run() {
					if(!prof.isDead()) {
						try {
							if(p.getLocation().getX() != loc.getX() || p.getLocation().getY() != loc.getY() || p.getLocation().getZ() != loc.getZ()) {
									if(timer == 0) {
										timer = 3;
											double x = 0.295;
											double y = 2;
											double z = 0.288;
											double yaw = Math.toRadians(p.getLocation().getYaw());
										
											
											for(int i = 0; i < 5; i++) {
												y=2;
												Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
							                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
												z-=0.12;
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
												y=0.1;
												locc1.setY(locc1.getY() + y);
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
											}
											
											for(int i = 0; i < 5; i++) {
												y=2;
												Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
							                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
												x-=0.12;
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
												y=0.1;
												locc1.setY(locc1.getY() + y);
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
											}
											
											for(int i = 0; i < 5; i++) {
												y=2;
												Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
							                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
												z+=0.12;
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
												y=0.1;
												locc1.setY(locc1.getY() + y);
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
											}
											
											for(int i = 0; i < 5; i++) {
												y=2;
												Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
							                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
												x+=0.12;
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
												y=0.1;
												locc1.setY(locc1.getY() + y);
												ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
											}
								}
									timer--;
									double z = -1;
									double x = 0;
									double y = 1;
									double yaw = Math.toRadians(p.getLocation().getYaw());
									Location loc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
				                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
									ParticleUtil.playParticles(EnumParticle.FLAME, loc1, 0.5, 0.7, 0.5, 0.04, 1);
							}else {
								
								Location loc1 = p.getLocation();
								double x1 = (radius * Math.sin(angle));
								double z1 = (radius * Math.cos(angle));
								loc1.add(x1, yoffset, z1);
								if(yoffset <= 0) {
									upwards = true;
									downwards = false;
									
									ParticleUtil.playParticles(EnumParticle.FLAME, loc1, 0, 0, 0, 0.02, 10);
								}
								if(yoffset >= 2) {
									upwards = false;
									downwards = true;
									
									ParticleUtil.playParticles(EnumParticle.FLAME, loc1, 0, 0, 0, 0.02, 10);
								}
								if(upwards) {
									yoffset+=0.1;
									
									Location loc2 = loc1.clone(); loc2.setX(loc2.getX()-0.1); loc2.setZ(loc2.getZ()-0.1); loc2.setY(loc2.getY()-0.1);
									ParticleUtil.playParticles(EnumParticle.REDSTONE, loc2, 0.77, 0.77, 0.255, 1, 0);
								}
								if(downwards) {
									yoffset-=0.1;
									
									Location loc2 = loc1.clone(); loc2.setX(loc2.getX()-0.1); loc2.setZ(loc2.getZ()-0.1); loc2.setY(loc2.getY()+0.1);
									ParticleUtil.playParticles(EnumParticle.REDSTONE, loc2, 0.77, 0.77, 0.255, 1, 0);
								}
								angle+=0.1;
								ParticleUtil.playParticles(EnumParticle.REDSTONE, loc1, 0.77, 0.255, 0.255, 1, 0);
								
								if(timer == 0) {
									timer = 3;
								
										double x = 0.295;
										double y = 2;
										double z = 0.288;
										double yaw = Math.toRadians(p.getLocation().getYaw());
									
										
										for(int i = 0; i < 5; i++) {
											y=2;
											Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
						                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
											z-=0.12;
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
											y=0.1;
											locc1.setY(locc1.getY() + y);
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
										}
										
										for(int i = 0; i < 5; i++) {
											y=2;
											Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
						                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
											x-=0.12;
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
											y=0.1;
											locc1.setY(locc1.getY() + y);
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
										}
										
										for(int i = 0; i < 5; i++) {
											y=2;
											Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
						                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
											z+=0.12;
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
											y=0.1;
											locc1.setY(locc1.getY() + y);
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
										}
										
										for(int i = 0; i < 5; i++) {
											y=2;
											Location locc1 = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
						                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
											x+=0.12;
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.55, 0.122, 1, 0);
											y=0.1;
											locc1.setY(locc1.getY() + y);
											ParticleUtil.playParticles(EnumParticle.REDSTONE, locc1, 0.77, 0.77, 0.255, 1, 0);
										}
										
										
									
								}
								timer--;
							}
						}catch(NullPointerException e) {}
						loc = p.getLocation();
					}
				}
			}.runTaskTimer(CTFMain.instance, 0, 1);
		}else if(id.equalsIgnoreCase("aura_demon")) {
			run = new BukkitRunnable() {
				float radius = 1.9f;
				float mod1 = 0;
				
				float rad1 = 1.5f;
				
				int timer = 70;
				boolean sprite = false;
				float yoffset = 0;
				float angle = 0;
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					if(!prof.isDead()) {
						try {
							if(p.getLocation().getX() != loc.getX() || p.getLocation().getY() != loc.getY() || p.getLocation().getZ() != loc.getZ()) {
								if(p.isOnGround()) {
									mod1+=0.05;
									ArrayList<Location> locs = new ArrayList<Location>();
									for(int i = 0; i < 3; i++) {
										double angle = (2*Math.PI*i/3)+mod1;
										Location loc1 = p.getLocation().clone().add(radius * Math.sin(angle), 0, radius*Math.cos(angle));
										locs.add(loc1);
										ParticleUtil.playParticles(EnumParticle.SPELL_WITCH, loc1, 0, 0, 0, 0, 1);
									}
									Location loc1 = locs.get(0);
									Location loc2 = locs.get(1);
									Location loc3 = locs.get(2);
									
									Vector p1 = loc1.toVector();
									Vector line1 = loc2.toVector().subtract(p1).normalize().multiply(0.1);
									double dist1 = loc1.distance(loc2);
									double progress1 = 0;
									
									for(; progress1 < dist1; p1.add(line1)) {
										Location l = new Location(p.getWorld(), p1.getX(), p1.getY(), p1.getZ());
										ParticleUtil.playParticles(EnumParticle.SPELL_MOB_AMBIENT, l, 0, 0, 0, 0, 1);
										progress1+=0.1;
									}
									
									Vector p2 = loc2.toVector();
									Vector line2 = loc3.toVector().subtract(p2).normalize().multiply(0.1);
									double dist2 = loc2.distance(loc3);
									double progress2 = 0;
									
									for(; progress2 < dist2; p2.add(line2)) {
										Location l = new Location(p.getWorld(), p2.getX(), p2.getY(), p2.getZ());
										ParticleUtil.playParticles(EnumParticle.SPELL_MOB_AMBIENT, l, 0, 0, 0, 0, 1);
										progress2+=0.1;
									}
									
									Vector p3 = loc3.toVector();
									Vector line3 = loc1.toVector().subtract(p3).normalize().multiply(0.1);
									double dist3 = loc3.distance(loc1);
									double progress3 = 0;
									
									for(; progress3 < dist3; p3.add(line3)) {
										Location l = new Location(p.getWorld(), p3.getX(), p3.getY(), p3.getZ());
										ParticleUtil.playParticles(EnumParticle.SPELL_MOB_AMBIENT, l, 0, 0, 0, 0, 1);
										progress3+=0.1;
									}
								}
							}else {
								timer--;
								//Sprite effect
								Location sloc = p.getLocation();
								
								if(timer <= 0) {
									sprite = true;
								}
								if(sprite) {
									double x1 = (rad1 * Math.sin(angle));
									double z1 = (rad1 * Math.cos(angle));
									sloc.add(x1, yoffset, z1);
									rad1-=0.03;
									yoffset+=0.05;
									angle+=0.3;
									ParticleUtil.playParticles(EnumParticle.SPELL_MOB, sloc, 0, 0, 0, 0, 3);
									ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, sloc, 0, 0, 0, 0.02, 2);
									
									if(rad1 <= 0) {
										sprite = false;
										timer = 70;
										angle = 0;
										yoffset = 0;
										rad1 = 1.5f;
										ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, sloc, 0, 0, 0, 0.08, 25);
									}
								}
								//Triangle effect
									mod1+=0.05;
									ArrayList<Location> locs = new ArrayList<Location>();
									for(int i = 0; i < 3; i++) {
										double angle = (2*Math.PI*i/3)+mod1;
										Location loc1 = p.getLocation().clone().add(radius * Math.sin(angle), 0, radius*Math.cos(angle));
										locs.add(loc1);
										ParticleUtil.playParticles(EnumParticle.SPELL_WITCH, loc1, 0, 0, 0, 0, 1);
									}
									Location loc1 = locs.get(0);
									Location loc2 = locs.get(1);
									Location loc3 = locs.get(2);
									
									Vector p1 = loc1.toVector();
									Vector line1 = loc2.toVector().subtract(p1).normalize().multiply(0.1);
									double dist1 = loc1.distance(loc2);
									double progress1 = 0;
									
									for(; progress1 < dist1; p1.add(line1)) {
										Location l = new Location(p.getWorld(), p1.getX(), p1.getY(), p1.getZ());
										ParticleUtil.playParticles(EnumParticle.SPELL_MOB_AMBIENT, l, 0, 0, 0, 0, 1);
										progress1+=0.1;
									}
									
									Vector p2 = loc2.toVector();
									Vector line2 = loc3.toVector().subtract(p2).normalize().multiply(0.1);
									double dist2 = loc2.distance(loc3);
									double progress2 = 0;
									
									for(; progress2 < dist2; p2.add(line2)) {
										Location l = new Location(p.getWorld(), p2.getX(), p2.getY(), p2.getZ());
										ParticleUtil.playParticles(EnumParticle.SPELL_MOB_AMBIENT, l, 0, 0, 0, 0, 1);
										progress2+=0.1;
									}
									
									Vector p3 = loc3.toVector();
									Vector line3 = loc1.toVector().subtract(p3).normalize().multiply(0.1);
									double dist3 = loc3.distance(loc1);
									double progress3 = 0;
									
									for(; progress3 < dist3; p3.add(line3)) {
										Location l = new Location(p.getWorld(), p3.getX(), p3.getY(), p3.getZ());
										ParticleUtil.playParticles(EnumParticle.SPELL_MOB_AMBIENT, l, 0, 0, 0, 0, 1);
										progress3+=0.1;
									}
								
							}
						} catch(NullPointerException e) {}
						loc = p.getLocation();
					}
				}
			}.runTaskTimer(CTFMain.instance, 0, 1);
		}
	}
	
	public void disable() {
		run.cancel();
	}
	
	public static void rightWing(Player p, EnumParticle core, EnumParticle rim) {
		int arr[][] = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 2, 2, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0},
                { 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 0, 0, 0, 0},
                { 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0},
                { 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 0},
                { 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0},
                { 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0},
                { 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2},
                { 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                { 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                { 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2},
                { 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 2},
                { 0, 0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0} };
		
		 double yaw = Math.toRadians(p.getLocation().getYaw() + 15);
	        double x = -2.15;
	        double y = 2;
	        double z = -0.045;

	        for (int i = 0; i < arr.length; i++) {
	            for (int j = 0; j < arr[i].length; j++) {
	                if (arr[i][j] == 1) {
	                	Location locc = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
	                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
	                	ParticleUtil.playParticles(core, locc, 0, 0, 0, 0, 1);
	                }else if(arr[i][j] == 2) {
	                	Location locc = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
	                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
	                	ParticleUtil.playParticles(rim, locc, 0.4, 0.77, 0.77, 1, 0);
	                }
	                x += 0.1;
	            }
	            x = -2.15;
	            y -= 0.05;
	        }
	}
	
	public void leftWing(Player p, EnumParticle core, EnumParticle rim) {
		int arr[][] = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 2, 2, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0},
                { 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 0, 0, 0, 0},
                { 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0},
                { 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 0},
                { 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0},
                { 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0},
                { 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2},
                { 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                { 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                { 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2},
                { 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 2},
                { 0, 0, 0, 0, 0, 0, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 1, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 1, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0} };
		
		 double yaw = Math.toRadians(p.getLocation().getYaw() + 165);
		 	double x = -2.15;
	        double y = 2;
	        double z = 0.045;

	        for (int i = 0; i < arr.length; i++) {
	            for (int j = 0; j < arr[i].length; j++) {
	                if (arr[i][j] == 1) {
	                	Location locc = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
	                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
	                	ParticleUtil.playParticles(core, locc, 0, 0, 0, 0, 1);
	                }else if(arr[i][j] == 2) {
	                	Location locc = new Location(p.getWorld(), p.getLocation().getX() + x * Math.cos(yaw) + z * -Math.sin(yaw),
	                			p.getLocation().getY() + y, p.getLocation().getZ() + x * Math.sin(yaw) + z * Math.cos(yaw));
	                	ParticleUtil.playParticles(rim, locc, 0.4, 0.77, 0.77, 1, 0);
	                }
	                x += 0.1;
	            }
	            x = -2.15;
	            y -= 0.05;
	        }
	}

}
