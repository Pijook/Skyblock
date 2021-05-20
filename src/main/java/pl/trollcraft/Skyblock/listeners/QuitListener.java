package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.cmdIslands.IsAdminCommand;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.WorkerController;

import java.util.UUID;

public class QuitListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final WorkerController workerController = Skyblock.getWorkerController();

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

        Player player = event.getPlayer();

        event.setQuitMessage("");

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        if(skyblockPlayerController.isCurrentlyChecked(player)){
            skyblockPlayerController.removeUncheckedPlayer(player);
        }

        if(skyblockPlayerController.isPlayerLoaded(player.getName())){
            if(skyblockPlayer.hasIslandOrCoop()){
                UUID islandID = skyblockPlayer.getIslandOrCoop();
                Debug.log("Player has island " + islandID);
                try{
                    if(!islandsController.hasIslandOnlineMembers(islandID, player.getName())){
                        if(!IsAdminCommand.currentlyUsedIslands.contains(islandID)){
                            if(islandsController.isIslandLoaded(islandID)){
                                if(islandsController.getIslandById(islandID).getServer().equalsIgnoreCase(Storage.serverName)){
                                    RedisSupport.saveIsland(player, islandID);
                                }
                            }
                        }
                    }
                }
                catch (NullPointerException e){
                    return;
                }
            }
            else{
                Debug.log("&cPlayer dont have island!");
            }
        }


        RedisSupport.savePlayer(player);
        workerController.savePlayer(player);
        if(Storage.kitsEnabled){
            Skyblock.getKitManager().savePlayer(player);
        }
    }
}
