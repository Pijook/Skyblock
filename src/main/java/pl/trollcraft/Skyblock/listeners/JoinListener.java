package pl.trollcraft.Skyblock.listeners;

import com.sk89q.worldedit.function.mask.MaskIntersection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class JoinListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                RedisSupport.loadPlayer(player);
            }
        }, 1L);

        //Skyblock.getWorkerController().loadPlayer(player);
        /*if(Settings.spawnOnJoin){
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
                        Main.getIslandLimiter().loadIsland(islandID);
                    }
                }

                ChatUtils.sendSyncMessage(player, "&aLoaded island!");
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 60L);*/


    }
}
