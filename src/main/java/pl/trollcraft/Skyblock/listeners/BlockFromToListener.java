package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;

import java.util.UUID;

public class BlockFromToListener implements Listener {

    private final LimitController limitController = Skyblock.getLimitController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler(priority = EventPriority.LOW)
    public void onWaterFromTo(BlockFromToEvent event){

        final Material newBlock = event.getBlock().getType();
        final Material oldBlock = event.getToBlock().getType();

        if(newBlock.equals(Material.WATER) && limitController.isDestroyableByWater(oldBlock.name())){

            UUID islandID = islandsController.getIslandIDByLocation(event.getToBlock().getLocation());
            if(islandID != null){
                limitController.decreaseType(oldBlock.name(), islandID);
            }

        }


    }
}
