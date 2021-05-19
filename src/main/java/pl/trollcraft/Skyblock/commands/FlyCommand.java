package pl.trollcraft.Skyblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if( args.length == 0) {
            if( !player.hasPermission(PermissionStorage.flyOwn)){
                ChatUtils.sendMessage(player, "&cNie masz dostepu do tej komendy!");
                return true;
            }

            if (player.isFlying()) {
                player.setFlying(false);
                ChatUtils.sendMessage(player, "&7Wylaczono fly!");
                return true;
            }
            if (!player.isFlying()) {
                player.setAllowFlight(true);
                player.setFlying(true);
                ChatUtils.sendMessage(player, "&7Wlaczono fly!");
                return true;
            }
        }
        if( args.length == 1 || args.length == 2){
            if( !player.hasPermission(PermissionStorage.flyOther)){
                ChatUtils.sendMessage(player, "&cNie mozesz wykonac tej komendy na innym graczu!");
                return true;
            }
            Player other = Bukkit.getPlayer(args[0]);

            if(other == null || !other.isOnline()){
                ChatUtils.sendMessage(player, "&cGracz musi byc online!");
                return true;
            }
            if( args.length == 2){
                if( args[1].equalsIgnoreCase("disable") ){
                    player.setFlying(false);
                    ChatUtils.sendMessage(player, "&7Wylaczono fly dla " + other.getName() + "!");
                    ChatUtils.sendMessage(other, "&7Wylaczono fly!");
                    return true;
                }
                if( args[1].equalsIgnoreCase("enable") ){
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    ChatUtils.sendMessage(player, "&7Wlaczono fly dla " + other.getName() + "!");
                    ChatUtils.sendMessage(other, "&7Wlaczono fly!");
                    return true;
                }
            }
            if (player.isFlying()) {
                player.setFlying(false);
                ChatUtils.sendMessage(player, "&7Wylaczono fly dla " + other.getName() + "!");
                ChatUtils.sendMessage(other, "&7Wylaczono fly!");
                return true;
            }
            if (!player.isFlying()) {
                player.setAllowFlight(true);
                player.setFlying(true);
                ChatUtils.sendMessage(player, "&7Wlaczono fly dla " + other.getName() + "!");
                ChatUtils.sendMessage(other, "&7Wlaczono fly!");
                return true;
            }
            return true;
        }

        ChatUtils.sendMessage(player, "&7/" + label);
        return true;
    }
}
