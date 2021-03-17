package pl.trollcraft.Skyblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class BlockBreakListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final IslandLimiter islandLimiter = Skyblock.getIslandLimiter();
    private final WorkerController workerController = Skyblock.getWorkerController();

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        /*
        if(!skyblockPlayer.isOnIsland()){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }*/

        if(!skyblockPlayer.hasIslandOrCoop()){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }

        if(!islandsController.isLocationOnIsland(block.getLocation(), skyblockPlayer.getIslandOrCoop())){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }

        /*
        //Uncomment when limits will work
        islandLimiter.removeBlock(block);

        if(workerController.isBlockToMine(block.getType())){
            workerController.getWorkerByName(player.getName()).increaseMinedStone(1);
        }
        if(workerController.isWoodToChop(block.getType())){
            workerController.getWorkerByName(player.getName()).increaseChoppedWood(1);
        }*/


    }
}
