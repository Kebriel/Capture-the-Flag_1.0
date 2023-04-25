package kebriel.ctf.display;

import kebriel.ctf.cosmetic.CosmeticRegistry;
import kebriel.ctf.cosmetic.CosmeticType;
import kebriel.ctf.cosmetic.ICosmetic;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import kebriel.ctf.PlayerProfile;
import kebriel.ctf.ProfileManager;
import kebriel.ctf.util.ItemBuilder;

public class GUIButtonMenu extends GUIButton {
	
	private CosmeticType type;
	
	public GUIButtonMenu(CosmeticType type, Player opener) {
		this.type = type;
		ItemStack stack = null;
		PlayerProfile prof = ProfileManager.getProfile(opener);
		switch(type) {
		case AURA: stack = new ItemBuilder(Material.BLAZE_POWDER, ChatColor.GOLD + "Particle Effects")
				.addLore(ChatColor.BLUE + "Choose from an array of unique")
				.addLore(ChatColor.BLUE + "particle effects to display")
				.addLore(ChatColor.BLUE + "around your character!").toItem(); break;
		case TRAIL: stack = new ItemBuilder(Material.ARROW, ChatColor.GOLD + "Arrow Trails")
				.addLore(ChatColor.BLUE + "Select a unique cosmetic effect that")
				.addLore(ChatColor.BLUE + "will apply to your arrows when")
				.addLore(ChatColor.BLUE + "they are shot").toItem(); break;
		case KILL: stack = new ItemBuilder(Material.SKULL_ITEM, ChatColor.GOLD + "Kill Effects")
				.addLore(ChatColor.BLUE + "Choose from a plethora of special ")
				.addLore(ChatColor.BLUE + "effects that will display whenever")
				.addLore(ChatColor.BLUE + "you kill a player!").toItem(); break;
		case MESSAGE: stack = new ItemBuilder(Material.NAME_TAG, ChatColor.GOLD + "Kill Messages")
				.addLore(ChatColor.BLUE + "Choose a from a suite of themes")
				.addLore(ChatColor.BLUE + "that will play special kill")
				.addLore(ChatColor.BLUE + "messages whenever you get a kill!").toItem(); break;
		}
		if(prof.getSelectedCosmetic(type).equalsIgnoreCase("none")) {
			stack = new ItemBuilder(stack).addLore(" ")
					.addLore(ChatColor.GREEN + "Click to browse").toItem();
		}else {
			ICosmetic selected = CosmeticRegistry.getInstanceByID(prof.getSelectedCosmetic(type));
			stack = new ItemBuilder(stack).addLore("  ")
					.addLore(ChatColor.GREEN + "Selected: " + ChatColor.GOLD + selected.getDisplayName())
					.addLore(" ")
					.addLore(ChatColor.GREEN + "Click to browse")
					.toItem();
		}
		
		setIcon(stack);
	}
	
	@Override
	public void run(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		switch(type) {
		case AURA: AuraGUI.getNew(p); break;
		case TRAIL: ArrowTrailGUI.getNew(p); break;
		case KILL: KillEffectGUI.getNew(p); break;
		case MESSAGE: KillMessageGUI.getNew(p); break;
		}
	}

}
