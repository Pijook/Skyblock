package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class BlockBreakListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();
    private final IslandsController islandsController = Main.getIslandsController();
    private final IslandLimiter islandLimiter = Main.getIslandLimiter();

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if(!islandsController.isPlayerOnHisIsland(player)){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }
        else{
            islandLimiter.removeBlock(event.getBlock());
        }
    }
}
