package pl.trollcraft.Skyblock.redisSupport;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.customEvents.IslandLoadEvent;
import pl.trollcraft.Skyblock.customEvents.IslandSaveEvent;
import pl.trollcraft.Skyblock.customEvents.PlayerLoadEvent;
import pl.trollcraft.Skyblock.customEvents.PlayerSaveEvent;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.island.bungeeIsland.BungeeIsland;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;

import java.util.HashMap;
import java.util.UUID;

public class RedisSupport {

    private static final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private static final IslandsController islandsController = Skyblock.getIslandsController();
    private static HashMap<String, Integer> retries = new HashMap<>();

    public static void loadSettings(){
        YamlConfiguration configuration = ConfigUtils.load("datasource.yml", Skyblock.getInstance());

        Storage.redisAddress = configuration.getString("redis.host");
    }

    /**
     * Loads skyblockPlayer from redis base
     * @param player Player to load
     */
    public static void loadPlayer(Player player){

        //Debug.log("&aLOADING PLAYER FROM REDIS:  " + player.getName());
        ChatUtils.sendMessage(player, "&f&lLoading " + player.getName() + " stats...");

        String nickname = player.getName();

        String code = Storage.redisCode;
        code = code.replace("%player%", nickname);

        String playerJSON = Skyblock.getJedis().hget(code, "player");

        Debug.log("&aJSON: " + playerJSON);

        SkyblockPlayer skyblockPlayer = stringToPlayer(playerJSON);

        if(skyblockPlayer.hasIslandOrCoop()){
            skyblockPlayerController.addUncheckedPlayer(player);
        }

        skyblockPlayerController.addPlayer(nickname, skyblockPlayer);

        sendMessage(player, "&a&lLoaded " + player.getName() + " stats!");
        skyblockPlayerController.debugPlayers();
        PlayerLoadEvent playerLoadEvent = new PlayerLoadEvent(player, skyblockPlayer);
        Bukkit.getPluginManager().callEvent(playerLoadEvent);

    }

    public static SkyblockPlayer getSkyblockPlayer(String nickname){
        String code = RedisSupport.getCode(nickname);

        if(!Skyblock.getJedis().hexists(code, "player")){
            return null;
        }
        else{

            String playerJSON = Skyblock.getJedis().hget(code, "player");
            return stringToPlayer(playerJSON);

        }
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
        if(playerJSON != null){
            Skyblock.getJedis().hset(code, "player", playerJSON);
        }
        else{
            Debug.log("playerJSON is NULL! Skipping...");
        }


        skyblockPlayerController.removePlayer(nickname);
        PlayerSaveEvent playerSaveEvent = new PlayerSaveEvent(player, skyblockPlayer);
        Bukkit.getPluginManager().callEvent(playerSaveEvent);
    }

    /**
     * Loads island from redis
     * @param islandID ID of island to load
     */
    public static void loadIsland(UUID islandID, Player player){
        Debug.log("Loading island " + islandID + "...");

        Island island;

        if(player == null || !player.isOnline()){
            return;
        }

        if(islandsController.isIslandLoaded(islandID)){
            return;
        }

        try{
            String redisCode = getIslandCode(islandID.toString());

            String islandJSON = Skyblock.getJedis().hget(redisCode, "island");

            Debug.log(islandJSON);

            BungeeIsland bungeeIsland = stringToBungeeIsland(islandJSON);

            island = islandsController.convertBungeeIslandToIsland(bungeeIsland);

            if(!island.getOwner().equalsIgnoreCase(player.getName()) && !island.getMembers().contains(player.getName())){
                Debug.log(player.getName() + ": " + skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop());
                BungeeSupport.sendReloadIslandCommand(skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop().toString(), player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        if(retries.containsKey(player.getName())){
                            int amount = retries.get(player.getName());
                            if(amount > 3){
                                Debug.log("&4Giving up");
                                return;
                            }
                            else{
                                retries.put(player.getName(), amount + 1);
                            }
                        }
                        else{
                            retries.put(player.getName(), 1);
                        }
                        Debug.log("Try number " + retries.get(player.getName()) + " to load island!");
                        loadIsland(islandID, player);
                    }
                },  60L);
                return;
            }

