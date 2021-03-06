package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Settings;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.cmdIslands.IsAdminCommand;
import pl.trollcraft.Skyblock.customEvents.PlayerLoadEvent;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.UUID;

public class PlayerLoadListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();
    private final IslandsController islandsController = Main.getIslandsController();

    @EventHandler
    public void onPlayerLoad(PlayerLoadEvent event){
        Player player = event.getPlayer();
        Debug.log("[Custom event]&aLoaded player " + player.getName());

        if(Settings.spawnOnJoin){
            Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.teleport(Storage.spawn);
                }
            });
        }

        ChatUtils.sendMessage(player, "&cLoading islands stats...");
        new BukkitRunnable(){

            @Override
            public void run() {
                SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
                UUID islandID = null;


                if(skyblockPlayer.hasIslandOrCoop()){
                    islandID = skyblockPlayer.getIslandOrCoop();
                }

                if(islandID != null){
                    if(!islandsController.isIslandLoaded(islandID)){
                        if(!IsAdminCommand.currentlyUsedIslands.contains(islandID)) {
                            RedisSupport.loadIsland(islandID);
                        }
                    }
                }

                ChatUtils.sendSyncMessage(player, "&aLoaded island!");
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 10L);
    }
}
