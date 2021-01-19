package pl.trollcraft.Skyblock.island;

import org.bukkit.Location;

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


}
