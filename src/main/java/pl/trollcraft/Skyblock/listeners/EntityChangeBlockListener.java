package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.limiter.Limiter;

import java.util.UUID;

public class EntityChangeBlockListener implements Listener {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final LimitController limitController = Skyblock.getLimitController();

    @EventHandler
    public void onChange(EntityChangeBlockEvent event){


        if(event.getEntity().getType().equals(EntityType.VILLAGER)){
            event.setCancelled(true);
        }

        if(event.getBlock().getType().equals(Material.FARMLAND)){
            if(!event.getEntity().getType().equals(EntityType.PLAYER)){
                event.setCancelled(true);
            }
        }

        /*Block block = event.getBlock();

        UUID islandID = islandsController.getIslandIDByLocation(block.getLocation());

        if(islandID != null){

            IslandLimiter limiter = limitController.getLimiter(islandID);

            if(event.getTo().isAir()){
                if(limitController.isTypeLimited(block.getType().name())){
                    limiter.decreaseAmount(block.getType().name(), 1);
                    return;
                }
            }
            if(limitController.isTypeLimited(event.getTo().name())){
                limiter.increaseAmount(block.getType().name(), 1);
                return;
            }

        }*/

    }
}
