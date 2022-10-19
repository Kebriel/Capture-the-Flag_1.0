package sylaires.ctf.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class Misc implements Listener {
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			if(!e.getPlayer().isOp()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent e) {
		if(CTFMain.theState != GameState.PLAYING) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onThrow(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		Entity en = e.getProjectile();
		if(en instanceof Arrow) {
			Arrow a = (Arrow) en;
			new BukkitRunnable() {

				@Override
				public void run() {
					if(a.isValid()) {
						if(a.isOnGround()) {
							a.remove();
							this.cancel();
						}
					}else {
						this.cancel();
					}
				}
				
			}.runTaskTimer(CTFMain.instance, 0, 2);
		}
	}

}
