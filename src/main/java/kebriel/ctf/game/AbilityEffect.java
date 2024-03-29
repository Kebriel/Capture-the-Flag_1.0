package kebriel.ctf.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;

public class AbilityEffect {
	
	public static void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(CTFMain.theState == GameState.PLAYING) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						PlayerProfile prof = ProfileManager.getProfile(p);
						if(!prof.isDead()) {
							if(prof.getIsSelected("ability_ironskin")) { //Keep effects updated
								p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 50, 0, true, false), true);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 0, true, false), true);
							}
						}
					}
				}else {
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 40);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				if(CTFMain.theState == GameState.PLAYING) {
					for(Player p : Bukkit.getOnlinePlayers()) {
						PlayerProfile prof = ProfileManager.getProfile(p);
						if(!prof.isDead()) {
							if(prof.getIsSelected("perk_untrackable")) { //Render hidden nametags properly -- packets are finnicky
								TeamHandler.applyPrefixes(p);
							}
						}
					}
				}else {
					this.cancel();
				}
			}
			
		}.runTaskTimer(CTFMain.instance, 0, 100);
	}

}
