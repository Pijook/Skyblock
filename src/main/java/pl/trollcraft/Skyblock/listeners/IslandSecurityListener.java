package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class IslandSecurityListener implements Listener {

    private static SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private static IslandsController islandsController = Skyblock.getIslandsController();


    @EventHandler(priority = EventPriority.LOW)
    public void onPickup(PlayerPickupItemEvent event){

        if(Storage.serverName.equalsIgnoreCase("sblobby")){
            return;
        }

        Player player = event.getPlayer();
        if(!islandsController.isPlayerOnHisIsland(player)){
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if(Storage.serverName.equalsIgnoreCase("sblobby")){
            return;
        }

        if(event.getDamager().getType().equals(EntityType.PLAYER)){
            Player player = (Player) event.getDamager();

            if(!islandsController.isPlayerOnHisIsland(player)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event){

        if(Storage.serverName.equalsIgnoreCase("sblobby")){
            return;
        }

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            event.setCancelled(true);
        }

    }
}
