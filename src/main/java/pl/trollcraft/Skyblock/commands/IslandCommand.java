package pl.trollcraft.Skyblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.generator.DeleteIsland;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.Islands;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;

import java.util.ArrayList;

public class IslandCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Player player = (Player) sender;
        if( args.length == 0 ) {

            sender.sendMessage(ChatUtils.fixColor("&cDostepne komendy:"));
            sender.sendMessage(ChatUtils.fixColor("&a/island create: Stworz wyspe"));
            sender.sendMessage(ChatUtils.fixColor("&a/island delete: Usun wyspe"));
        }
        else{
            if( args[0].equalsIgnoreCase("create")){
                if(Islands.isPlayerOwner(player.getName())){
                    sender.sendMessage(ChatUtils.fixColor("&cPosiadasz juz wyspe!"));
                }
                else {
                    sender.sendMessage(ChatUtils.fixColor("&aTworze wyspe..."));
                    CreateIsland.createNew(player);
                }
            }
            else if( args[0].equalsIgnoreCase("delete")){
                if(Islands.isPlayerOwner(player.getName())) {
                    DeleteIsland.deleteIs(player.getName());
                    sender.sendMessage(ChatUtils.fixColor("&aUsunieto wyspe"));
                }
                else{
                    sender.sendMessage(ChatUtils.fixColor("&cNie posiadasz wyspy"));
                }
            }
            else if( args[0].equalsIgnoreCase("add")){
                if( args.length > 1 ) {
                    if (Islands.isPlayerOwner(player.getName())) { //If player is owner of island
                        if (Bukkit.getPlayer(args[1]) != null) { //If argument is player
                            if (Bukkit.getPlayer(args[1]).isOnline()) { //If argument is online
                                String member = Bukkit.getPlayer(args[1]).getName();
                                if (SkyblockPlayers.getPlayer(member).hasIsland()) {  //If argument has island/is member of island
                                    sender.sendMessage(ChatUtils.fixColor("&cTen gracz posiada juz wyspe"));
                                }
                                else {
                                    if( SkyblockPlayers.hasInvite(player.getName(), member) ){
                                        sender.sendMessage(ChatUtils.fixColor("&cTen grasz posiada juz zaproszenie na ta wyspe"));
                                    }
                                    else{
                                        SkyblockPlayers.addInvite(player.getName(), member);
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
            else if( args[0].equalsIgnoreCase("join")){
                if( args.length > 1 ) {
                    if (Bukkit.getPlayer(args[1]) != null) { //If argument is player
                        if (Bukkit.getPlayer(args[1]).isOnline()) { //If argument is online
                            String owner = Bukkit.getPlayer(args[1]).getName();
                            if (Islands.isPlayerOwner(owner)) { //If player is owner of island
                                if (SkyblockPlayers.getPlayer(player.getName()).hasIsland()) {  //If player has island/is member of island
                                    sender.sendMessage(ChatUtils.fixColor("&Nie mozesz uzyc tej komendy poniewaz posiadasz juz wyspe"));
                                }
                                else {
                                    if( SkyblockPlayers.hasInvite(owner, player.getName()) ){
                                        SkyblockPlayers.clearInvites(player.getName());
                                        SkyblockPlayers.getPlayer(player.getName()).setIslandID(owner);
                                        Islands.addMember(owner, player.getName());

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
            else if( args[0].equalsIgnoreCase("remove")){
                if( args.length > 1 ) {
                    if (Islands.isPlayerOwner(player.getName())) { //If player is owner of island
                        String member = args[1];
                        if (SkyblockPlayers.getPlayer(member).hasIsland()) {  //If argument has island/is member of island
                            if (SkyblockPlayers.getPlayer(member).getIslandID().equalsIgnoreCase(player.getName()) ){
                                SkyblockPlayers.getPlayer(member).setIslandID(null);
                                Islands.remMember(player.getName(), member);
                                sender.sendMessage(ChatUtils.fixColor("&cUsunieto " + member + " z wyspy"));
                            }
                            else{
                                sender.sendMessage(ChatUtils.fixColor("&c" + member + " nie jest czlonkiem Twojej wyspy"));
                            }
                        }
                        else{
                            sender.sendMessage(ChatUtils.fixColor("&c" + member + " nie jest czlonkiem Twojej wyspy. Upewnij sie czy wpisales poprawny nick( Wielkosc liter ma znaczenie )"));
                            ArrayList<String> members = Islands.getIslandById(player.getName()).getMembers();
                            sender.sendMessage(ChatUtils.fixColor("&cCzlonkowie Twojej wyspy: "));
                            for( String mbr : members ){
                                sender.sendMessage(ChatUtils.fixColor("&3" + mbr));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
