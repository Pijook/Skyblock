package pl.trollcraft.Skyblock.essentials;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;

public class ChatUtils {

    private static String prefix = "&f&l[&a&lSky&2&lblock&f&l]";

    /**
     * Changes & to minecraft colors
     * @param message Message to fix
     * @return Returns ready message
     */
    public static String fixColor(String message){
        message = message.replace("&","§");
        return message;
    }

    /**
     * Sends colored message to online Players
     * @param message Message to send
     */
    public static void broadcast(String message){
        for(Player player : Bukkit.getOnlinePlayers()){
            ChatUtils.sendMessage(player, message);
        }
    }

    /**
     * Sends colored message to Player
     * @param player Player that receives message
     * @param message Message to send
     */
    public static void sendMessage(Player player, String message){
        player.sendMessage(fixColor(prefix + message));
    }

    public static void sendSyncMessage(Player player, String message){
        Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.sendMessage(fixColor(prefix + message));
            }
        });
    }

}
