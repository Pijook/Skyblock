package pl.trollcraft.Skyblock.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.*;

public class IslandsController {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    private HashMap<UUID, Island> islands = new HashMap<>();

    /**
     * Adds island to list of list
     * @param island Island to add
     */
    public void addIsland(UUID islandID, Island island){
        islands.put(islandID, island);
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
     * To do
     * Not finished
     * @param location
     * @return
     */
    public Island getIslandByLocation(Location location){
        //To do

        return null;
    }

    public HashMap<UUID, Island> getIslands(){
        return islands;
    }

    /*public void loadIsland(String playerName){
        YamlConfiguration configuration = ConfigUtils.load("islands.yml", Main.getInstance());
        //////Better option

        for( String owner : configuration.getConfigurationSection("islands").getKeys(false)){
            List<String> members = new ArrayList<>();
            if( configuration.getList("islands." + owner + ".members") != null){
                members = configuration.getStringList("islands." + owner + ".members");
            }
            if( owner.equalsIgnoreCase(playerName) || members.contains(playerName) ) {
                Location islandSpawn = ConfigUtils.getLocationFromConfig(configuration, "islands." + owner + ".spawn");
                Location islandCenter = ConfigUtils.getLocationFromConfig(configuration, "islands." + owner + ".center");
                Location point1 = ConfigUtils.getLocationFromConfig(configuration, "islands." + owner + ".point1");
                Location point2 = ConfigUtils.getLocationFromConfig(configuration, "islands." + owner + ".point2");
                int islandLevel = configuration.getInt("islands." + owner + "level");
                addIsland(owner, new Island(owner, members, islandCenter, islandSpawn, islandLevel, point1, point2));
                return;
            }
        }

//        for( String owner : configuration.getConfigurationSection("islands").getKeys(false)){
//            ArrayList<String> members = new ArrayList<>();
//            if( configuration.getStringList("islands." + owner + ".members") != null){
//                members = (ArrayList<String>) configuration.getStringList("islands." + owner + ".members");
//            }
//            double spawnX = configuration.getLong("islands." + owner + ".spawn.x");
//            double spawnY = configuration.getLong("islands." + owner + ".spawn.y");
//            double spawnZ = configuration.getLong("islands." + owner + ".spawn.z");
//            World spawnWorld = Bukkit.getWorld(Objects.requireNonNull(configuration.getString("islands." + owner + ".spawn.world")));
//            Location islandSpawn;
//            islandSpawn = new Location(spawnWorld, spawnX, spawnY, spawnZ);
//
//            double centerX = configuration.getLong("islands." + owner + ".spawn.x");
//            double centerY = configuration.getLong("islands." + owner + ".spawn.y");
//            double centerZ = configuration.getLong("islands." + owner + ".spawn.z");
//            World centerWorld = Bukkit.getWorld(Objects.requireNonNull(configuration.getString("islands." + owner + ".center.world")));
//            Location islandCenter = null;
//            islandCenter = new Location(centerWorld, centerX, centerY, centerZ);
//
//            int islandLevel = 1;
//            islandLevel = configuration.getInt("islands." + owner + "level");
//
//            islands.put(owner, new Island(owner, members, islandCenter, islandSpawn, islandLevel));
//        }
    }*/


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
    public boolean isPlayerMember( String member ){
        for(UUID islandID : islands.keySet()){
            if(islands.get(islandID).getMembers().contains(member)){
                return true;
            }
        }
        //TODO Sprawdzenie czy gracz jest czlonkiem jesli wyspa nie jest zaladowana
        return false;
    }

    public void addMember(String owner, String member ){
        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(owner);

        getIslandById(skyblockPlayer.getIslandOrCoop()).addMember(member);
    }

    public void remMember( String owner, String member ){
        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(owner);

        getIslandById(skyblockPlayer.getIslandOrCoop()).removeMember(member);
    }

    public boolean isIslandLoaded(UUID islandID){
        if(islands.containsKey(islandID)){
            return true;
        }
        return false;
    }

    public boolean hasIslandOnlineMembers(UUID islandID){

        Island island = getIslandById(islandID);

        Player player = Bukkit.getServer().getPlayer(island.getOwner());

        if(player != null && player.isOnline()){
            return true;
        }

        for(String nickname : island.getMembers()){
            player = Bukkit.getServer().getPlayer(nickname);

            if(player != null && player.isOnline()){
                return true;
            }
        }

        return false;
    }
}
