package pl.trollcraft.Skyblock.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Islands {

    private static HashMap<String, Island> islands = new HashMap<>();

    /**
     * Adds island to list of list
     * @param island Island to add
     */
    public static void addIsland(String owner, Island island){
        islands.put(owner, island);
    }

    /**
     * Returns island with given id
     * @param owner Id of island
     * @return Island to return
     */
    public static Island getIslandById(String owner){
        if(!islands.containsKey(owner)){
            return null;
        }

        return islands.get(owner);
    }

    /**
     * To do
     * Not finished
     * @param location
     * @return
     */
    public static Island getIslandByLocation(Location location){
        //To do

        return null;
    }

    public static HashMap<String, Island> getIslands(){
        return islands;
    }

    public static void loadIsland(String playerName){
        YamlConfiguration configuration = ConfigUtils.load("islands.yml", Main.getInstance());
        //////Added by Lucyfer
        for( String owner : configuration.getConfigurationSection("islands").getKeys(false)){
            ArrayList<String> members = new ArrayList<>();
            if( configuration.getStringList("islands." + owner + ".members") != null){
                members = (ArrayList<String>) configuration.getStringList("islands." + owner + ".members");
            }
            double spawnX = configuration.getLong("islands." + owner + ".spawn.x");
            double spawnY = configuration.getLong("islands." + owner + ".spawn.y");
            double spawnZ = configuration.getLong("islands." + owner + ".spawn.z");
            World spawnWorld = Bukkit.getWorld(Objects.requireNonNull(configuration.getString("islands." + owner + ".spawn.world")));
            Location islandSpawn = null;
            islandSpawn = new Location(spawnWorld, spawnX, spawnY, spawnZ);

            double centerX = configuration.getLong("islands." + owner + ".spawn.x");
            double centerY = configuration.getLong("islands." + owner + ".spawn.y");
            double centerZ = configuration.getLong("islands." + owner + ".spawn.z");
            World centerWorld = Bukkit.getWorld(Objects.requireNonNull(configuration.getString("islands." + owner + ".center.world")));
            Location islandCenter = null;
            islandCenter = new Location(centerWorld, centerX, centerY, centerZ);

            int islandLevel = 1;
            islandLevel = configuration.getInt("islands." + owner + "level");

            islands.put(owner, new Island(owner, members, islandCenter, islandSpawn, islandLevel));
        }
        //End of Lucyfer changes

    }

    public static void checkIslands(String playerName){
        SkyblockPlayer skyblockPlayer = SkyblockPlayers.getPlayer(playerName);

        if(skyblockPlayer.getIslandID() != null){
            if(!islands.containsKey(playerName)){
//
            }
        }


    }


    public static boolean isPlayerOwner( String owner ){
        if( islands.containsKey(owner) ){
            return true;
        }
        else{
            return false;
        }
    }

    public static void addMember( String owner, String member ){
        ArrayList<String> mbrs = islands.get(owner).getMembers();
        mbrs.add(member);
        islands.get(owner).setMembers(mbrs);
    }
    public static void remMember( String owner, String member ){
        ArrayList<String> mbrs = islands.get(owner).getMembers();
        mbrs.remove(member);
        islands.get(owner).setMembers(mbrs);
    }
}
