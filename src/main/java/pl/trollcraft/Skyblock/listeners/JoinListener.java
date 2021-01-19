package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        SkyblockPlayers.loadPlayer(player.getName());
    }
}
