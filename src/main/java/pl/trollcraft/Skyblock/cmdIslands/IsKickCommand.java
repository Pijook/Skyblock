package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsKickCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsKickCommand() {
        super(Collections.singletonList("kick"), "Wyrzuc gracza ze swojej wyspy", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 2){
            Player player = (Player) sender;

            Player target = Bukkit.getPlayer(args[1]);
            if(target == null || !target.isOnline()){
                ChatUtils.sendMessage(sender, "&cTen gracz jest offline!");
                return;
            }

            if(!islandsController.isPlayerOnHisIsland(player)){
                ChatUtils.sendMessage(player, "&cMusisz byc na wyspie aby to zrobic!");
                return;
            }

            Island island = islandsController.getIslandByLocation(player.getLocation());

            if(island.getOwner().equalsIgnoreCase(player.getName())){
                ChatUtils.sendMessage(target, "&cZostales wyrzucony z wyspy!");
                ChatUtils.sendMessage(player, "&aGracz &e" + target.getName() + " &azostal wyrzucony z wyspy");
                target.teleport(Storage.spawn);
                return;
            }
            else{
                ChatUtils.sendMessage(player, "&cTylko wlasciciel wyspy moze to zrobic!");
                return;
            }
        }
        else{
            ChatUtils.sendMessage(sender, "&7/is" + aliases.get(0) + " <nickname>");
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
