package pl.trollcraft.Skyblock.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.dropManager.DropManager;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class BlockBreakListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final LimitController limitController = Skyblock.getLimitController();
    private final WorkerController workerController = Skyblock.getWorkerController();
    private final DropManager dropManager = Skyblock.getDropManager();

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        //Debug.log("=========BlockBreakEvent=============");
        Block block = event.getBlock();

//        Debug.log(block.getType().name());

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        if(!skyblockPlayer.hasIslandOrCoop()){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }

        if(!islandsController.isPlayerOnHisIsland(player)){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }

        /*
        if(!skyblockPlayer.isOnIsland()){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }*/

        /*if(!skyblockPlayer.hasIslandOrCoop()){
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
        }*/

        Worker worker = workerController.getWorkerByName(player.getName());

        if(Storage.dropEnable) {
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                if( !(player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH))){
                    if (dropManager.countsAsDrop(block.getType())) {
                        //ItemStack dropItem = new ItemStack(dropManager.generateMaterial(skyblockPlayer.getDropLevel()));
                        ItemStack dropItem = new ItemStack(dropManager.generateMaterial((int) worker.getAverageLevel()));
                        if( !(dropItem.getType().equals(Material.COBBLESTONE))){
                            if( player.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) ) {
                                int fortuneLevel = player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                                int amountOfItems = dropManager.amountByFortune(fortuneLevel);
                                dropItem.setAmount(amountOfItems);
                            }
                        }
                        event.setDropItems(false);
                        block.getWorld().dropItemNaturally(block.getLocation(), dropItem);
                    }
                }
            }
        }

        //Uncomment when limits will work
        //islandLimiter.removeBlock(block);

        /*if(islandLimiter.isBlockLimited(block.getType())){
            islandLimiter.removeBlock(skyblockPlayer.getIslandOrCoop(), block.getType());
        }*/

        Material material = block.getType();
        if(material.equals(Material.STICKY_PISTON)){
            material = Material.PISTON;
        }
        if(limitController.isTypeLimited(material.name())){
            limitController.decreaseType(material.name(), skyblockPlayer.getIslandOrCoop());
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
        if(workerController.isCropsToHarvest(block.getType())){
            if(block.getData() == 0x7){
                worker.increaseHarvestedCrops(1);
                if(workerController.canLevelUp(worker, "farmer")){
                    workerController.levelUpJob(worker, "farmer");
                    ChatUtils.sendMessage(player, "&aOsiagnales nowy lvl pracy!");
                }
            }
        }

        Skyblock.getPointsController().removePoints(block.getType().name(), skyblockPlayer.getIslandOrCoop());

    }
}