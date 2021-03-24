package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class InteractListener implements Listener {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    @EventHandler
    public void onInteract(PlayerInteractEvent event){

        Player player = event.getPlayer();

        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){

            if(player.getInventory().getItemInMainHand().getType().equals(Material.BUCKET)){

                if(!event.getClickedBlock().getType().equals(Material.OBSIDIAN)){
                    return;
                }

                if(!islandsController.isPlayerOnHisIsland(player)){
                    return;
                }

                player.getInventory().getItemInMainHand().setType(Material.LAVA_BUCKET);
                event.getClickedBlock().setType(Material.AIR);
                ChatUtils.sendMessage(player, "&aWhooosh!");
                return;

            }
        }

    }
}
