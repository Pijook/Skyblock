package pl.trollcraft.Skyblock.bungeeSupport;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.listeners.customListeners.PlayerLoadListener;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;

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
        if(commands.length == 3){
            if(commands[0].equalsIgnoreCase("syncHome")){
                UUID islandID = UUID.fromString(commands[1]);

                if(!Skyblock.getIslandsController().isIslandLoaded(islandID)){
                    return;
                }

                Island island = Skyblock.getIslandsController().getIslandById(islandID);

                island.setHome(locationPartToLocation(commands[2]));
            }
            if(commands[0].equalsIgnoreCase("returnIslandInfo")){
                String nickname = commands[1];
                String islandParts = commands[2];

                Player player = Bukkit.getPlayer(nickname);

                if(islandParts.equalsIgnoreCase("null")){
                    ChatUtils.sendMessage(player, "&cNie znaleziono takiej wyspy!");
                    return;
                }
                else{
                    Island island = RedisSupport.stringToIsland(islandParts);

                    ChatUtils.sendMessage(player, "&7Informacje o wyspie gracza &e&l" + island.getOwner());
                    ChatUtils.sendMessage(player, "&7Poziom wyspy: &e" + island.getIslandLevel());
                    ChatUtils.sendMessage(player, "&7Czlonkowie wyspy: &e" + island.getMembers().toString());
                    ChatUtils.sendMessage(player, "&7Sektor: &e" + island.getServer());
                    return;
                }
            }
        }
        if(commands.length == 6){
            if(commands[0].equalsIgnoreCase("tp")){
                String nickname = commands[1];
                if(commands[2].equalsIgnoreCase("null")){
                    Player target = Bukkit.getPlayer(nickname);
                    ChatUtils.sendMessage(target, "&cNie znaleziono wyspy");
                    return;
                }
                else{
                    Player target = Bukkit.getPlayer(nickname);
                    Location location = new Location(
                            Bukkit.getWorld(commands[5]),
                            Double.parseDouble(commands[2]),
                            Double.parseDouble(commands[3]),
                            Double.parseDouble(commands[4])
                    );
                    ChatUtils.sendMessage(target, "&aTeleportowanie...");
                    target.teleport(location);
                    return;
                }
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

    public static void sendReloadIslandCommand(String islandID, Player player){
        Debug.log("Sending command to reload island");
        String command = "reloadIsland:" + islandID + ":" + player.getName();
        sendMessage(command, player);
    }

    public static void sendReloadPlayerCommand(Player player){
        Debug.log("Sending command to reload player");
        String command = "reloadPlayer:" + player.getName();
        sendMessage(command, player);
    }

    public static void sendReloadWorkerCommand(Player player){
        Debug.log("Sending command to reload worker");
        String command = "reloadWorker:" + player.getName();
        sendMessage(command, player);
    }

    public static void sendVisitIslandCommand(Player player, String owner){
        Debug.log("Sending command to visit island");
        String command = "visit:" + player.getName() + ":" + owner;
        sendMessage(command, player);
    }

    public static void sendSyncHomeCommand(UUID islandID, Player player){
        Location home = Skyblock.getIslandsController().getIslandById(islandID).getHome();
        Debug.log("Sending sync homes command");
        String locationPart = home.getWorld().getName() + ";" + home.getX() + ";" + home.getY() + ";" + home.getZ();
        String command = "syncHome:" + islandID + ":" + locationPart;
        sendMessage(command, player);
    }

    public static void sendGetIslandInfoCommand(Player player){
        Location location = player.getLocation();
        Debug.log("Sending get island info command");
        String locationPart = location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
        String command = "getIslandInfo:" + player.getName() + ":" + locationPart;
        sendMessage(command, player);
    }

    private static Location locationPartToLocation(String locationPart){
        String[] parts = locationPart.split(";");
        return new Location(Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }
}
