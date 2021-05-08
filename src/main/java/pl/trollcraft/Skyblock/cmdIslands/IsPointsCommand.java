package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IsPointsCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsPointsCommand() {
        super(Collections.singletonList("points"), "Sprawdz punkty swojej wyspy", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( args.length == 1 ){
            Player player = (Player) sender;
            if(skyblockPlayerController.getPlayer(player.getName()).hasIslandOrCoop()){
                Island island = islandsController.getIslandById(skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop());
                ChatUtils.sendMessage(player, "&aPunkty wyspy: " + island.getPoints());
            }
            else{
                ChatUtils.sendMessage(player, "&cNie posiadasz wyspy");
            }
            return;
        }
        sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) ));
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        ChatUtils.sendMessage(sender, "&cNadal pracujemy nad ta komenda");
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
