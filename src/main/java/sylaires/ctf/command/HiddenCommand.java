package sylaires.ctf.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EntityLightning;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityWeather;
import sylaires.ctf.CTFMain;
import sylaires.ctf.listener.AbilityProcDamage;
import sylaires.ctf.util.MessageUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class HiddenCommand implements CommandExecutor {

	/*
	Temporarily unimplemented feature for a perk that is not currently accessible in the
	perk menu... Created as a command as I wanted to experiment with creating a hidden
	command that could be invoked as an ability. Likely a more effective way to implement
	would be simply to create a class with appropriate methods.
	 */

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("hiddencom")) {
				if(args[0].equalsIgnoreCase("perk_reinforce")) {
					Player reinforced = Bukkit.getPlayer(args[1]); //This should never be run with an invalid player
					Player teleporting = (Player) sender;
					if(AbilityProcDamage.reinforce_cooldown.contains(reinforced.getUniqueId())) {
						if(AbilityProcDamage.reinforce_tele_amount.get(reinforced.getUniqueId()) < 2) {
							if(!AbilityProcDamage.reinforce_teleported.contains(teleporting.getUniqueId())) {
								teleporting.teleport(reinforced);
								AbilityProcDamage.reinforce_teleported.add(teleporting.getUniqueId());
								AbilityProcDamage.reinforce_tele_amount.put(reinforced.getUniqueId(), AbilityProcDamage.reinforce_tele_amount.get(reinforced.getUniqueId())+1);
								teleporting.playSound(teleporting.getLocation(), Sound.ENDERMAN_TELEPORT, 1.5f, 1);
								
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.playSound(reinforced.getLocation(), Sound.AMBIENCE_THUNDER, 1.3f, 1);
								}
								
								MessageUtil.sendToPlayer(reinforced, ChatColor.GOLD + teleporting.getName() + ChatColor.GREEN + " has answered your call!");
								reinforced.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1, true, false), true);
								reinforced.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1, true, false), true);
								reinforced.playSound(reinforced.getLocation(), Sound.ORB_PICKUP, 1, 1);
								
								new BukkitRunnable() {

									@Override
									public void run() {
										AbilityProcDamage.reinforce_teleported.remove(teleporting.getUniqueId());
									}
									
								}.runTaskLater(CTFMain.instance, 1200);
							}else {
								MessageUtil.sendToPlayer(teleporting, ChatColor.RED + "You've done this too recently, please wait!");
							}
						}else {
							MessageUtil.sendToPlayer(teleporting, ChatColor.RED + "The maximum number of players have already teleported!");
						}
					}else {
						MessageUtil.sendToPlayer(teleporting, ChatColor.RED + "The request has expired!");
					}
				}
			}
		}
		return false;
	}
	
	

}
