package pl.trollcraft.Skyblock.bungeeSupport;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.CreateIsland;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BungeeSupport {

    /**
     * Sends message via bungee
     * @param message Message to send
     */
    public static void sendMessage(String message){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(Storage.channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.getInstance().getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", stream.toByteArray());

        Debug.log("Sent message to bungee!");
    }

    /**
     * Loads configuration to communicate from bungeeconfig.yml
     */
    public static void loadConfiguration(){

        YamlConfiguration configuration = ConfigUtils.load("bungeeconfig.yml", Main.getInstance());

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
                CreateIsland.createNew(player);
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
        sendMessage(command);
    }

    /**
     * Sends signal to delete island
     * @param islandID ID of island to delete
     */
    public static void sendDeleteIslandCommand(String islandID){
        String command = "deleteIsland:" + islandID;
        sendMessage(command);
    }
}
