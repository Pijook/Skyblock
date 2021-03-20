package pl.trollcraft.Skyblock.islandUpgrades;

import org.bukkit.Location;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;

import java.util.UUID;

public class IslandSizeUpgrade {

    private static final IslandsController islandsController = Skyblock.getIslandsController();

    public static boolean upgradeSize(UUID islandID){

        Island island = islandsController.getIslandById( islandID );

        int level = island.getIslandLevel();
        Location location = island.getCenter();

        if( level == 1){
            Debug.log("&4=====================");
            Debug.log("&4Poziom jest rowny 1");
            Debug.log("&4islandID: " + islandID);
            Debug.log("&4Wlasciciel: " + island.getOwner());
            Debug.log("&4=====================");
            return false;
        }
        int startSize = Storage.startSize;
        int maxSize = Storage.maxSize;
        int size = startSize + ( (level-1) * 10 );

        if( size > maxSize ){
            Debug.log("&4=====================");
            Debug.log("&4Rozmiar poza skala");
            Debug.log("&4islandID: " + islandID);
            Debug.log("&4Wlasciciel: " + island.getOwner());
            Debug.log("&4=====================");
            return false;
        }

        Location point1 = new Location(location.getWorld() , location.getX() - ((double)size/2), 0, location.getZ() - ((double)size/2));
        Location point2 = new Location(location.getWorld() , location.getX() + ((double)size/2), 255, location.getZ() + ((double)size/2));

        island.setPoint1(point1);
        island.setPoint2(point2);

        return true;
    }


}
