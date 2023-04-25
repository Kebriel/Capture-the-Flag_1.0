package kebriel.ctf.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import kebriel.ctf.CTFMain;

public class Consume implements Listener {
	
	@EventHandler
	public void onConsume(PlayerItemConsumeEvent e) {
		if(e.getItem().getType() == Material.POTION) {
			new BukkitRunnable() {

				@Override
				public void run() {
					e.getPlayer().getInventory().remove(Material.GLASS_BOTTLE);
				}
				
			}.runTaskLater(CTFMain.instance, 2);
		}
	}

}
