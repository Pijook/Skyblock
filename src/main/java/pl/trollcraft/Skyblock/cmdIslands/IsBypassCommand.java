package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;

import java.util.Collections;
import java.util.List;

public class IsBypassCommand extends Command{

    public IsBypassCommand() {
        super(Collections.singletonList("bypass"), "Omin zabezpieczenia", "" + PermissionStorage.otherCommandPermission + ".bypass", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( args.length == 1) {
            if( !(sender instanceof Player)){
                ChatUtils.sendMessage(sender, "&cMusisz byc graczem");
                return;
            }
            Player player = (Player) sender;
            if (Storage.bypassList.contains(player.getName())) {
                Storage.bypassList.remove(player.getName());
                ChatUtils.sendMessage(player, "&aNie omijasz juz dluzej zabezpieczen na sektorze " + Storage.serverName);
            } else {
                Storage.bypassList.add(player.getName());
                ChatUtils.sendMessage(player, "&aOd teraz omijasz zabezpieczenia na sektorze " + Storage.serverName);
            }
            return;
        }
        if( args.length == 2 ){
            String other = args[1];
            Player otherPlayer = Bukkit.getPlayer(args[1]);

            if (Storage.bypassList.contains(other)) {
                Storage.bypassList.remove(other);
                if(otherPlayer != null && otherPlayer.isOnline()){
                    ChatUtils.sendMessage(otherPlayer, "&aNie omijasz juz dluzej zabezpieczen na sektorze " + Storage.serverName);
                    return;
                }
                ChatUtils.sendMessage(sender, "&aGracz " + other + " nie omija juz zabezpieczen na sektorze " + Storage.serverName);
            }
            else {
                Storage.bypassList.add(other);
                if(otherPlayer != null && otherPlayer.isOnline()){
                    ChatUtils.sendMessage(otherPlayer, "&aOd teraz omijasz zabezpieczenia na sektorze " + Storage.serverName);
                    return;
                }
                ChatUtils.sendMessage(sender, "&aGracz " + other + " od teraz omija zabezpieczenia na sektorze " + Storage.serverName);
            }
            return;
        }
        sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) + " " + "[nick]" ));
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
