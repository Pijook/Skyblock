package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Settings;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.cmdIslands.IsAdminCommand;
import pl.trollcraft.Skyblock.customEvents.PlayerLoadEvent;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerLoadListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    public static ArrayList<String> toGenerate = new ArrayList<>();

    @EventHandler
    public void onPlayerLoad(PlayerLoadEvent event){
        Player player = event.getPlayer();
        Debug.log("[Custom event]&aLoaded player " + player.getName());

        if(toGenerate.contains(player.getName())){
            Debug.log("&aFound player in queue!");
            CreateIsland.createNew(player);
            toGenerate.remove(player.getName());
        }

        ChatUtils.sendMessage(player, "&cLoading islands stats...");

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
        UUID islandID = null;

        if(skyblockPlayer.hasIslandOrCoop()){
            islandID = skyblockPlayer.getIslandOrCoop();
        }

        if(islandID != null){
            /*if(!islandsController.isIslandLoaded(islandID)){
                if(!IsAdminCommand.currentlyUsedIslands.contains(islandID)) {
                    RedisSupport.loadIsland(islandID, player);
                }
            }*/
            RedisSupport.loadIsland(islandID, player);
        }

        ChatUtils.sendSyncMessage(player, "&aLoaded island!");
    }
}
