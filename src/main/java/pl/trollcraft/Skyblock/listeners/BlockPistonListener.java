package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;

public class BlockPistonListener implements Listener {

    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler(priority = EventPriority.LOW)
    public void onPistonMove(BlockPistonExtendEvent event){
        Block block = event.getBlock();
        Island island = islandsController.getIslandByLocation(block.getLocation());

        if(island == null){
            block.setType(Material.AIR);
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPistonMove(BlockPistonRetractEvent event){
        Block block = event.getBlock();
        Island island = islandsController.getIslandByLocation(block.getLocation());

        if(island == null){
            block.setType(Material.AIR);
            event.setCancelled(true);
            return;
        }
    }
}
