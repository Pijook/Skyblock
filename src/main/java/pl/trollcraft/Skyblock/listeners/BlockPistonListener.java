package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.Debug;
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

        for(Block block : event.getBlocks()){
            if(limitController.isCrop(block.getType().name())){
                limitController.decreaseType(block.getType().name(), islandID);
            }
            if(limitController.isCrop(block.getRelative(BlockFace.UP).getType().name())){
                limitController.decreaseType(block.getType().name(), islandID);
            }
        }

        /*if(island == null){
            block.setType(Material.AIR);
            event.setCancelled(true);
            return;
        }*/
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonRetract(BlockPistonRetractEvent event){

        for(Block block : event.getBlocks()){
            Island island = islandsController.getIslandByLocation(block.getLocation());
            if(island == null) {
                event.setCancelled(true);
                return;
            }

        }

        UUID islandID = islandsController.getIslandIDByLocation(event.getBlocks().get(0).getLocation());

        for(Block block : event.getBlocks()){
            Debug.log(block.getType());
            if(limitController.isCrop(block.getType().name())){
                limitController.decreaseType(block.getType().name(), islandID);
            }
            if(limitController.isCrop(block.getRelative(BlockFace.UP).getType().name())){
                limitController.decreaseType(block.getRelative(BlockFace.UP).getType().name(), islandID);
            }
        }

        /*if(island == null){
            block.setType(Material.AIR);
            event.setCancelled(true);
            return;
        }*/
    }

}
