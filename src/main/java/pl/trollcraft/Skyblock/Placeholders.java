package pl.trollcraft.Skyblock;

import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class Placeholders extends PlaceholderHook {

    SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    IslandsController islandsController = Skyblock.getIslandsController();

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if(params.equalsIgnoreCase("island_level")){
            if(!skyblockPlayerController.isPlayerLoaded(player.getName())){
                return "0";
            }

            SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

            if(!skyblockPlayer.hasIslandOrCoop()){
                return "0";
            }
            else{
                return String.valueOf(islandsController.getIslandById(skyblockPlayer.getIslandOrCoop()).getIslandLevel());
            }

        }

        return super.onPlaceholderRequest(player, params);
    }
}
