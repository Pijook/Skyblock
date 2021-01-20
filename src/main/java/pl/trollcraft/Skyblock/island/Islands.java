package pl.trollcraft.Skyblock.island;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;

import java.util.HashMap;

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


    }

    public static void checkIslands(String playerName){
        SkyblockPlayer skyblockPlayer = SkyblockPlayers.getPlayer(playerName);

        if(skyblockPlayer.getIslandID() != null){
            if(!islands.containsKey(playerName)){
//
            }
        }


    }


}
