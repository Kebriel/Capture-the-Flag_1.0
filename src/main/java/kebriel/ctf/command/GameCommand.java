package kebriel.ctf.command;

import kebriel.ctf.display.ScoreboardSidebar;
import kebriel.ctf.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import kebriel.ctf.CTFMain;
import kebriel.ctf.GameState;
import kebriel.ctf.LobbyTimer;
import kebriel.ctf.game.MapVoting;

public class GameCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(cmd.getName().equalsIgnoreCase("game")) {
			if(sender.isOp()) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Usage: /game <start:stop>");
				}else {
					if(args[0].equalsIgnoreCase("start")) {
						if(CTFMain.theState == GameState.WAITING) {
							CTFMain.theState = GameState.STARTING;
							MessageUtil.sendToLobby(ChatColor.GREEN + "An admin started the game!");
							CTFMain.theState = GameState.STARTING;
							LobbyTimer.timer = 30;
							MapVoting.init();
							MapVoting.voting = true;
							ScoreboardSidebar.votingBoard();
							MessageUtil.sendToLobby(ChatColor.AQUA + "The game is starting in " + ChatColor.WHITE + LobbyTimer.timer + ChatColor.AQUA + " seconds!");
						}else {
							sender.sendMessage("The game is either already started, or terminated.");
						}
					}else if(args[0].equalsIgnoreCase("stop")) {
						if(CTFMain.theState == GameState.STARTING || CTFMain.theState == GameState.PLAYING) {
							CTFMain.theState = GameState.TERMINATED;
							MessageUtil.sendToLobby(ChatColor.RED + "An admin has terminated the game.");
						}
					}
				}
			}
		}
		return false;
	}
	
	

}
