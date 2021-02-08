package pl.trollcraft.Skyblock.bungeeSupport;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import pl.trollcraft.Skyblock.Storage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class BungeeListener implements PluginMessageListener {

    public void onPluginMessageReceived(String channel, Player player, byte[] bytes){

        if(!channel.equalsIgnoreCase("BungeeCord")){
            return;
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        DataInputStream in = new DataInputStream(stream);

        String subChannel = null;

        try {
            subChannel = in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!subChannel.equalsIgnoreCase(Storage.channel)){
            return;
        }


        try {
            BungeeSupport.matchBungeeCommand(in.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
