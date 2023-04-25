package kebriel.ctf.command;

import kebriel.ctf.display.CosmeticGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CosmeticCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("cosmetic")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				CosmeticGUI.getNew(p);
			}
		}
		return false;
	}
	
	

}
