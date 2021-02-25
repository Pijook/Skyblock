package pl.trollcraft.Skyblock.redisSupport;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.UUID;

public class RedisSupport {

    private static final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();
    private static final IslandsController islandsController = Main.getIslandsController();

    /**
     * Loads skyblockPlayer from redis base
     * @param player Player to load
     */
    public static void loadPlayer(Player player){

        ChatUtils.sendMessage(player, "&f&lLoading stats...");

        new BukkitRunnable(){

            @Override
            public void run() {

                String nickname = player.getName();

                String code = Storage.redisCode;
                code = code.replace("%player%", nickname);

                String playerJSON = Main.getJedis().hget(code, "player");

                skyblockPlayerController.addPlayer(nickname, stringToPlayer(playerJSON));

                sendMessage(player, "&a&lLoaded stats!");
                skyblockPlayerController.debugPlayers();

            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 20L);

    }

    /**
     * Saves skyblockPlayer to redis
     * @param player Player to save
     */
    public static void savePlayer(Player player){
        String nickname = player.getName();

        Debug.log("Saving player " + nickname + "...");
        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(nickname);

        String code = getCode(nickname);

        String playerJSON = playerToString(skyblockPlayer);
        Debug.log("JSON:" + playerJSON);
        Main.getJedis().hset(code, "player", playerJSON);
    }

    /**
     * Loads island from redis
     * @param islandID ID of island to load
     */
    public static void loadIsland(UUID islandID){
        Debug.log("Loading island " + islandID + "...");
        new BukkitRunnable(){

            @Override
            public void run(){

                String redisCode = getIslandCode(islandID.toString());

                String islandJSON = Main.getJedis().hget(redisCode, "island");

                Island island = stringToIsland(islandJSON);

                islandsController.addIsland(islandID, island);
                Debug.log("Loaded island" + islandID + "!");
            }

        }.runTaskLaterAsynchronously(Main.getInstance(), 20L);
    }

    /**
     * Saves island to redis
     * @param islandID ID of island to save
     */
    public static void saveIsland(UUID islandID){
        Debug.log("Saving island " + islandID + "...");

        Island island = islandsController.getIslandById(islandID);

        String islandJSON = islandToString(island);

        Main.getJedis().hset(getIslandCode(islandID.toString()), "island", islandJSON);
    }

    /**
     * Converts player to string
     * @param player Skyblockplayer to convert
     * @return Ready string
     */
    public static String playerToString(SkyblockPlayer player){
        Gson gson = Main.getGson();
        return gson.toJson(player);
    }

    /**
     * Converts string to player
     * @param json String to convert
     * @return Ready skyblockPlayer
     */
    public static SkyblockPlayer stringToPlayer(String json){
        Gson gson = Main.getGson();
        return gson.fromJson(json, SkyblockPlayer.class);
    }

    /**
     * Converts SkyblockPlayer object to json string
     * @param island object to convert
     * @return Converted object to string
     */
    public static String islandToString(Island island){
        Gson gson = Main.getGson();
        return gson.toJson(island);

    }

    /**
     * Converts json string to Island object
     * @param json String to convert
     * @return Converted string to object
     */
    public static Island stringToIsland(String json){
        Gson gson = Main.getGson();
        return gson.fromJson(json, Island.class);
    }

    /**
     * Sends delayed synced messaged to player
     * @param player Receiver
     * @param message Message to send
     */
    private static void sendMessage(Player player, String message){
        Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                ChatUtils.sendMessage(player, message);
            }
        });
    }

    /**
     * Returns ready code to player in redis
     * @param nickname Nickname of player to insert into code
     * @return Ready to use redis code
     */
    public static String getCode(String nickname){
        String code = Storage.redisCode;
        code = code.replace("%player%", nickname);
        return code;
    }

    /**
     * Returns ready code to island in redis
     * @param islandID ID of island to insert into code
     * @return Ready to use redis code
     */
    public static String getIslandCode(String islandID){
        String code = Storage.islandCode;
        code = code.replace("%id%", islandID);
        return code;
    }
}