            islandsController.addIsland(islandID, island, player);
            Debug.log("Loaded island" + islandID + "!");
            IslandLoadEvent islandLoadEvent = new IslandLoadEvent(islandID, island);
            Bukkit.getPluginManager().callEvent(islandLoadEvent);
            retries.put(player.getName(), 0);
        }
        catch (NullPointerException exception){
            exception.printStackTrace();
            BungeeSupport.sendReloadIslandCommand(islandID.toString(), player);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    loadIsland(islandID, player);
                }
            }, 20L);
        }

    }

    public static Island getIsland(UUID islandID){
        String redisCode = getIslandCode(islandID.toString());

        if(!Skyblock.getJedis().hexists(redisCode, "island")){
            return null;
        }

        String islandJSON = Skyblock.getJedis().hget(redisCode, "island");

        BungeeIsland bungeeIsland = stringToBungeeIsland(islandJSON);

        return islandsController.convertBungeeIslandToIsland(bungeeIsland);
    }

    /**
     * Saves island to redis
     * @param islandID ID of island to save
     */
    public static void saveIsland(Player player, UUID islandID){
        Debug.log("Saving island " + islandID + "...");

        if(player == null || !player.isOnline()){
            return;
        }

        Island island = islandsController.getIslandById(islandID);
        BungeeIsland bungeeIsland = islandsController.convertIslandToBungeeIsland(island);

        String islandJSON = bungeeIslandToString(bungeeIsland);

        Debug.log(islandJSON);

        if(islandJSON == null){
            return;
        }

        Skyblock.getJedis().hset(getIslandCode(islandID.toString()), "island", islandJSON);

        BungeeSupport.sendIslancSyncCommand(islandID.toString(), player);

        IslandSaveEvent islandSaveEvent = new IslandSaveEvent(islandID, island);
        Bukkit.getPluginManager().callEvent(islandSaveEvent);
    }

    /**
     * Converts player to string
     * @param player Skyblockplayer to convert
     * @return Ready string
     */
    public static String playerToString(SkyblockPlayer player){
        /*Gson gson = Skyblock.getGson();
        return gson.toJson(player);*/
        return Skyblock.getObjectConverter().playerToString(player);
    }

    /**
     * Converts string to player
     * @param json String to convert
     * @return Ready skyblockPlayer
     */
    public static SkyblockPlayer stringToPlayer(String json){
        /*Gson gson = Skyblock.getGson();
        return gson.fromJson(json, SkyblockPlayer.class);*/
        return Skyblock.getObjectConverter().stringToPlayer(json);
    }

    /**
     * Converts SkyblockPlayer object to json string
     * @param island object to convert
     * @return Converted object to string
     */
    public static String islandToString(Island island){
        /*Gson gson = Skyblock.getGson();
        return gson.toJson(island);*/
        return Skyblock.getObjectConverter().IslandToString(island);

    }

    /**
     * Converts json string to Island object
     * @param json String to convert
     * @return Converted string to object
     */
    public static Island stringToIsland(String json){
        /*Gson gson = Skyblock.getGson();
        return gson.fromJson(json, Island.class);*/
        return Skyblock.getObjectConverter().stringToIsland(json);
    }

    public static String bungeeIslandToString(BungeeIsland bungeeIsland){
        /*Gson gson = Skyblock.getGson();
        return gson.toJson(bungeeIsland);*/
        return Skyblock.getObjectConverter().bungeeIslandToString(bungeeIsland);
    }

    public static BungeeIsland stringToBungeeIsland(String json){
        /*Gson gson = Skyblock.getGson();
        return gson.fromJson(json, BungeeIsland.class);*/
        return Skyblock.getObjectConverter().stringToBungeeIsland(json);
    }

    public static String workerToString(Worker worker){
        /*Gson gson = Skyblock.getGson();
        return gson.toJson(worker);*/
        return Skyblock.getObjectConverter().workerToString(worker);
    }

    public static Worker stringToWorker(String string){
        /*Gson gson = Skyblock.getGson();
        return gson.fromJson(string, Worker.class);*/
        return Skyblock.getObjectConverter().stringToWorker(string);
    }

    /**
     * Sends delayed synced messaged to player
     * @param player Receiver
     * @param message Message to send
     */
    private static void sendMessage(Player player, String message){
        Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
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
