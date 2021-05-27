package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.trollcraft.Skyblock.PermissionStorage;
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
            if( player.hasPermission("" + PermissionStorage.thisIsSpawn)) {

                if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    if (player.getLocation().getY() < 0) {
                        event.setCancelled(true);
                        player.teleport(Storage.spawn);
                    }
                }

            }
        }

    }
}
