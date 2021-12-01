package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;

import java.util.UUID;

public class EntitySpawnListener implements Listener {

    private final LimitController limitController = Skyblock.getLimitController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
//        if( Storage.serverName.equalsIgnoreCase("sblobby")){
        if( Storage.isSpawn ){
            return;
        }

        Entity entity = event.getEntity();

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
