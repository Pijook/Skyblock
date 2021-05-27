package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class BlockPlaceListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final LimitController limitController = Skyblock.getLimitController();

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        Block block = event.getBlock();

//        Debug.log("Placed block: " + block.getType().name());

        boolean hasCoop = skyblockPlayer.hasIslandOrCoop();
        boolean isOnHisIsland = islandsController.isPlayerOnHisIsland(player);

        if( player.hasPermission(PermissionStorage.thisIsSpawn)){
            return;
        }

        if(!hasCoop){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }

        if(!isOnHisIsland){
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }

        if(!islandsController.isLocationOnIsland(event.getBlockPlaced().getLocation(), skyblockPlayer.getIslandOrCoop())){
//            Debug.log("&c[BlockPlace] Location is not on island!");
            if(!player.hasPermission(PermissionStorage.islandBuild)){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                return;
            }
        }
//        else{
//            Location location = event.getBlockPlaced().getLocation();
//            Debug.log("&a[BlockPlace] Location is on island!");
//            Debug.log("&aLocation: " + location.getX() + " " + location.getY() + " " + location.getZ());
//            Debug.log("&aIsland owner: " + islandsController.getIslandByLocation(location).getOwner());
//        }

        Material material = block.getType();
        if(material.equals(Material.STICKY_PISTON)){
            material = Material.PISTON;
        }
        String type = material.name();
        if(isOnHisIsland){
            if(limitController.isTypeLimited(type)){

                if(limitController.isAboveLimit(type, skyblockPlayer.getIslandOrCoop())){
                    ChatUtils.sendMessage(player, "&cOsiagnales limit blokow tego typu!");
                    event.setCancelled(true);
                    return;
                }
                else{
                    limitController.increaseType(type, skyblockPlayer.getIslandOrCoop());
                }

            }
            Skyblock.getPointsController().addPoints(block.getType().name(), skyblockPlayer.getIslandOrCoop());
        }

        skyblockPlayer.setPlacedBlocks(skyblockPlayer.getPlacedBlocks() + 1);
    }
}
