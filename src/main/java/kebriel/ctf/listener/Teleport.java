package kebriel.ctf.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;
import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.util.ParticleUtil;

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
 