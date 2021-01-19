package pl.trollcraft.Skyblock.island;

import org.bukkit.Location;

import java.util.HashMap;

public class Islands {

    private static HashMap<Integer, Island> islands = new HashMap<>();

    /**
     * Adds island to list of list
     * @param island Island to add
     */
    public static void addIsland(int id, Island island){
        islands.put(id, island);
    }

    /**
     * Returns island with given id
     * @param id Id of island
     * @return Island to return
     */
    public static Island getIslandById(int id){
        if(!islands.containsKey(id)){
            return null;
        }

        return islands.get(id);
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

    public static HashMap<Integer, Island> getIslands(){
        return islands;
    }


}
