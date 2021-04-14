package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.islandUpgrades.IslandSizeUpgrade;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;

import java.util.Collections;
import java.util.List;

public class IsUpgradeCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsUpgradeCommand() {
        super(Collections.singletonList("upgrade"), "Ulepsz swoja wyspe", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("&cKomenda tylko dla graczy!");
            return;
        }

        Player player = (Player) sender;

        if(args.length == 1){
            SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
            Worker worker = Skyblock.getWorkerController().getWorkerByName(player.getName());

            if(!skyblockPlayer.hasIsland()){
                ChatUtils.sendMessage(player, "&cMusisz miec wyspe aby to zrobic!");
                return;
            }

            Island island = islandsController.getIslandById(skyblockPlayer.getIslandOrCoop());

            if(!island.getOwner().equalsIgnoreCase(player.getName())){
                ChatUtils.sendMessage(player, "&cMusisz byc wlascicielem wyspy aby to zrobic!");
                return;
            }

            if(!islandsController.isPlayerOnHisIsland(player)){
                ChatUtils.sendMessage(player, "&cMusisz byc na swojej wyspie aby to zrobic!");
                return;
            }

            if(!islandsController.canUpgrade(island, player)){
                ChatUtils.sendMessage(player, "&cMasz za maly poziom aby ulepszyc wyspe!");
                return;
            }

            island.upgradeIsland();

            if(IslandSizeUpgrade.upgradeSize(skyblockPlayer.getIslandID())){
                ChatUtils.sendMessage(player, "&aUlepszono wyspe!");
            }
            else{
                ChatUtils.sendMessage(player, "&cCos poszlo nie tak...");
            }

            Skyblock.getEconomy().withdrawPlayer(player, islandsController.getIslandUpgradeCost(island.getIslandLevel()).getMoney());
            return;

        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) ));
        }
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
