package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class EntityDamageListener implements Listener {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    @EventHandler
    public void onDamage(EntityDamageEvent event){

        if(event.getEntity().getType().equals(EntityType.PLAYER)){

            Player player = (Player) event.getEntity();

            if(event.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                event.setCancelled(true);
                SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

                if(!skyblockPlayer.hasIslandOrCoop()){
                    player.teleport(Storage.spawn);
                    return;
                }

                if(!islandsController.isIslandLoaded(skyblockPlayer.getIslandOrCoop())){
                    player.teleport(Storage.spawn);
                    return;
                }

                player.teleport(islandsController.getIslandById(skyblockPlayer.getIslandOrCoop()).getHome());
                return;

            }
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                event.setCancelled(true);
            }

        }

    }
}
