package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class BlockPlaceListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();
    private final IslandsController islandsController = Main.getIslandsController();

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        if(!islandsController.isPlayerOnHisIsland(player)){
            event.setCancelled(true);
            ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
            return;
        }

        skyblockPlayer.setPlacedBlocks(skyblockPlayer.getPlacedBlocks() + 1);
    }
}
