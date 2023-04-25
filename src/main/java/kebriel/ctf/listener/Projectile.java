package kebriel.ctf.listener;

import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;
import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.game.FlagManager;
import kebriel.ctf.util.ParticleUtil;

public class Projectile implements Listener {
	
	@EventHandler
	public void onShootProj(ProjectileLaunchEvent e) {
		if(CTFMain.theState == GameState.PLAYING) {
			if(e.getEntity() instanceof Snowball) {
				Snowball snow = (Snowball) e.getEntity();
				Player shooter = (Player) snow.getShooter();
				PlayerProfile prof = ProfileManager.getProfile(shooter);
				if(prof.getIsSelected("item_snow")) {
					new BukkitRunnable() {

						@Override
						public void run() {
							if(snow.isValid()) {
								ParticleUtil.playParticles(EnumParticle.SNOW_SHOVEL, snow.getLocation(), 0, 0, 0, 0, 1);
							}else {
								this.cancel();
							}
						}
						
					}.runTaskTimer(CTFMain.instance, 0, 1);
				}
			}
			if(e.getEntity() instanceof EnderPearl) {
				EnderPearl pearl = (EnderPearl) e.getEntity();
				Player shooter = (Player) pearl.getShooter();
				PlayerProfile prof = ProfileManager.getProfile(shooter);
				if(prof.getIsSelected("item_pearl")) {
					if(FlagManager.blueFlag.getHolder() != null) {
						if(FlagManager.blueFlag.getHolder().getUniqueId().equals(shooter.getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
					if(FlagManager.redFlag.getHolder() != null) {
						if(FlagManager.redFlag.getHolder().getUniqueId().equals(shooter.getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
					new BukkitRunnable() {

						@Override
						public void run() {
							if(pearl.isValid()) {
								ParticleUtil.playParticles(EnumParticle.PORTAL, pearl.getLocation(), 0, 0, 0, 0.3, 1);
							}else {
								this.cancel();
							}
						}
						
					}.runTaskTimer(CTFMain.instance, 0, 1);
				}
			}
		}else {
			e.setCancelled(true);
		}
	}

}
