package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class PlayerKillPlayerListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    @EventHandler
    public void onKill(PlayerDeathEvent event){

        Player dead = event.getEntity();
        if(event.getEntity().getKiller() != null){
            Player killer = event.getEntity().getKiller();

            skyblockPlayerController.getPlayer(dead.getName()).increaseDeaths(1);
            skyblockPlayerController.getPlayer(killer.getName()).increaseKills(1);
        }

    }
}
