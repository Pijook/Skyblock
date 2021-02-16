package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.cmdIslands.IsAdminCommand;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class QuitListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();
    private final IslandsController islandsController = Main.getIslandsController();

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());


        if(skyblockPlayer.hasIslandOrCoop()){
            String islandID = skyblockPlayer.getIslandOrCoop();
            if(!islandsController.hasIslandOnlineMembers(islandID)){
                if(!IsAdminCommand.currentlyUsedIslands.contains(islandID)){
                    RedisSupport.saveIsland(islandID);
                }
            }
        }

        RedisSupport.savePlayer(player);
    }
}
