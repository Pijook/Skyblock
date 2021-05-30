package pl.trollcraft.Skyblock.listeners;

import com.sk89q.worldedit.function.mask.MaskIntersection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;
import pl.trollcraft.Skyblock.Settings;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.WorkerController;
import pl.trollcraft.ists.model.event.ISRProcessedEvent;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JoinListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final WorkerController workerController = Skyblock.getWorkerController();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().addPotionEffect(  PotionEffectType.ABSORPTION.createEffect(3, 50) );
        event.setJoinMessage("");
    }

    @EventHandler
    public void onAppear(ISRProcessedEvent event){
        Player player = event.getPlayer();


        if(!event.wasISRFound()){
            if(Settings.spawnOnJoin){
                Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.teleport(Storage.spawn);
                    }
                });
            }
        }

        if(!player.getWorld().getName().equalsIgnoreCase("Islands")){
            player.teleport(new Location(Bukkit.getWorld("Islands"), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        }

        Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                try{
                    RedisSupport.loadPlayer(player);
                    workerController.loadPlayer(player);
                }
                catch (JedisConnectionException e){
                    Skyblock.initJedis();
                    return;
                }

                if(Storage.kitsEnabled){
                    Skyblock.getKitManager().loadPlayer(player);
                }
            }
        }, 1L);
    }

    /*@EventHandler
    public void onAppear(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!event.wasISRFound()){
            if(Settings.spawnOnJoin){
                Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        player.teleport(Storage.spawn);
                    }
                });
            }
        }

        if(Settings.spawnOnJoin){
            Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    player.teleport(Storage.spawn);
                }
            });
        }

        if(!player.getWorld().getName().equalsIgnoreCase("Islands")){
            player.teleport(new Location(Bukkit.getWorld("Islands"), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        }

        Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                try{
                    RedisSupport.loadPlayer(player);
                    workerController.loadPlayer(player);
                }
                catch (JedisConnectionException e){
                    Skyblock.setJedis(new Jedis());
                    return;
                }

                if(Storage.kitsEnabled){
                    Skyblock.getKitManager().loadPlayer(player);
                }
            }
        }, 1L);
    }*/
}
