package sylaires.ctf.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;
import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.game.FlagManager;
import sylaires.ctf.util.ParticleUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class Teleport implements Listener {
	
	@EventHandler
	public void onTp(PlayerTeleportEvent e) {
		if(CTFMain.theState == GameState.PLAYING) {
			if(e.getCause() == TeleportCause.ENDER_PEARL) {
				PlayerProfile prof = ProfileManager.getProfile(e.getPlayer());
				if(prof.isDead()) {
					e.setCancelled(true); return;
				}
				if(prof.getIsSelected("item_pearl")) {
					
					if(FlagManager.blueFlag.getHolder() != null) {
						if(FlagManager.blueFlag.getHolder().getUniqueId().equals(e.getPlayer().getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
					if(FlagManager.redFlag.getHolder() != null) {
						if(FlagManager.redFlag.getHolder().getUniqueId().equals(e.getPlayer().getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
					Player p = e.getPlayer();
					p.damage(0.01);
					p.setHealth(p.getHealth()/1.5);
					new BukkitRunnable() {

						@Override
						public void run() {
							ParticleUtil.playParticles(EnumParticle.SMOKE_NORMAL, p.getLocation(), 0.3, 0.3, 0.3, 0, 70);
							CTFMain.instance.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
						}
						
					}.runTaskLater(CTFMain.instance, 3);
				}else {
					e.setCancelled(true);
				}
			}
		}
	}

}
 