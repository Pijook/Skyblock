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

public class BungeeSupport {

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

    public static void loadConfiguration(){

        YamlConfiguration configuration = ConfigUtils.load("bungeeconfig.yml", Main.getInstance());

        Storage.serverName = configuration.getString("serverName");
        Storage.channel = configuration.getString("channel");

    }

    public static void matchBungeeCommand(String command){

        String[] commands = command.split(":");

        if(commands.length == 2){
            if(commands[0].equalsIgnoreCase("generateIsland")){
                Player player = Bukkit.getPlayer(commands[1]);

                CreateIsland.createNew(player);
            }
        }
    }

    public static void sendGenerateIslandCommand(Player player){
        String command = "generateIsland:" + player.getName();
        sendMessage(command);
    }
}
