package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.cmdIslands.IsAdminCommand;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Islands;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        RedisSupport.loadPlayer(player);

        /*ChatUtils.sendMessage(player, "&cLoading islands stats...");
        new BukkitRunnable(){

            @Override
            public void run() {
                SkyblockPlayer skyblockPlayer = SkyblockPlayers.getPlayer(player.getName());
                String islandID = null;


                if(skyblockPlayer.hasIslandOrCoop()){
                    islandID = skyblockPlayer.getIslandOrCoop();
                }

                if(islandID != null){
                    if(!Islands.isIslandLoaded(islandID)){
                        if(!IsAdminCommand.currentlyUsedIslands.contains(islandID)) {
                            RedisSupport.loadIsland(islandID);
                        }
                    }
                }

                ChatUtils.sendSyncMessage(player, "&aLoaded island!");
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 40L);*/


    }
}
