package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsHomeCommand extends Command {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsHomeCommand() {
        super(Collections.singletonList("home"), "Teleportuj sie na swoja wyspe", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
            if(skyblockPlayerController.getPlayer(player.getName()).hasIslandOrCoop()){
                Island island = islandsController.getIslandById(skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop());
                Location loc = island.getHome();
                player.teleport(loc.add(0, 2, 0));
            }
            else{
                ChatUtils.sendMessage(player, "&cNie posiadasz wyspy");
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }

    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        ChatUtils.sendMessage(sender, "&cNadal pracujemy nad ta komenda");
        /*
        if( islandsController.getIslandByOwnerOrMember(player[0].getName()) != null ) {
            Location islandHome = islandsController.getIslandByOwnerOrMember(player[0].getName()).getHome();
            player[0].teleport(islandHome);

            ChatUtils.sendMessage(player[0], "&aTeleportowano na wyspe");
            ChatUtils.sendMessage((Player) sender, "&aTeleportowano " + player[0].getName() + " na jego wyspe");
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c" + player[0].getName() + " nie posiada wyspy"));
        }

         */
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
