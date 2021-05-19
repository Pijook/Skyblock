package pl.trollcraft.Skyblock.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

public class GameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("&cKomenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;


        if(args.length == 1){
            if(!player.hasPermission(PermissionStorage.gamemodeOwn)){
                ChatUtils.sendMessage(player, "&cNie masz dostepu do tej komendy!");
                return true;
            }
            if(!Utils.isInteger(args[0])){
                int mode = Integer.parseInt(args[0]);
                if(mode >= 0 && mode <= 3){
                    setGameMode(player, GameMode.getByValue(mode));
                    return true;
                }
            }
        }

        if(args.length == 2){
            if(!player.hasPermission(PermissionStorage.gamemodeOther)){
                ChatUtils.sendMessage(player, "&cNie mozesz wykonac tej komendy na innym graczu!");
                return true;
            }
            if(!Utils.isInteger(args[0])){
                Player target = Bukkit.getPlayer(args[1]);

                if(target == null || !target.isOnline()){
                    ChatUtils.sendMessage(player, "&cGracz musi byc online!");
                    return true;
                }

                int mode = Integer.parseInt(args[0]);
                if(mode >= 1 && mode <= 3){
                    setGameMode(target, GameMode.getByValue(mode));
                    ChatUtils.sendMessage(player, "&7Ustawiono tryb gry gracza " + args[1] + " na &a" + target.getGameMode().name().toLowerCase());
                    return true;
                }

            }
        }


        ChatUtils.sendMessage(player, "&7/" + label + " <0/1/2/3>");
        ChatUtils.sendMessage(player, "&7/" + label + " <0/1/2/3> <nick>");
        return true;
    }

    private void setGameMode(Player player, GameMode gameMode){
        player.setGameMode(gameMode);
        ChatUtils.sendMessage(player, "&7Ustawiono tryb gry na &a" + gameMode.name().toLowerCase());
    }
}
