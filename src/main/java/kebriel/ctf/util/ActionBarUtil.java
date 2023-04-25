package kebriel.ctf.util;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBarUtil {
	
	private String msg;
	
	public ActionBarUtil(String msg) {
		this.msg = msg;
	}
	
	public void print(Player p) {
		IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + msg + "\"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(comp, (byte) 2);
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
	}

}
