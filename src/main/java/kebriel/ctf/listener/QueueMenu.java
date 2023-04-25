package kebriel.ctf.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.game.TeamHandler;
import kebriel.ctf.util.MessageUtil;

public class QueueMenu implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() != null) {
				if(CTFMain.theState == GameState.WAITING || CTFMain.theState == GameState.STARTING) {
					if(e.getItem().getType() == Material.WOOL && e.getItem().getData().getData() == 11) {
						if(!TeamHandler.blueQueue.contains(p.getUniqueId())) {
							if(TeamHandler.joinQueue(p, "blue")) { //Successfully joined
								MessageUtil.sendToPlayer(p, ChatColor.GREEN + "You are now Queued for the " + ChatColor.BLUE + "Blue Team" + ChatColor.GREEN + "!");
								p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
								if(TeamHandler.redQueue.contains(p.getUniqueId())) {
									TeamHandler.redQueue.remove(p.getUniqueId());
								}
							}else { //Not so much
								MessageUtil.sendToPlayer(p, ChatColor.RED + "The Queue for the " + ChatColor.BLUE + "Blue Team" + ChatColor.RED + " is full!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
							}
						}else {
							MessageUtil.sendToPlayer(p, ChatColor.RED + "You are already Queued for the " + ChatColor.BLUE + "Blue Team" + ChatColor.RED + "!");
							p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						}
					}else if(e.getItem().getType() == Material.WOOL && e.getItem().getData().getData() == 14) {
						if(!TeamHandler.redQueue.contains(p.getUniqueId())) {
							if(TeamHandler.joinQueue(p, "red")) { //Successfully joined
								MessageUtil.sendToPlayer(p, ChatColor.GREEN + "You are now Queued for the " + ChatColor.RED + "Red Team" + ChatColor.GREEN + "!");
								p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
								if(TeamHandler.blueQueue.contains(p.getUniqueId())) {
									TeamHandler.blueQueue.remove(p.getUniqueId());
								}
							}else { //Not so much
								MessageUtil.sendToPlayer(p, ChatColor.RED + "The Queue for the " + ChatColor.RED + "Red Team" + ChatColor.RED + " is full!");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
							}
						}else {
							MessageUtil.sendToPlayer(p, ChatColor.RED + "You are already Queued for the Red Team!");
							p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						}
					}else if(e.getItem().getType() == Material.WOOL && e.getItem().getData().getData() == 0) {
						if(TeamHandler.redQueue.contains(p.getUniqueId())) {
							TeamHandler.redQueue.remove(p.getUniqueId());
							MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Removed from Queue!");
							p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
						}else if(TeamHandler.blueQueue.contains(p.getUniqueId())) {
							TeamHandler.blueQueue.remove(p.getUniqueId());
							MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Removed from Queue!");
							p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.4f, 1);
						}else {
							MessageUtil.sendToPlayer(p, ChatColor.RED + "You are not Queued!");
						}
					}
				}
			}
		}
	}

}
