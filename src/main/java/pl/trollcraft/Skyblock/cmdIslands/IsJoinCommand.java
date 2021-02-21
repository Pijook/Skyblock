package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsJoinCommand extends Command{

    private final IslandsController islandsController = Main.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    public IsJoinCommand() {
        super(Collections.singletonList("join"), "Dolacz do wyspy znajomego", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( !(sender instanceof Player) ){
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
        Player player = (Player) sender;


        if( args.length > 1 ) {
            if (Bukkit.getPlayer(args[1]) != null) { //If argument is player
                if (Bukkit.getPlayer(args[1]).isOnline()) { //If argument is online
                    String owner = Bukkit.getPlayer(args[1]).getName();
                    if (islandsController.isPlayerOwner(owner)) { //If player is owner of island
                        if (skyblockPlayerController.getPlayer(player.getName()).hasIsland()) {  //If player has island/is member of island
                            sender.sendMessage(ChatUtils.fixColor("&Nie mozesz uzyc tej komendy poniewaz posiadasz juz wyspe"));
                        }
                        else {
                            if( skyblockPlayerController.hasInvite(owner, player.getName()) ){
                                SkyblockPlayer sbowner = skyblockPlayerController.getPlayer(owner);
                                skyblockPlayerController.clearInvites(player.getName());
                                skyblockPlayerController.getPlayer(player.getName()).setIslandID(sbowner.getIslandOrCoop());
                                islandsController.addMember(owner, player.getName());

                                sender.sendMessage(ChatUtils.fixColor("&aPomyslnie dolaczono do wyspy " + owner ));
                                Bukkit.getPlayer(owner).sendMessage(ChatUtils.fixColor("&aTwoja wyspa zyskala nowego czlonka - " + player.getName()));
                            }
                            else{
                                sender.sendMessage(ChatUtils.fixColor("&aNie posiadasz zaproszenia na wyspe " + owner));
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
