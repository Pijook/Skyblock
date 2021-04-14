package pl.trollcraft.Skyblock.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.islandUpgrades.IslandSizeUpgrade;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;

public class TestIslandCommand implements CommandExecutor {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("&cKomenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 1){

            if(args[0].equalsIgnoreCase("upgrade")){

                SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
                Worker worker = Skyblock.getWorkerController().getWorkerByName(player.getName());

                if(!skyblockPlayer.hasIsland()){
                    ChatUtils.sendMessage(player, "&cMusisz miec wyspe aby to zrobic!");
                    return true;
                }

                Island island = islandsController.getIslandById(skyblockPlayer.getIslandOrCoop());

                if(!island.getOwner().equalsIgnoreCase(player.getName())){
                    ChatUtils.sendMessage(player, "&cMusisz byc wlascicielem wyspy aby to zrobic!");
                    return true;
                }

                if(!islandsController.isPlayerOnHisIsland(player)){
                    ChatUtils.sendMessage(player, "&cMusisz byc na swojej wyspie aby to zrobic!");
                    return true;
                }

                if(!islandsController.canUpgrade(island, player)){
                    ChatUtils.sendMessage(player, "&cMasz za maly poziom aby ulepszyc wyspe!");
                    return true;
                }

                island.upgradeIsland();
                if(IslandSizeUpgrade.upgradeSize(skyblockPlayer.getIslandID())){
                    ChatUtils.sendMessage(player, "&aUlepszono wyspe!");
                }
                else{
                    ChatUtils.sendMessage(player, "&cCos poszlo nie tak...");
                }
                return true;

            }



        }

        if(args.length == 2){
            if(args[0].equalsIgnoreCase("borders")){

                SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());

                if(!skyblockPlayer.hasIslandOrCoop()){
                    ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                    return true;
                }

                if(!islandsController.isPlayerOnHisIsland(player)){
                    ChatUtils.sendMessage(player, "&cMusisz byc na swojej wyspie aby to zrobic!");
                    return true;
                }

                Island island = islandsController.getIslandById(skyblockPlayer.getIslandOrCoop());

                if(args[1].equalsIgnoreCase("show")){
                    island.getPoint1().getBlock().setType(Material.RED_WOOL);
                    island.getPoint2().getBlock().setType(Material.RED_WOOL);
                    ChatUtils.sendMessage(player, "&aUstawiono bloki!");
                }
                else if(args[1].equalsIgnoreCase("hide")){
                    island.getPoint1().getBlock().setType(Material.AIR);
                    island.getPoint2().getBlock().setType(Material.AIR);
                    ChatUtils.sendMessage(player, "&cUsunieto bloki!");
                }

                return true;
            }
        }

        ChatUtils.sendMessage(player, "&7/" + label + " upgrade");
        ChatUtils.sendMessage(player, "&7/" + label + " borders hide/show");
        return true;
    }
}
