package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;

import java.util.UUID;

public class EntitySpawnListener implements Listener {

    private final LimitController limitController = Skyblock.getLimitController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){

        Entity entity = event.getEntity();
        if(!(Storage.serverName.equalsIgnoreCase("sblobby"))) {
            Debug.log("&5Spawnuje " + entity.getName());
        }

        String type = entity.getType().name();
        if(limitController.isTypeLimited(type)){
            Location location = entity.getLocation();

            UUID islandID = islandsController.getIslandIDByLocation(location);

            if(islandID == null){
                return;
            }

            if(limitController.isAboveLimit(type, islandID)){
                Debug.log("&5Anulowalem spawn " + type);
                event.setCancelled(true);
            }
            else{
                limitController.increaseType(type, islandID);
            }
        }
    }
}
