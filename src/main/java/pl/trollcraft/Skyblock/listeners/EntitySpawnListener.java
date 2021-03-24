package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;

import java.util.UUID;

public class EntitySpawnListener implements Listener {

    private final IslandLimiter islandLimiter = Skyblock.getIslandLimiter();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){

        Entity entity = event.getEntity();

        if(islandLimiter.isEntityLimited(entity.getType())){

            Location location = entity.getLocation();

            UUID islandID = islandsController.getIslandIDByLocation(location);

            if(islandID == null){
                return;
            }

            if(islandLimiter.isEntityAboveLimit(islandID, entity.getType())){
                event.setCancelled(true);
            }
            else{
                islandLimiter.addEntity(islandID, entity.getType());
            }
        }
    }
}
