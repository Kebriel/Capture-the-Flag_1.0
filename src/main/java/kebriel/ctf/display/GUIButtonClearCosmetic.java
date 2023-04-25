package kebriel.ctf.display;

import kebriel.ctf.cosmetic.CosmeticType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;
import kebriel.ctf.util.MessageUtil;

public class GUIButtonClearCosmetic extends GUIButton {
	
	private CosmeticType type;
	private ItemStack stack;
	
	public GUIButtonClearCosmetic(CosmeticType type, Player p) {
		this.type = type;
		
		stack = new ItemBuilder(Material.HOPPER, ChatColor.RED + "Clear Effect")
				.addLore(ChatColor.YELLOW + "Click to clear your current effect").toItem();
		setIcon(stack);
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		PlayerProfile prof = ProfileManager.getProfile(p);
		if(!prof.getSelectedCosmetic(type).equalsIgnoreCase("none")) {
			prof.setSelectedCosmetic("none", type);
			p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
			MessageUtil.sendToPlayer(p, ChatColor.GREEN + "Selection wiped!");
			if(type == CosmeticType.AURA) {
				prof.getEffect().disable();
				AuraGUI.getNew(p);
			}else if(type == CosmeticType.TRAIL) {
				ArrowTrailGUI.getNew(p);
			}else if(type == CosmeticType.KILL) {
				KillEffectGUI.getNew(p);
			}else if(type == CosmeticType.MESSAGE) {
				KillMessageGUI.getNew(p);
			}
		}else {
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
			MessageUtil.sendToPlayer(p, ChatColor.RED + "You have nothing selected!");
		}
	}

}
