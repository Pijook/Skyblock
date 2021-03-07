package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class IsVisitCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsVisitCommand() {
        super(Collections.singletonList("visit"), "Teleportuj sie na wyspe wybranego gracza", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
            UUID islandID = islandsController.getIslandIdByOwnerOrMember(args[1]);
            if( islandID != null ){
                Location islandHome = islandsController.getIslandById(islandID).getHome();
                player.teleport(islandHome);
                player.sendMessage(ChatUtils.fixColor( "&aTeleportowano na wyspe gracza " + islandsController.getIslandById(islandID).getOwner() ));
            }
            else{
                player.sendMessage(ChatUtils.fixColor("&c" + args[1] + " nie posiada wyspy"));
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        sender.sendMessage(ChatUtils.fixColor("&cKomenda chwilowo niedostepna."));
//        if( islandsController.getIslandByOwnerOrMember(args[] != null ) {
//            Location islandHome = islandsController.getIslandByOwnerOrMember(player[0].getName()).getHome();
//            player[0].teleport(islandHome);
//            player[0].sendMessage(ChatUtils.fixColor("&aTeleportowano na wyspe"));
//            sender.sendMessage(ChatUtils.fixColor("&aTeleportowano " + player[0].getName() + " na jego wyspe"));
//        }
//        else{
//            sender.sendMessage(ChatUtils.fixColor("&c" + player[0].getName() + " nie posiada wyspy"));
//        }
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
