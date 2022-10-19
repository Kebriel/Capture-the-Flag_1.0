package sylaires.ctf.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import sylaires.ctf.CTFMain;
import sylaires.ctf.Map;
import sylaires.ctf.MapManager;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class MapCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("map")) {
			if(sender.isOp()) {
				if(sender instanceof Player) {
					Player p = (Player) sender;
					if(args.length == 0) {
						p.sendMessage(ChatColor.RED + "Usage: /map <list:create:delete:set>");
					}else {
						if(args.length == 1) {
							if(args[0].equalsIgnoreCase("list")) {
								//Display all maps
								if(MapManager.maps.isEmpty()) {
									p.sendMessage(ChatColor.GOLD + "=============================================");
									p.sendMessage(ChatColor.RED + "No maps to display!");
									p.sendMessage(ChatColor.GOLD + "=============================================");
								}else {
									p.sendMessage(ChatColor.GOLD + "=============================================");
									for(Map map : MapManager.maps) {
										p.sendMessage(ChatColor.YELLOW + "-Name: " + ChatColor.GREEN + map.getName() + ChatColor.YELLOW + " ID: " + ChatColor.GREEN + map.getId());
									}
									p.sendMessage(ChatColor.GOLD + "=============================================");
								}
							}else if(args[0].equalsIgnoreCase("create")) {
								p.sendMessage(ChatColor.RED + "Usage: /map create <id> <name>");
							}else if(args[0].equalsIgnoreCase("delete")) {
								p.sendMessage(ChatColor.RED + "Usage: /map delete <id>");
							}else if(args[0].equalsIgnoreCase("set")) {
								p.sendMessage(ChatColor.RED + "Usage: /map set <id:hub> <redSpawn:blueSpawn:redFlag:blueFlag:redNpc:blueNpc>");
							}
						} else if(args.length == 2) {
							if(args[0].equalsIgnoreCase("create")) {
								p.sendMessage(ChatColor.RED + "Usage: /map create <id> <name>");
							}else if(args[0].equalsIgnoreCase("delete")) {
								if(StringUtils.isNumeric(args[1])) { //Is an number
									int id = 0;
									try {
										id = Integer.parseInt(args[1]);
									} catch (Exception ex) {
										p.sendMessage(ChatColor.RED + "Please enter a valid number!");
										return false;
									}
									if(MapManager.getMap(id) == null) {
										p.sendMessage(ChatColor.RED + "Invalid map!");
										return false;
									}
									if(MapManager.deleteMapByID(id)) { 
										p.sendMessage(ChatColor.GREEN + "Map deleted!");
									}else {
										p.sendMessage(ChatColor.RED + "Invalid map.");
									}
								}else {
									p.sendMessage(ChatColor.RED + "Please enter a valid number!");
								}
								
							}else if(args[0].equalsIgnoreCase("set")) {
								if(args[1].equalsIgnoreCase("hub")) {
									if(args[1].equalsIgnoreCase("hub")) {
										CTFMain.instance.setHub(p.getLocation());
										p.sendMessage(ChatColor.GREEN + "Hub set to your location!");
									}
								}else {
								p.sendMessage(ChatColor.RED + "Usage: /map set <id:hub> <redSpawn:blueSpawn:redFlag:blueFlag:redNpc:blueNpc>");
								}
							}
						} else if(args.length == 3) {
							if(args[0].equalsIgnoreCase("create")) {
								int id = 0;
								if(StringUtils.isNumeric(args[1])) { //Is an number
									try {
										id = Integer.parseInt(args[1]);
									} catch (Exception ex) {
										p.sendMessage(ChatColor.RED + "Please enter a valid number!");
										return false;
									}
								}else {
									p.sendMessage(ChatColor.RED + "Please enter a valid number!");
								}
								String name = args[2];
								if(MapManager.getMap(id) != null || MapManager.getMap(name) != null) {
									p.sendMessage(ChatColor.RED + "A map with that name or ID already exists!");
									return false;
								}
								MapManager.createMap(id, name);
								p.sendMessage(ChatColor.GREEN + "Map '" + name + "' created!");
							}else if(args[0].equalsIgnoreCase("set")) {
								
									int id = 0;
									if(StringUtils.isNumeric(args[1])) { //Is an number
										try {
											id = Integer.parseInt(args[1]);
										} catch (Exception ex) {
											p.sendMessage(ChatColor.RED + "Please enter a valid number!");
											return false;
										}
									}else {
										p.sendMessage(ChatColor.RED + "Please enter a valid number!");
									}
									if(MapManager.getMap(id) == null) {
										p.sendMessage(ChatColor.RED + "Invalid map!");
										return false;
									}
									Map map = MapManager.getMap(id);
									if(args[2].equalsIgnoreCase("redspawn")) {
										Location ploc = new Location(CTFMain.instance.getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ()+0.5);
										map.setRedSpawn(ploc);
										MapManager.saveMapToFile(map);
										p.sendMessage(ChatColor.GREEN + "Set!");
									}else if(args[2].equalsIgnoreCase("redflag")) {
										Location ploc = new Location(CTFMain.instance.getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ()+0.5);
										float angle =  p.getLocation().getYaw()%360;
										angle =(angle +360)%360;
										if(angle >180)
										    angle -=360; 
										ploc.setYaw(angle);
										map.setRedFlag(ploc);
										MapManager.saveMapToFile(map);
										p.sendMessage(ChatColor.GREEN + "Set!");
									}else if(args[2].equalsIgnoreCase("bluespawn")) {
										Location ploc = new Location(CTFMain.instance.getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ()+0.5);
										map.setBlueSpawn(ploc);
										MapManager.saveMapToFile(map);
										p.sendMessage(ChatColor.GREEN + "Set!");
									}else if(args[2].equalsIgnoreCase("blueflag")) {
										Location ploc = new Location(CTFMain.instance.getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ()+0.5);
										float angle =  p.getLocation().getYaw()%360;
										angle =(angle +360)%360;
										if(angle >180)
										    angle -=360; 
										ploc.setYaw(angle);
										map.setBlueFlag(ploc);
										MapManager.saveMapToFile(map);
										p.sendMessage(ChatColor.GREEN + "Set!");
									}else if(args[2].equalsIgnoreCase("rednpc")) {
										Location ploc = new Location(CTFMain.instance.getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ()+0.5);
										float angle =  p.getLocation().getYaw()%360;
										angle =(angle +360)%360;
										if(angle >180)
										    angle -=360; 
										ploc.setYaw(angle);
										map.setRedNpc(ploc);
										MapManager.saveMapToFile(map);
										p.sendMessage(ChatColor.GREEN + "Set!");
										//TODO Spawn it for rotation
									}else if(args[2].equalsIgnoreCase("bluenpc")) {
										Location ploc = new Location(CTFMain.instance.getWorld(), p.getLocation().getBlockX()+0.5, p.getLocation().getBlockY(), p.getLocation().getBlockZ()+0.5);
										float angle =  p.getLocation().getYaw()%360;
										angle =(angle +360)%360;
										if(angle >180)
										    angle -=360; 
										ploc.setYaw(angle);
										map.setBlueNpc(ploc);
										MapManager.saveMapToFile(map);
										p.sendMessage(ChatColor.GREEN + "Set!");
									}else {
										p.sendMessage(ChatColor.RED + "Usage: /map set <id:hub> <redSpawn:blueSpawn:redFlag:blueFlag:redNpc:blueNpc>");
									}
								}
							}
						}
					}
				}
			}
		return false;
	}
	
	

}
