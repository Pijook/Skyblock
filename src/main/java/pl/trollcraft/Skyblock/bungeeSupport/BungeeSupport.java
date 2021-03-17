package pl.trollcraft.Skyblock.bungeeSupport;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.listeners.customListeners.PlayerLoadListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BungeeSupport {

    /**
     * Sends message via bungee
     * @param message Message to send
     */
    public static void sendMessage(String message, Player player){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(Storage.channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(Skyblock.getInstance(), "BungeeCord", stream.toByteArray());
        //Skyblock.getInstance().getServer().sendPluginMessage(Skyblock.getInstance(), "BungeeCord", stream.toByteArray());

        Debug.log("Sent message to bungee!");
    }

    /**
     * Loads configuration to communicate from bungeeconfig.yml
     */
    public static void loadConfiguration(){

        YamlConfiguration configuration = ConfigUtils.load("bungeeconfig.yml", Skyblock.getInstance());

        Storage.serverName = configuration.getString("serverName");
        Storage.channel = configuration.getString("channel");

    }

    /**
     * Matches signal from bungee to specified task
     * @param command Command to match
     */
    public static void matchBungeeCommand(String command){

        String[] commands = command.split(":");

        if(commands.length == 2){
            if(commands[0].equalsIgnoreCase("generateIsland")){
                Player player = Bukkit.getPlayer(commands[1]);
                Debug.log("Received message to generate island!");
                if(!Skyblock.getSkyblockPlayerController().isPlayerLoaded(commands[1])){
                    Debug.log("&cPlayer is not loaded! Adding to queue...");
                    PlayerLoadListener.toGenerate.add(commands[1]);
                }
                else if(player == null || !player.isOnline()){
                    Debug.log("&cPlayer is offline! Adding to queue...");
                    PlayerLoadListener.toGenerate.add(commands[1]);
                }
                else{
                    Debug.log("&aPlayer is online! Creating...");
                    CreateIsland.createNew(player);
                }

            }
            if(commands[0].equalsIgnoreCase("removeMember")){
                String memberNickname = commands[1];
                Skyblock.getSkyblockPlayerController().getPlayer(memberNickname).setIslandID(null);
                ChatUtils.sendMessage(Bukkit.getPlayer(memberNickname), "&cZostales usuniety z wyspy");
            }
        }
    }

    /**
     * Sends signal to generate island
     * @param player Owner of new island
     */
    public static void sendGenerateIslandCommand(Player player){
        String command = "generateIsland:" + player.getName();
        Debug.log("Sending message to generate island!");
        sendMessage(command, player);
    }

    /**
     * Sends signal to delete island
     * @param islandID ID of island to delete
     */
    public static void sendDeleteIslandCommand(UUID islandID, Player player){
        String command = "deleteIsland:" + islandID;
        sendMessage(command, player);
    }

    public static void sendRemoveMemberCommand(String memberNickname, Player player){
        String command = "removeMember:" + memberNickname;
        sendMessage(command, player);
    }

    public static void sendIslancSyncCommand(String islandID, Player player){
        Debug.log("Sending command to sync island");
        String command = "syncIsland:" + islandID;
        sendMessage(command, player);
    }
}
