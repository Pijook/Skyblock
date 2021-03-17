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

public class IsAddCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

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
                String memberNickname = args[1];

                Player member = Bukkit.getPlayer(memberNickname);

                if(member == null || !member.isOnline()){
                    ChatUtils.sendMessage(player, "&cAby zaprosic gracza musi byc on na twojej wyspie!");
                    return;
                }

                SkyblockPlayer skyblockMember = skyblockPlayerController.getPlayer(memberNickname);

                if(skyblockMember.hasIslandOrCoop()){
                    ChatUtils.sendMessage(player, "&cTen gracz posiada juz wyspe lub nalezy do innej!");
                    return;
                }

                if(skyblockPlayerController.hasInvite(player.getName(), memberNickname)){
                    ChatUtils.sendMessage(player, "&cTen gracz juz posiada zaproszenie na twoja wyspe");
                    return;
                }

                skyblockMember.addInvite(player.getName());

                ChatUtils.sendMessage(player, "&aWyslano zaproszenie do gracza " + memberNickname);

                ChatUtils.sendMessage(member, "&aOtrzymujesz zaproszenie od " + player.getName());
                ChatUtils.sendMessage(member, "&aAby je przyjac wpisz &7/is join " + player.getName());
                return;
                /*
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
                                ChatUtils.sendMessage(Bukkit.getPlayer(member), "&aOtrzymujesz zaproszenie na wyspe gracza " + player.getName());
                            }
                        }
                    }
                    else{
                        sender.sendMessage(ChatUtils.fixColor("&cTen grasz nie jest online"));
                    }
                }
                else{
                    sender.sendMessage(ChatUtils.fixColor("&cNie znaleziono gracza"));
                }*/
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
