package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class RespawnListener implements Listener {

    private final IslandsController islandsController = Main.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if(!islandsController.isPlayerOnHisIsland(player)){
            skyblockPlayerController.getPlayer(player.getName()).setOnIsland(false);
        }
        else{
            skyblockPlayerController.getPlayer(player.getName()).setOnIsland(true);
        }
    }
}
