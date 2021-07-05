package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class PlayerKillPlayerListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler
    public void onKill(PlayerDeathEvent event){

        Player dead = event.getEntity();
        if(event.getEntity().getKiller() != null){
            Player killer = event.getEntity().getKiller();

            skyblockPlayerController.getPlayer(dead.getName()).increaseDeaths(1);
            skyblockPlayerController.getPlayer(killer.getName()).increaseKills(1);
        }

        if(!Storage.isSpawn){
            if(!islandsController.isPlayerOnHisIsland(dead)){
                event.setKeepInventory(true);
                event.setKeepLevel(true);
                event.getDrops().clear();
            }
        }

    }
}
