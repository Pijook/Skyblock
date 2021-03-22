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

public class IsCancelInviteCommand extends Command {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsCancelInviteCommand() {
        super(Collections.singletonList("cancelinvite"), "Anuluj zaproszenie", "TcSb.basic", true);
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
                        String inv = Bukkit.getPlayer(args[1]).getName();
                        if( skyblockPlayerController.hasInvite(player.getName(), inv) ){
                            skyblockPlayerController.remInvite(player.getName(), inv);
                            sender.sendMessage(ChatUtils.fixColor("&aZaproszenie dla graca %inv% zostalo anulowane").replaceAll("%inv%", "" + inv));
                        }
                        else{
                            sender.sendMessage(ChatUtils.fixColor("&a%inv% nie posiada zaproszenia na Twoja wyspe").replaceAll("%inv%", "" + inv));
                        }

                    }
                    else{
                        sender.sendMessage(ChatUtils.fixColor("&cTen grasz nie jest online"));
                    }
                }
                else{
                    sender.sendMessage(ChatUtils.fixColor("&cNie znaleziono gracza"));
                }
            }
            else{
                sender.sendMessage(ChatUtils.fixColor("&cNie jestes wlascicielem wyspy"));
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) + " " + "<nick>" ));
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
