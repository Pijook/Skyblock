package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trollcraft.Skyblock.cmdIslands.IsAdminCommand;
import pl.trollcraft.Skyblock.island.Islands;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        RedisSupport.loadPlayer(player);

        SkyblockPlayer skyblockPlayer = SkyblockPlayers.getPlayer(player.getName());

        String islandID = skyblockPlayer.getIslandOrCoop();

        if(islandID != null){
            if(!Islands.isIslandLoaded(islandID)){
                if(!IsAdminCommand.currentlyUsedIslands.contains(islandID)) {
                    RedisSupport.loadIsland(islandID);
                }
            }
        }
    }
}
