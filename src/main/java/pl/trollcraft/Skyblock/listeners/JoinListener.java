package pl.trollcraft.Skyblock.listeners;

import com.sk89q.worldedit.function.mask.MaskIntersection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private final WorkerController workerController = Skyblock.getWorkerController();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        
        if(!player.getWorld().getName().equalsIgnoreCase("Islands")){
            player.teleport(new Location(Bukkit.getWorld("Islands"), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        }

        Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                RedisSupport.loadPlayer(player);
                workerController.loadPlayer(player);
            }
        }, 1L);



    }
}
