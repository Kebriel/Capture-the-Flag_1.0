package kebriel.ctf.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.game.TeamHandler;
import kebriel.ctf.util.MessageUtil;

public class AbilityProcDamage implements Listener {
	
	private ArrayList<Player> rodded = new ArrayList<Player>();
	
	public static ArrayList<UUID> reinforce_cooldown = new ArrayList<UUID>();
	public static HashMap<UUID, Integer> reinforce_tele_amount = new HashMap<UUID, Integer>();
	public static ArrayList<UUID> reinforce_teleported = new ArrayList<UUID>();
	
	private static ArrayList<UUID> panic_cooldown = new ArrayList<UUID>();
	private static ArrayList<UUID> regen_cooldown = new ArrayList<UUID>();
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof ArmorStand) {
			e.setCancelled(true);
		}
		
		if(e.getEntity() instanceof Player) {
			if(CTFMain.theState == GameState.PLAYING) {
					Player p = (Player) e.getEntity();
					PlayerProfile prof = ProfileManager.getProfile(p);
					if(prof.isDead()) {
						e.setCancelled(true);
					}
					if(p.getHealth() - e.getDamage() < 6) {
						if(p.getHealth() >= 6) {
							if(prof.getIsSelected("ability_panic")) {
								if(!panic_cooldown.contains(p.getUniqueId())) {
									p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, true, false));
									MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your " + ChatColor.GOLD + "Panic Ability" + ChatColor.GREEN + " triggered!");
									p.playSound(p.getLocation(), "mob.guardian.hit", 1.3f, 1);
									panic_cooldown.add(p.getUniqueId());
									
									new BukkitRunnable() {
										
										@Override
										public void run() {
											panic_cooldown.remove(p.getUniqueId());
										}
										
									}.runTaskLater(CTFMain.instance, 100);
								}
							}else if(prof.getIsSelected("ability_mending")) {
								if(!regen_cooldown.contains(p.getUniqueId())) {
									p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 0, true, false));
									MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your " + ChatColor.GOLD + "Mending Ability" + ChatColor.GREEN + " triggered!");
									p.playSound(p.getLocation(), "mob.guardian.hit", 1.3f, 1);
									regen_cooldown.add(p.getUniqueId());
									
									new BukkitRunnable() {
										
										@Override
										public void run() {
											regen_cooldown.remove(p.getUniqueId());
										}
										
									}.runTaskLater(CTFMain.instance, 100);
								}
							}
						}
					}
					if(p.isBlocking()) {
						if(prof.getIsSelected("ability_parry")) {
							if(e.getCause() == DamageCause.ENTITY_ATTACK) {
								e.setDamage(e.getDamage()*0.7);
								p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.7f, 0.5f);
							}
						}
					}
					if(e.getCause() == DamageCause.PROJECTILE) {
						if(prof.getIsSelected("ability_kevlar")) {
							if(p.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE) {
								e.setDamage(e.getDamage()*0.55);
							}
						}
					}
					if(e.getCause() == DamageCause.FALL) {
						if(prof.getIsSelected("ability_braced")) {
							e.setDamage(e.getDamage()-(e.getDamage()*0.5));
						}
					}
					if(prof.getIsSelected("ability_gladiator")) {
						for(Entity ent : p.getNearbyEntities(8, 8, 8)) {
							if(ent instanceof Player) {
								Player nearby = (Player) ent;
								if(TeamHandler.redTeam.contains(p.getUniqueId())) {
									if(TeamHandler.blueTeam.contains(nearby.getUniqueId())) {
										e.setDamage(e.getDamage()-(e.getDamage()*0.05));
									}
								}
								if(TeamHandler.blueTeam.contains(p.getUniqueId())) {
									if(TeamHandler.redTeam.contains(nearby.getUniqueId())) {
										e.setDamage(e.getDamage()-(e.getDamage()*0.05));
									}
								}
							}
						}
					}
			}else {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onAttack(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			if(e.getCause() == DamageCause.ENTITY_ATTACK) {
				Player attacker = (Player) e.getDamager();
				PlayerProfile aprof = ProfileManager.getProfile(attacker);
				Player victim = (Player) e.getEntity();
				PlayerProfile vprof = ProfileManager.getProfile(victim);
				if(aprof.isDead()) {
					e.setCancelled(true);
				}
				
				if(TeamHandler.redTeam.contains(victim.getUniqueId()) && TeamHandler.redTeam.contains(attacker.getUniqueId())) {		
					e.setCancelled(true);
					return;
				}else if(TeamHandler.blueTeam.contains(victim.getUniqueId()) && TeamHandler.blueTeam.contains(attacker.getUniqueId())) {		
					e.setCancelled(true);
					return;
				}
				
				triggerReinforce(victim, e.getDamage());
				
				if(victim.getHealth() == victim.getMaxHealth()) {
					if(aprof.getIsSelected("ability_clean")) {
						e.setDamage(e.getDamage() + (e.getDamage()*0.3));
					}
				}
				if(aprof.getIsSelected("ability_berserk")) {
					if(aprof.isBerserked()) {
						e.setDamage(e.getDamage()+(e.getDamage()*0.5));
					}
				}
				if(aprof.getIsSelected("item_rod")) {
					if(rodded.contains(victim)) {
						rodded.remove(victim);
						e.setDamage(e.getDamage()+(e.getDamage()*0.2));
					}
				}
				if(aprof.getIsSelected("ability_firebrand")) {
					if(attacker.getItemInHand() != null) {
						if(attacker.getItemInHand().getType() == Material.WOOD_SWORD) {
							if(!vprof.getIsSelected("ability_antidote")) {
								victim.setFireTicks(60);
							}
						}
					}
				}
				if(aprof.getIsSelected("ability_leech")) {
					Random rand = new Random();
					int random = rand.nextInt(5);
					if(random == 4) { //20% chance of this
						if(attacker.getHealth()+2 > attacker.getMaxHealth()) {
							attacker.setHealth(attacker.getMaxHealth());
						}else {
							attacker.setHealth(attacker.getHealth()+2);
						}
					}
				}
				if(aprof.getIsSelected("ability_massacre")) {
					for(Entity ent : attacker.getNearbyEntities(8, 8, 8)) {
						if(ent instanceof Player) {
							Player nearby = (Player) ent;
							if(TeamHandler.redTeam.contains(attacker.getUniqueId())) {
								if(TeamHandler.blueTeam.contains(nearby.getUniqueId())) {
									e.setDamage(e.getDamage()+(e.getDamage()*0.05));
								}
							}
							if(TeamHandler.blueTeam.contains(attacker.getUniqueId())) {
								if(TeamHandler.redTeam.contains(nearby.getUniqueId())) {
									e.setDamage(e.getDamage()+(e.getDamage()*0.05));
								}
							}
						}
					}
				}
				if(vprof.getIsSelected("ability_bastion")) {
					victim.setVelocity(victim.getLocation().getDirection().multiply(0.0f));
				}
			}
		}
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
			Player damaged = (Player) e.getEntity();
			Arrow a = (Arrow) e.getDamager();
			
			//Remove arrows from bodies
			new BukkitRunnable() {

				@Override
				public void run() {
					((CraftPlayer)damaged).getHandle().getDataWatcher().watch(10, (byte) 0);
				}
				
			}.runTaskLaterAsynchronously(CTFMain.instance, 3);
			
			triggerReinforce(damaged, e.getDamage());
			if(a.getShooter() instanceof Player) {
				Player shooter = (Player) a.getShooter();
				//Friendly fire
				if(TeamHandler.redTeam.contains(shooter.getUniqueId()) && TeamHandler.redTeam.contains(damaged.getUniqueId())) {e.setCancelled(true); return;}
				if(TeamHandler.blueTeam.contains(shooter.getUniqueId()) && TeamHandler.blueTeam.contains(damaged.getUniqueId())) {e.setCancelled(true); return;}
				
				PlayerProfile prof = ProfileManager.getProfile(shooter);
				if(prof.getIsSelected("ability_farshot")) {
					if(Bow.farshot_shots.containsKey(a)) {
						Location shooter_loc = Bow.farshot_shots.get(a);
						double distance = shooter_loc.distance(damaged.getLocation());
						int damagemult = (int) Math.floor(distance-15);
						if(damagemult > 0) {
							double extra = damagemult*0.25;
							e.setDamage(e.getDamage()+extra);
						}
					}
				}
				if(prof.getIsSelected("ability_blank")) {
					if(damaged.getLocation().distance(shooter.getLocation()) <= 8) {
						Vector vec = damaged.getLocation().getDirection().setY(0).normalize().multiply(3);
						damaged.setVelocity(damaged.getLocation().getDirection().setY(0).normalize().add(vec));
					}
				}
			}
		}
		if(e.getDamager() instanceof FishHook && e.getEntity() instanceof Player) {
			Player damaged = (Player) e.getEntity();
			FishHook hook = (FishHook) e.getDamager();
			Player shooter = (Player) hook.getShooter();
			PlayerProfile prof = ProfileManager.getProfile(shooter);
			
			//Friendly fire
			if(TeamHandler.redTeam.contains(shooter.getUniqueId()) && TeamHandler.redTeam.contains(damaged.getUniqueId())) {e.setCancelled(true); return;}
			if(TeamHandler.blueTeam.contains(shooter.getUniqueId()) && TeamHandler.blueTeam.contains(damaged.getUniqueId())) {e.setCancelled(true); return;}
			
			if(prof.getIsSelected("item_rod")) {
				if(!rodded.contains(damaged)) {
					rodded.add(damaged);
				}
			}
		}
		if(e.getDamager() instanceof Snowball && e.getEntity() instanceof Player) {
			Player damaged = (Player) e.getEntity();
			Snowball snow = (Snowball) e.getDamager();
			Player shooter = (Player) snow.getShooter();
			PlayerProfile prof = ProfileManager.getProfile(shooter);
			
			//Friendly fire
			if(TeamHandler.redTeam.contains(shooter.getUniqueId()) && TeamHandler.redTeam.contains(damaged.getUniqueId())) {e.setCancelled(true); return;}
			if(TeamHandler.blueTeam.contains(shooter.getUniqueId()) && TeamHandler.blueTeam.contains(damaged.getUniqueId())) {e.setCancelled(true); return;}
			
			if(prof.getIsSelected("item_snow")) {
				PlayerProfile dprof = ProfileManager.getProfile(damaged);
				if(!dprof.getIsSelected("ability_antidote")) {
					damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 0, true, false), true);
				}
			}
		}
	}
	
	public static void triggerReinforce(Player victim, double damage) {
		PlayerProfile vprof = ProfileManager.getProfile(victim);
		if(vprof.getIsSelected("perk_reinforce")) {
			if(FlagManager.blueFlag.getHolder() != null) {
				if(FlagManager.blueFlag.getHolder().getUniqueId().equals(victim.getUniqueId())) { //They're a flag holder
					if(victim.getHealth() - damage < (victim.getMaxHealth()/2)) {
						if(!reinforce_cooldown.contains(victim.getUniqueId())) {
							reinforce_cooldown.add(victim.getUniqueId());
							reinforce_tele_amount.put(victim.getUniqueId(), 0);
							
							if(TeamHandler.redTeam.contains(victim.getUniqueId())) {
								for(UUID id : TeamHandler.redTeam) {
									if(!id.equals(victim.getUniqueId())) {
										Player p = Bukkit.getPlayer(id);
										
										TextComponent click = new TextComponent("CLICK HERE");
										click.setColor(net.md_5.bungee.api.ChatColor.GOLD);
										click.setBold(true);
										click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hiddencom perk_reinforce " + victim.getName()));
										click.addExtra(ChatColor.GREEN + " to instantly teleport to them!");
										
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your teammate " + ChatColor.YELLOW + victim.getName() + ChatColor.GREEN + " is requesting aid through their " + ChatColor.AQUA + "Call Reinforcements" + ChatColor.GREEN + " perk!");
										p.spigot().sendMessage(click);
										p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1.3f, 2);
									}
								}
							}else if(TeamHandler.blueTeam.contains(victim.getUniqueId())) {
								for(UUID id : TeamHandler.blueTeam) {
									if(!id.equals(victim.getUniqueId())) {
										Player p = Bukkit.getPlayer(id);
										
										TextComponent click = new TextComponent("CLICK HERE");
										click.setColor(net.md_5.bungee.api.ChatColor.GOLD);
										click.setBold(true);
										click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hiddencom perk_reinforce " + victim.getName()));
										click.addExtra(ChatColor.GREEN + " to instantly teleport to them!");
										
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your teammate " + ChatColor.YELLOW + victim.getName() + ChatColor.GREEN + " is requesting aid through their " + ChatColor.AQUA + "Call Reinforcements" + ChatColor.GREEN + " perk!");
										p.spigot().sendMessage(click);
										p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1.3f, 2);
									}
								}
							}
							
							new BukkitRunnable() {

								@Override
								public void run() {
									reinforce_cooldown.remove(victim.getUniqueId());
									reinforce_tele_amount.remove(victim.getUniqueId());
								}
								
							}.runTaskLater(CTFMain.instance, 400);
						}
					}
				}
			}
			if(FlagManager.redFlag.getHolder() != null) {
				if(FlagManager.redFlag.getHolder().getUniqueId().equals(victim.getUniqueId())) { //They're a flag holder
					if(victim.getHealth() - damage < (victim.getMaxHealth()/2)) {
						if(!reinforce_cooldown.contains(victim.getUniqueId())) {
							reinforce_cooldown.add(victim.getUniqueId());
							reinforce_tele_amount.put(victim.getUniqueId(), 0);
							
							if(TeamHandler.redTeam.contains(victim.getUniqueId())) {
								for(UUID id : TeamHandler.redTeam) {
									if(!id.equals(victim.getUniqueId())) {
										Player p = Bukkit.getPlayer(id);
										
										TextComponent click = new TextComponent("CLICK HERE");
										click.setColor(net.md_5.bungee.api.ChatColor.GOLD);
										click.setBold(true);
										click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hiddencom perk_reinforce " + victim.getName()));
										click.addExtra(ChatColor.GREEN + " to instantly teleport to them!");
										
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your teammate " + ChatColor.YELLOW + victim.getName() + ChatColor.GREEN + " is requesting aid through their " + ChatColor.AQUA + "Call Reinforcements" + ChatColor.GREEN + " perk!");
										p.spigot().sendMessage(click);
										p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1.3f, 2);
									}
								}
							}else if(TeamHandler.blueTeam.contains(victim.getUniqueId())) {
								for(UUID id : TeamHandler.blueTeam) {
									if(!id.equals(victim.getUniqueId())) {
										Player p = Bukkit.getPlayer(id);
										
										TextComponent click = new TextComponent("CLICK HERE");
										click.setColor(net.md_5.bungee.api.ChatColor.GOLD);
										click.setBold(true);
										click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hiddencom perk_reinforce " + victim.getName()));
										click.addExtra(ChatColor.GREEN + " to instantly teleport to them!");
										
										MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Your teammate " + ChatColor.YELLOW + victim.getName() + ChatColor.GREEN + " is requesting aid through their " + ChatColor.AQUA + "Call Reinforcements" + ChatColor.GREEN + " perk!");
										p.spigot().sendMessage(click);
										p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1.3f, 2);
									}
								}
							}
							
							new BukkitRunnable() {

								@Override
								public void run() {
									reinforce_cooldown.remove(victim.getUniqueId());
									reinforce_tele_amount.remove(victim.getUniqueId());
								}
								
							}.runTaskLater(CTFMain.instance, 400);
						}
					}
				}
			}
		}
	}

}
