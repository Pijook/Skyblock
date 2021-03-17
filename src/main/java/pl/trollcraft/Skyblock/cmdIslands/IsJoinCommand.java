package pl.trollcraft.Skyblock.cmdIslands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsJoinCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

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

            String ownerNickname = args[1];

            SkyblockPlayer skyblockMember = skyblockPlayerController.getPlayer(player.getName());

            if(!skyblockMember.hasInvite(ownerNickname)){
                ChatUtils.sendMessage(player, "&cNie posiadasz zaproszenia od tego gracza!");
                return;
            }

            Player owner = Bukkit.getPlayer(ownerNickname);

            if(owner == null || !owner.isOnline()){
                ChatUtils.sendMessage(player, "&cWlasciciel wyspy jest offline! Sprobuj ponownie pozniej...");
                return;
            }


            SkyblockPlayer skyblockOwner = skyblockPlayerController.getPlayer(ownerNickname);

            if(!skyblockOwner.hasIslandOrCoop()){
                ChatUtils.sendMessage(player, "&cTen gracz nie jest wlascicielem wyspy!");
                return;
            }

            if(skyblockMember.hasIslandOrCoop()){
                ChatUtils.sendMessage(player, "&cJuz posiadasz wyspe!");
                return;
            }

            skyblockMember.clearInvites();
            skyblockMember.setIslandID(skyblockOwner.getIslandID());
            skyblockMember.setOnIsland(true);
            islandsController.addMember(ownerNickname, player.getName());

            ChatUtils.sendMessage(player, "&aDolaczono na wyspe gracza " + ownerNickname);
            ChatUtils.sendMessage(owner, "&aGracz " + player.getName() + " dolaczyl do twojej wyspy!");
            return;

            /*
            if (Bukkit.getPlayer(args[1]) != null) { //If argument is player
                if (Bukkit.getPlayer(args[1]).isOnline()) { //If argument is online
                    String owner = Bukkit.getPlayer(args[1]).getName();
                    if (islandsController.isPlayerOwner(owner)) { //If player is owner of island
                        if (skyblockPlayerController.getPlayer(player.getName()).hasIsland()) {  //If player has island/is member of island
                            sender.sendMessage(ChatUtils.fixColor("&cNie mozesz uzyc tej komendy poniewaz posiadasz juz wyspe"));
                        }
                        else {
                            if (skyblockPlayerController.hasInvite(owner, player.getName())) {
                                SkyblockPlayer sbowner = skyblockPlayerController.getPlayer(owner);
                                skyblockPlayerController.clearInvites(player.getName());
                                skyblockPlayerController.getPlayer(player.getName()).setIslandID(sbowner.getIslandOrCoop());
                                skyblockPlayerController.getPlayer(player.getName()).setOnIsland(true);
                                islandsController.addMember(owner, player.getName());

                                sender.sendMessage(ChatUtils.fixColor("&aPomyslnie dolaczono do wyspy " + owner));
                                Bukkit.getPlayer(owner).sendMessage(ChatUtils.fixColor("&aTwoja wyspa zyskala nowego czlonka - " + player.getName()));
                            }
                            else {
                                sender.sendMessage(ChatUtils.fixColor("&aNie posiadasz zaproszenia na wyspe " + owner));
                            }
                        }
                    }
                    else {
                        sender.sendMessage(ChatUtils.fixColor("&cTen grasz nie jest wlascicielem wyspy"));
                    }
                }
                else{
                    sender.sendMessage(ChatUtils.fixColor("&cTen grasz nie jest online"));
                }
            }
            else{
                sender.sendMessage(ChatUtils.fixColor("&cNie znaleziono gracza"));
            }
            */
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
