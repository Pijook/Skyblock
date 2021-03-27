package pl.trollcraft.Skyblock.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.dropManager.DropManager;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class BlockBreakListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final IslandLimiter islandLimiter = Skyblock.getIslandLimiter();
    private final WorkerController workerController = Skyblock.getWorkerController();
    private final DropManager dropManager = Skyblock.getDropManager();

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

        Worker worker = workerController.getWorkerByName(player.getName());

        if(player.getGameMode().equals(GameMode.SURVIVAL)){
            if(dropManager.countsAsDrop(block.getType())){
                event.setDropItems(false);
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(dropManager.generateMaterial((int) worker.getAverageLevel())));
            }
        }

        //Uncomment when limits will work
        //islandLimiter.removeBlock(block);

        if(islandLimiter.isBlockLimited(block.getType())){
            islandLimiter.removeBlock(skyblockPlayer.getIslandOrCoop(), block.getType());
        }

        if(workerController.isBlockToMine(block.getType())){
            worker.increaseMinedStone(1);
            if(workerController.canLevelUp(worker,  "miner")){
                workerController.levelUpJob(worker, "miner");
                ChatUtils.sendMessage(player, "&aOsiagnales nowy lvl pracy!");
            }
        }
        if(workerController.isWoodToChop(block.getType())){
            worker.increaseChoppedWood(1);
            if(workerController.canLevelUp(worker,  "lumberjack")){
                workerController.levelUpJob(worker, "lumberjack");
                ChatUtils.sendMessage(player, "&aOsiagnales nowy lvl pracy!");
            }
        }


    }
}
