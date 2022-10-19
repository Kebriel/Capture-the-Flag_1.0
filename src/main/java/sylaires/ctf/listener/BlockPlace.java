package sylaires.ctf.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;
import sylaires.ctf.CTFMain;
import sylaires.ctf.GameState;
import sylaires.ctf.PlayerProfile;
import sylaires.ctf.ProfileManager;
import sylaires.ctf.util.ParticleUtil;

/*
 * Copyright 2022, Sylaires. All rights reserved.
 */

public class BlockPlace implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(CTFMain.theState == GameState.PLAYING) {
			if(prof.getIsSelected("item_blocks")) {
				if(e.getBlockPlaced().getType() == Material.GLASS) {
					Block b = e.getBlockPlaced();
					BlockState state = e.getBlockReplacedState();
					e.setCancelled(false);
					new BukkitRunnable() {

						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							b.setType(state.getType());
							b.setData(state.getData().getData());
							ParticleUtil.playParticles(EnumParticle.CLOUD, b.getLocation(), 0.5, 0.5, 0.5, 0, 30);
						}
							
					}.runTaskLater(CTFMain.instance, 60);
					}else {
						e.setCancelled(true);
					}
				}else {
					e.setCancelled(true);
				}
		}else {
			if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				if(!e.getPlayer().isOp()) {
					e.setCancelled(true);
				}
			}
		}
		}
	}

