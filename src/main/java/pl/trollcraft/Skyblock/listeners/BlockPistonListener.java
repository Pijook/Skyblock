package pl.trollcraft.Skyblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.limiter.Limiter;

import java.util.UUID;

public class BlockPistonListener implements Listener {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final LimitController limitController = Skyblock.getLimitController();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonMove(BlockPistonExtendEvent event){

        for(Block block : event.getBlocks()){
            Island island = islandsController.getIslandByLocation(block.getLocation());
            if(island == null) {
                event.setCancelled(true);
                return;
            }

        }

        UUID islandID = islandsController.getIslandIDByLocation(event.getBlocks().get(0).getLocation());

        /*if(island == null){
            block.setType(Material.AIR);
            event.setCancelled(true);
            return;
        }*/
    }

}
