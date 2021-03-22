package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsVisitCommand extends Command {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsVisitCommand() {
        super(Collections.singletonList("visit"), "Teleportuj sie na wyspe wybranego gracza", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( sender instanceof Player) {
            String ownerToFind = args[1];
            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(ownerToFind);

            //If Player is on same sector
            if(target != null && target.isOnline()){
                SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(ownerToFind);

                if(skyblockPlayer.hasIslandOrCoop()){
                    player.teleport(islandsController.getIslandById(skyblockPlayer.getIslandOrCoop()).getHome());
                    ChatUtils.sendMessage(player, "&aTeleportowano na wyspe!");
                    return;
                }
                else{
                    ChatUtils.sendMessage(player, "&cTen gracz nie ma wyspy!");
                    return;
                }
            }
            //If player is on different sector
            else{
                SkyblockPlayer skyblockPlayer = RedisSupport.getSkyblockPlayer(ownerToFind);

                if(skyblockPlayer == null){
                    ChatUtils.sendMessage(player, "&cTen gracz jest offline!");
                    return;
                }

                if(!skyblockPlayer.hasIslandOrCoop()){
                    ChatUtils.sendMessage(player, "&cTen gracz nie ma wyspy!");
                    return;
                }

                Island island = RedisSupport.getIsland(skyblockPlayer.getIslandOrCoop());

                if(island == null){
                    ChatUtils.sendMessage(player, "&cWyspa tego gracza nie istnieje!");
                    return;
                }
                else{
                    skyblockPlayerController.getPlayer(player.getName()).setOnIsland(false);
                    player.teleport(island.getHome());
                    ChatUtils.sendMessage(player, "&aTeleportowano na wyspe!");
                    return;
                }

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
