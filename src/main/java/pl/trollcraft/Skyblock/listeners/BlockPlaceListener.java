package pl.trollcraft.Skyblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class BlockPlaceListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final IslandLimiter islandLimiter = Skyblock.getIslandLimiter();

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        Block block = event.getBlock();

        if(!skyblockPlayer.hasIslandOrCoop()){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }


        if(!islandsController.isLocationOnIsland(block.getLocation(), skyblockPlayer.getIslandOrCoop())) {
            if (!player.hasPermission(PermissionStorage.islandBuild)) {
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;

            }
        }

        /*
        //Uncomment when limits will work
        else{
            if(islandLimiter.isBlockLimited(block.getType())){
                if(islandLimiter.isBlockAboveLimit(block)){
                    event.setCancelled(true);
                }
            }
        }*/


        skyblockPlayer.setPlacedBlocks(skyblockPlayer.getPlacedBlocks() + 1);
    }
}
