package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.island.bungeeIsland.BungeeIsland;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class DebugCommand implements CommandExecutor {

    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if( !sender.hasPermission(PermissionStorage.admCommands)){
            ChatUtils.sendMessage(sender, "&cNie masz dostepu do tej komendy!");
            return true;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("stats")){
                ChatUtils.sendMessage(player, "&f&lPlaced blocks:" + skyblockPlayerController.getPlayer(player.getName()).getPlacedBlocks());
                ChatUtils.sendSyncMessage(player, "&f&lLoaded islands: " + islandsController.getIslands().size());
                return true;
            }

            if(args[0].equalsIgnoreCase("island")){
                Island island = islandsController.getIslandByLocation(player.getLocation());

                if(island == null){
                    ChatUtils.sendMessage(player, "&cNie znajdujesz sie na wyspie!");
                    return true;
                }

                ChatUtils.sendMessage(player, "&f&lOwner: " + island.getOwner());
                ChatUtils.sendMessage(player, "&f&lMembers: " + island.getMembers().toString());
                ChatUtils.sendMessage(player, "&f&lIsland level: " + island.getIslandLevel());
                ChatUtils.sendMessage(player, "&f&lPoints: " + island.getPoints());
                ChatUtils.sendMessage(player, "&f&lSpawn");
                ChatUtils.sendMessage(player, "X:" + island.getHome().getX());
                ChatUtils.sendMessage(player, "Y:" + island.getHome().getY());
                ChatUtils.sendMessage(player, "Z:" + island.getHome().getZ());
                ChatUtils.sendMessage(player, "&f&lPoint 1: " + Utils.locationToString(island.getPoint1()));
                ChatUtils.sendMessage(player, "&f&lPoint 2: " + Utils.locationToString(island.getPoint2()));
                return true;
            }
            if(args[0].equalsIgnoreCase("islandjson")){
                Island island = islandsController.getIslandByLocation(player.getLocation());

                if(island == null){
                    ChatUtils.sendMessage(player, "&cNie znajdujesz sie na wyspie!");
                    return true;
                }

                BungeeIsland bungeeIsland = islandsController.convertIslandToBungeeIsland(island);
                String islandString = RedisSupport.bungeeIslandToString(bungeeIsland);
                ChatUtils.sendMessage(player, islandString);
                return true;
            }
            if(args[0].equalsIgnoreCase("sync")){
                islandsController.syncIslands();
                return true;
            }
            if(args[0].equalsIgnoreCase("syncTop")){
                Debug.log("&aChecking top...");
                islandsController.checkTop();
                Skyblock.getTopController().sync();
                return true;
            }
        }


        ChatUtils.sendMessage(player, "&7/" + label + " stats");
        ChatUtils.sendMessage(player, "&7/" + label + " island");
        ChatUtils.sendMessage(player, "&7/" + label + " islandjson");
        ChatUtils.sendMessage(player, "&7/" + label + " sync");
        ChatUtils.sendMessage(player, "&7/" + label + " costs");
        ChatUtils.sendMessage(player, "&7/" + label + " syncTop");
        return true;
    }
}
