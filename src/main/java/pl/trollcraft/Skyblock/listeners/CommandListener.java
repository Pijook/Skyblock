package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class CommandListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();
    private final IslandsController islandsController = Main.getIslandsController();

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();

        if(!skyblockPlayerController.isPlayerLoaded(player.getName())){
            player.sendMessage(ChatUtils.fixColor("&cCos poszlo nie tak! Sprobuj ponownie za pare sekund..."));
//            ChatUtils.sendMessage(player, "&cCos poszlo nie tak! Sprobuj ponownie za pare sekund...");
            event.setCancelled(true);
            return;
        }

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        if(skyblockPlayer.hasIslandOrCoop()){
            if(!islandsController.isIslandLoaded(skyblockPlayer.getIslandOrCoop())){
                player.sendMessage(ChatUtils.fixColor("&cCos poszlo nie tak! Sprobuj ponownie za pare sekund..."));
//                ChatUtils.sendMessage(player, "&cCos poszlo nie tak! Sprobuj ponownie za pare sekund...");
                event.setCancelled(true);
                return;
            }
        }
    }
}
