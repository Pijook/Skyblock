package pl.trollcraft.Skyblock.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.bungeeIsland.BungeeIsland;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class IslandsController {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    private HashMap<UUID, Island> islands = new HashMap<>();

    /**
     * Adds island to list of list
     * @param island Island to add
     */
    public void addIsland(UUID islandID, Island island){
        islands.put(islandID, island);
        RedisSupport.saveIsland(islandID);
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

        dim[1] = island.getPoint1().getX();
        dim[2] = island.getPoint2().getX();
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

        Player player = Bukkit.getServer().getPlayer(island.getOwner());

        if(!player.getName().equalsIgnoreCase(toIgnore)){
            if(player != null && player.isOnline()){
                Debug.log("&cOwner is online");
                return true;
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
                island.getIslandLevel()
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
                new Location(Bukkit.getWorld(center.getWorld()), center.getX(), center.getY(), center.getZ()),
                new Location(Bukkit.getWorld(home.getWorld()), home.getX(), home.getY(), home.getZ()),
                bungeeIsland.getIslandLevel(),
                new Location(
                        Bukkit.getWorld(point1.getWorld()),
                        point1.getX(),
                        point1.getY(),
                        point1.getZ()),
                new Location(Bukkit.getWorld(point2.getWorld()), point2.getX(), point2.getY(), point2.getZ())
        );

        return island;
    }


    public void initTimer(){
        Main.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                syncIslands();
            }
        }, 60L, 12000L);
    }

    public void syncIslands(){
        Debug.log("&aSyncing islands...");
        ArrayList<UUID> toRemove = new ArrayList<>();

        for(UUID uuid : islands.keySet()){

            RedisSupport.saveIsland(uuid);

            if(!hasIslandOnlineMembers(uuid, null)){
                toRemove.add(uuid);
            }

        }

        for(UUID uuid : toRemove){
            Debug.log("&cRemoving from memory island " + uuid + "...");
            islands.remove(uuid);
        }

        Debug.log("&aFinished!");

    }
}
