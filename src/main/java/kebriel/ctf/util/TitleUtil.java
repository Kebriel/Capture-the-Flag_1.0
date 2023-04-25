package kebriel.ctf.util;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import kebriel.ctf.CTFMain;

public class TitleUtil {
	
	@SuppressWarnings("deprecation")
	public static void setForPlayer(Player p, String header, String footer){
	       
        CraftPlayer craftplayer = (CraftPlayer)p;
        PlayerConnection connection = craftplayer.getHandle().playerConnection;
        IChatBaseComponent top = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent bottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
          Field headerField = packet.getClass().getDeclaredField("a");
          headerField.setAccessible(true);
          headerField.set(packet, top);
          headerField.setAccessible(!headerField.isAccessible());

          Field footerField = packet.getClass().getDeclaredField("b");
          footerField.setAccessible(true);
          footerField.set(packet, bottom);
          footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception ev) {
          ev.printStackTrace();
        }

        connection.sendPacket(packet);
    }
	
	public static void sendTitleToPlayer(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
        new BukkitRunnable() {
        	public void run() {
        		con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut));
        	}
        }.runTaskLaterAsynchronously(CTFMain.instance, 1);

        if (subtitle != null) {
            new BukkitRunnable() {
            	public void run() {
            		con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}")));
            	}
            }.runTaskLaterAsynchronously(CTFMain.instance, 1);
        }

        if (title != null) {
           new BukkitRunnable() {
        	   public void run() {
        		   con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}")));
        	   }
           }.runTaskLaterAsynchronously(CTFMain.instance, 1);
        }
    }
	
	public static void sendTitleToLobby(Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			 PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
			 	new BukkitRunnable() {
		        	public void run() {
		        		con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut));
		        	}
		        }.runTaskLaterAsynchronously(CTFMain.instance, 1);


		        if (subtitle != null) {
		        	new BukkitRunnable() {
		            	public void run() {
		            		con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}")));
		            	}
		            }.runTaskLaterAsynchronously(CTFMain.instance, 1);
		        }

		        if (title != null) {
		        	new BukkitRunnable() {
		         	   public void run() {
		         		   con.sendPacket(new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}")));
		         	   }
		            }.runTaskLaterAsynchronously(CTFMain.instance, 1);
		        }
		}
	}
	
	public static void sendText(String text, Player[] player){
        String json = "{text:\""+text+"\"}";
        sendRaw(json, player);
    }
    public static void sendRaw(String json, Player[] player){
        PacketPlayOutChat chat = new PacketPlayOutChat(new ChatComponentText(json), (byte)2);
        for(Player p : player)sendPacket(chat, p);
    }
    @SuppressWarnings("rawtypes")
	private static void sendPacket(Packet p, Player p1){
        ((CraftPlayer)p1).getHandle().playerConnection.sendPacket(p);
    }

}
