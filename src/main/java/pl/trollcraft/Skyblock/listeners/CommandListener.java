package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.ArrayList;

public class CommandListener implements Listener {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();


    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();

        String message = event.getMessage();

        ArrayList<String> blocked = new ArrayList<>();
        blocked.add("is");
        blocked.add("island");
        blocked.add("tcisland");

        int end = event.getMessage().length();
        if( event.getMessage().contains(" ") ){
            end = event.getMessage().indexOf(" ");
        }
        String label = event.getMessage().substring(1, end);

        if( label.equalsIgnoreCase("bug") ){
            return;
        }

        if(!skyblockPlayerController.isPlayerLoaded(player.getName())){
            player.sendMessage(ChatUtils.fixColor("&cCos poszlo nie tak! Sprobuj ponownie za pare sekund..."));
            Debug.log("&cGracz " + player.getName() + " sie zepsul - gracz nie zostal zaladowany");
//            ChatUtils.sendMessage(player, "&cCos poszlo nie tak! Sprobuj ponownie za pare sekund...");
            event.setCancelled(true);
            BungeeSupport.sendReloadPlayerCommand(player);
            return;
        }

        SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

        if(skyblockPlayer.hasIslandOrCoop()){
            if(!islandsController.isIslandLoaded(skyblockPlayer.getIslandOrCoop())){
                if( player.hasPermission("" + PermissionStorage.disabledCommandBypassPermission) ){
                    if( !(blocked.contains("" + label)) ){
                        Debug.log("&cGracz " + player.getName() + " sie zepsul - wyspa nie zostala zaladowana");
                        Debug.log("&cOmijam blokade poniewaz uzyta komenda to: /" + label);
                        BungeeSupport.sendReloadIslandCommand(skyblockPlayer.getIslandOrCoop().toString(), player);
                        return;
                    }
                }
                player.sendMessage(ChatUtils.fixColor("&cCos poszlo nie tak! Sprobuj ponownie za pare sekund..."));
                Debug.log("&cGracz " + player.getName() + " sie zepsul - wyspa nie zostala zaladowana");
//                ChatUtils.sendMessage(player, "&cCos poszlo nie tak! Sprobuj ponownie za pare sekund...");
                event.setCancelled(true);
                BungeeSupport.sendReloadIslandCommand(skyblockPlayer.getIslandOrCoop().toString(), player);
                return;
            }
        }
    }
}
