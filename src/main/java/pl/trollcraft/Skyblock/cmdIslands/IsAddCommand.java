package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsAddCommand extends Command{

    private final IslandsController islandsController = Main.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    public IsAddCommand() {
        super(Collections.singletonList("add"), "Dodaj znajomego do wyspy", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( !(sender instanceof Player) ){
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
        Player player = (Player) sender;

        if( args.length > 1 ) {
            if (islandsController.isPlayerOwner(player.getName())) { //If player is owner of island
                if (Bukkit.getPlayer(args[1]) != null) { //If argument is player
                    if (Bukkit.getPlayer(args[1]).isOnline()) { //If argument is online
                        String member = Bukkit.getPlayer(args[1]).getName();
                        if (skyblockPlayerController.getPlayer(member).hasIsland()) {  //If argument has island/is member of island
                            sender.sendMessage(ChatUtils.fixColor("&cTen gracz posiada juz wyspe"));
                        }
                        else {
                            if( skyblockPlayerController.hasInvite(player.getName(), member) ){
                                sender.sendMessage(ChatUtils.fixColor("&cTen grasz posiada juz zaproszenie na ta wyspe"));
                            }
                            else{
                                skyblockPlayerController.addInvite(player.getName(), member);
                                sender.sendMessage(ChatUtils.fixColor("&aZaproszono gracza " + member));
                            }
                        }
                    }
                    else{
                        sender.sendMessage(ChatUtils.fixColor("&cTen grasz nie jest online"));
                    }
                }
            }
        }

    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {

    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
