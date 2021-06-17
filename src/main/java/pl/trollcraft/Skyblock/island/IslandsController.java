package pl.trollcraft.Skyblock.island;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.cost.Cost;
import pl.trollcraft.Skyblock.customEvents.RemoveIslandFromMemoryEvent;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.island.bungeeIsland.BungeeIsland;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class IslandsController {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private HashMap<Integer, Cost> levelsCosts = new HashMap<>();
    private long islandCooldown = -1;
    private HashMap<UUID, Island> islands = new HashMap<>();

    public void loadLevelsCosts(){

        YamlConfiguration configuration = ConfigUtils.load("upgradesCost.yml", Skyblock.getInstance());

        for(String islandLevel : configuration.getConfigurationSection("upgrade").getKeys(false)){

            double level = configuration.getDouble("upgrade." + islandLevel + ".requiredLevel");
            double money = configuration.getDouble("upgrade." + islandLevel + ".money");
            levelsCosts.put(Integer.parseInt(islandLevel), new Cost(level, money));

        }

    }

    public boolean canUpgrade(Island island, Player player){
        Debug.log("&cChecking can player upgrade island...");
        if(!levelsCosts.containsKey(island.getIslandLevel() + 1)){
            Debug.log("&4Could not find higher upgrade level! (False)");
            return false;
        }

        double averageLevel = Skyblock.getWorkerController().getWorkerByName(player.getName()).getAverageLevel();

        Cost cost = levelsCosts.get(island.getIslandLevel() + 1);
        if(averageLevel < cost.getPlayerLevel()){
            ChatUtils.sendMessage(player, "&cMasz za maly poziom aby ulepszyc wyspe!");
            Debug.log("&4Player average level is too low! (False)");
            return false;
        }
        if(Skyblock.getEconomy().getBalance(player) < cost.getMoney()){
            ChatUtils.sendMessage(player, "&cMasz za malo pieniedzy aby ulepszyc wyspe!");
            Debug.log("&4Player doesn't have enough money! (False)");
            return false;
        }

        Debug.log("&aEverything is fine! (True)");
        return true;
    }

    public Cost getIslandUpgradeCost(int level){
        return levelsCosts.getOrDefault(level, null);
    }

    /**
     * Adds island to list of list
     * @param island Island to add
     */
    public void addIsland(UUID islandID, Island island, Player player){
        islands.put(islandID, island);
        RedisSupport.saveIsland(player, islandID);
    }

    /**
     * Returns island with given id
     * @param islandID Id of island
     * @return Island to return
     */
    public Island getIslandById(UUID islandID){
        if(!islands.containsKey(islandID)){
            return null;
        }

        return islands.get(islandID);
    }

    /**
     * Returns island of given playerName
     * @param pName name of player who we searching
     * @return Island to return
     */
    public Island getIslandByOwnerOrMember( String pName ){
        for( UUID islandID : islands.keySet() ){
            if( islands.get(islandID).getOwner().equalsIgnoreCase(pName) ){
                return islands.get(islandID);
            }
            if( islands.get(islandID).getMembers().contains(pName) ){
                return islands.get(islandID);
            }
        }
        return null;
    }

    /**
     * Returns islandID of given playerName
     * @param pName name of player who we searching
     * @return IslandID to return
     */
    public UUID getIslandIdByOwnerOrMember( String pName ){
        for( UUID islandID : islands.keySet() ){
            if( islands.get(islandID).getOwner().equalsIgnoreCase(pName) ){
                return islandID;
            }
            if( islands.get(islandID).getMembers().contains(pName) ){
                return islandID;
            }
        }
        return null;
    }

    /**
     * Teleports player to island
     * @param player player who we need to teleport
     * @param islandID id of island where we want to teleport player
     * @return boolean depending on the result
     */
    public boolean tpToIsland( Player player, UUID islandID ){
        if( islands.containsKey(islandID) && player.isOnline() ) {
            Location islandHome = islands.get(islandID).getHome();
            player.teleport(islandHome);
            return true;
        }
        return false;
    }

    /**
     * Returns island that contains location
     * @param location Location to check
     * @return Island if location is inside island or null if it doesn't
     */
    public Island getIslandByLocation(Location location){
        double[] dim = new double[2];

        for(UUID uuid : islands.keySet()){
            Island island = islands.get(uuid);

            dim[0] = island.getPoint1().getX();
            dim[1] = island.getPoint2().getX();
            Arrays.sort(dim);

            if(location.getX() > dim[1] || location.getX() < dim[0]){
                continue;
            }

            dim[0] = island.getPoint1().getZ();
            dim[1] = island.getPoint2().getZ();
            Arrays.sort(dim);

            if(location.getZ() > dim[1] || location.getZ() < dim[0]){
                continue;
            }

            return island;
        }

        return null;
    }

    public boolean isPlayerOnIsland(Player player, Island island){
        Location location = player.getLocation();
        double[] dim = new double[2];
        dim[0] = island.getPoint1().getX();
        dim[1] = island.getPoint2().getX();
        Arrays.sort(dim);

        if(location.getX() > dim[1] || location.getX() < dim[0]){
            return false;
        }

        dim[0] = island.getPoint1().getZ();
        dim[1] = island.getPoint2().getZ();
        Arrays.sort(dim);

        if(location.getZ() > dim[1] || location.getZ() < dim[0]){
            return false;
        }

        return true;
    }

    public UUID getIslandIDByLocation(Location location){
        double[] dim = new double[2];

        for(UUID uuid : islands.keySet()){
            Island island = islands.get(uuid);

            dim[0] = island.getPoint1().getX();
            dim[1] = island.getPoint2().getX();
            Arrays.sort(dim);

            if(location.getX() > dim[1] || location.getX() < dim[0]){
                continue;
            }

            dim[0] = island.getPoint1().getZ();
            dim[1] = island.getPoint2().getZ();
            Arrays.sort(dim);

            if(location.getZ() > dim[1] || location.getZ() < dim[0]){
                continue;
            }

            return uuid;
        }

        return null;
    }

    /**
     *
     * @return List of islands
     */
    public HashMap<UUID, Island> getIslands(){
        return islands;
    }


    /**
     * Checks does player is on his or coop island
     * @param player Player to check
     * @return true if player is on his island
     */
    public boolean isPlayerOnHisIsland(Player player){
        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        Island island = null;

        if(skyblockPlayer.hasIsland()){
            island = getIslandById(skyblockPlayer.getIslandID());
        }
        else if(skyblockPlayer.hasCOOP()){
            island = getIslandById(skyblockPlayer.getCoopIslandID());
        }

        if(island == null){
            return false;
        }

        double[] dim = new double[2];

        dim[0] = island.getPoint1().getX();
        dim[1] = island.getPoint2().getX();
        Arrays.sort(dim);

        if(player.getLocation().getX() > dim[1] || player.getLocation().getX() < dim[0]){
            return false;
        }

        dim[0] = island.getPoint1().getZ();
        dim[1] = island.getPoint2().getZ();
        Arrays.sort(dim);

        if(player.getLocation().getZ() > dim[1] || player.getLocation().getZ() < dim[0]){
            return false;
        }

        return true;
    }

    public boolean isLocationOnIsland(Location location, UUID islandID){

        if(islandID == null){
            return false;
        }

        Island island = getIslandById(islandID);

        double[] dim = new double[2];

        dim[0] = island.getPoint1().getX();
        dim[1] = island.getPoint2().getX();
        Arrays.sort(dim);

        if(location.getX() > dim[1] || location.getX() < dim[0]){
            return false;
        }

        dim[0] = island.getPoint1().getZ();
        dim[1] = island.getPoint2().getZ();
        Arrays.sort(dim);

        if(location.getZ() > dim[1] || location.getZ() < dim[0]){
            return false;
        }

        return true;
    }

    /**
     * Checks is player owner of island
     * @param owner Nickname of player to check
     * @return true if player has island or coop
     */
    public boolean isPlayerOwner(String owner){

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(owner);

        UUID islandID = skyblockPlayer.getIslandOrCoop();

        if(islandID == null){
            return false;
        }

        if(islands.containsKey(islandID)){
            return true;
        }
        else{

            //TODO Sprawdzenie czy gracz jest wlascicielem jesli wyspa nie jest zaladowana

            return false;
        }
    }

    /**
     * Checks if player is member of island
     * @param member Nickname of player to check
     * @return true if player is member
     */
    public boolean isPlayerMember( String member ){
        for(UUID islandID : islands.keySet()){
            if(islands.get(islandID).getMembers().contains(member)){
                return true;
            }
        }
        //TODO Sprawdzenie czy gracz jest czlonkiem jesli wyspa nie jest zaladowana
        return false;
    }

    /**
     * Adds member to island
     * @param owner Nickname of owner of island
     * @param member Nickname of member to add
     */
    public void addMember(String owner, String member ){
        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(owner);

        getIslandById(skyblockPlayer.getIslandOrCoop()).addMember(member);
    }

    /**
     * Removes member from island
     * @param owner Nickname of owner of island
     * @param member Nickname of member to remove
     */
    public void remMember( String owner, String member ){
        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(owner);

        getIslandById(skyblockPlayer.getIslandOrCoop()).removeMember(member);
    }

    /**
     * Checks if island is loaded to server memore
     * @param islandID ID of island to check
     * @return true if island is loaded
     */
    public boolean isIslandLoaded(UUID islandID){
        if(islands.containsKey(islandID)){
            return true;
        }
        return false;
    }

    /**
     * Checks if island has any online members
     * @param islandID ID of island to check
     * @return true if island has at least one online member
     */
    public boolean hasIslandOnlineMembers(UUID islandID, String toIgnore){
        Debug.log("&aChecking does island has online members");
        Island island = getIslandById(islandID);

        String owner = island.getOwner();

        Player player = Bukkit.getServer().getPlayer(owner);

        if(player != null){
            if(!player.getName().equalsIgnoreCase(toIgnore)){
                if(player.isOnline()){
                    Debug.log("&cOwner is online");
                    return true;
                }
            }
        }


        for(String nickname : island.getMembers()){
            player = Bukkit.getServer().getPlayer(nickname);

            if(nickname.equalsIgnoreCase(toIgnore)){
                continue;
            }

            if(player != null && player.isOnline()){
                Debug.log("&cMember " + nickname + " is online");
                return true;
            }
        }

        Debug.log("&cAll members are offline");
        return false;
    }

    public BungeeIsland convertIslandToBungeeIsland(Island island){
        Location center = island.getCenter();
        Location home = island.getHome();
        Location point1 = island.getPoint1();
        Location point2 = island.getPoint2();
        BungeeIsland bungeeIsland = new BungeeIsland(
                island.getOwner(),
                island.getMembers(),
                new pl.trollcraft.Skyblock.island.bungeeIsland.Location(center.getWorld().getName(), center.getX(), center.getY(), center.getZ()),
                new pl.trollcraft.Skyblock.island.bungeeIsland.Location(home.getWorld().getName(), home.getX(), home.getY(), home.getZ()),
                island.getIslandLevel(),
                new pl.trollcraft.Skyblock.island.bungeeIsland.Location(point1.getWorld().getName(),point1.getX(), point1.getY(), point1.getZ()),
                new pl.trollcraft.Skyblock.island.bungeeIsland.Location(point2.getWorld().getName(),point2.getX(), point2.getY(), point2.getZ()),
                island.getServer(),
                island.getPoints()
        );

        bungeeIsland.setPoint1(new pl.trollcraft.Skyblock.island.bungeeIsland.Location(point1.getWorld().getName(),point1.getX(), point1.getY(), point1.getZ()));
        bungeeIsland.setPoint2(new pl.trollcraft.Skyblock.island.bungeeIsland.Location(point2.getWorld().getName(),point2.getX(), point2.getY(), point2.getZ()));

        return bungeeIsland;
    }

    public Island convertBungeeIslandToIsland(BungeeIsland bungeeIsland){
        pl.trollcraft.Skyblock.island.bungeeIsland.Location center = bungeeIsland.getCenter();
        pl.trollcraft.Skyblock.island.bungeeIsland.Location home = bungeeIsland.getHome();
        pl.trollcraft.Skyblock.island.bungeeIsland.Location point1 = bungeeIsland.getPoint1();
        pl.trollcraft.Skyblock.island.bungeeIsland.Location point2 = bungeeIsland.getPoint2();

        Island island = new Island(
                bungeeIsland.getOwner(),
                bungeeIsland.getMembers(),
                new Location(Bukkit.getWorld("Islands"),
                        center.getX(),
                        center.getY(),
                        center.getZ()),
                new Location(Bukkit.getWorld("Islands"),
                        home.getX(),
                        home.getY(),
                        home.getZ()),
                bungeeIsland.getIslandLevel(),
                new Location(
                        Bukkit.getWorld(point1.getWorld()),
                        point1.getX(),
                        point1.getY(),
                        point1.getZ()),
                new Location(Bukkit.getWorld("Islands"),
                        point2.getX(),
                        point2.getY(),
                        point2.getZ()),
                bungeeIsland.getServer(),
                bungeeIsland.getPoints()
        );

        return island;
    }


    public void initTimer(){
        Skyblock.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                syncIslands();
            }
        }, 60L, 12000L);
    }

    public void syncIslands(){
        Debug.log("&aSyncing islands...");
        ArrayList<UUID> toRemove = new ArrayList<>();


        if(Bukkit.getOnlinePlayers().size() > 0){

            Player player = null;

            for(Player t : Bukkit.getOnlinePlayers()){
                player = t;
                break;
            }

            for(UUID uuid : islands.keySet()){

                RedisSupport.saveIsland(player, uuid);

                if(!hasIslandOnlineMembers(uuid, null)){
                    toRemove.add(uuid);
                }

            }

            for(UUID uuid : toRemove){
                Debug.log("&cRemoving from memory island " + uuid + "...");
                RemoveIslandFromMemoryEvent removeIslandFromMemoryEvent = new RemoveIslandFromMemoryEvent(uuid, getIslandById(uuid));
                Bukkit.getPluginManager().callEvent(removeIslandFromMemoryEvent);
                islands.remove(uuid);
            }
        }


        Debug.log("&aFinished!");

    }

    public void setGeneratorOnCooldown(){
        this.islandCooldown = System.currentTimeMillis();
    }

    public boolean isGeneratorOnCooldown(){
        if(islandCooldown == -1){
            return false;
        }

        if(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - TimeUnit.MILLISECONDS.toSeconds(islandCooldown) >= 5){
            return false;
        }
        return true;
    }

    public void initTopChecking(){
        Skyblock.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                checkTop();

            }
        }, 60L, 72000L);

    }

    public void checkTop(){
        String topJSON = Skyblock.getJedis().get("skyblock:topisland");
        Debug.log(topJSON);
        Storage.islandsTop = Skyblock.getGson().fromJson(topJSON, LinkedHashMap.class);

        if (Storage.topHologram == null) {
            Storage.topHologram = HologramsAPI.createHologram(Skyblock.getInstance(), Storage.topLocation);
        }

        Storage.topHologram.clearLines();

        int i = 1;
        Storage.topHologram.insertTextLine(0, ChatUtils.fixColor("&e&lTopka Wysp"));
        for(String owner : Storage.islandsTop.keySet()){
            String line = "&e&l" + i + ".&e " + owner + "&7: " + Storage.islandsTop.get(owner);
            line = ChatUtils.fixColor(line);
            Storage.topHologram.insertTextLine(i, line);
            i++;
        }
    }

    public long getIslandCreateCooldown(String nickname){
        YamlConfiguration configuration = ConfigUtils.loadFromNetworkGlobalFolder("createIslandCooldown.yml");

        if(!configuration.contains("cooldowns." + nickname)){
            return -1;
        }
        else{
            long playerCooldown = configuration.getLong("cooldowns." + nickname);
            long difference = System.currentTimeMillis() - playerCooldown;
            return TimeUnit.MILLISECONDS.toSeconds(difference);
        }
    }

    public void saveCooldown(String nickname){
        YamlConfiguration configuration = ConfigUtils.loadFromNetworkGlobalFolder("createIslandCooldown.yml");
        configuration.set("cooldowns." + nickname, System.currentTimeMillis());
        ConfigUtils.saveToNetworkGlobalFolder(configuration, "createIslandCooldown.yml");
    }
}
