package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

public class ReloadCommand implements CommandExecutor {

    /*

        Command currently disabled

     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("&cKomenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("skyblock.admin")){
            ChatUtils.sendMessage(player, "&cNie masz dostepu do tej komendy!");
            return true;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("limits")){

            }
        }

        ChatUtils.sendMessage(player, "&7/" + label + " limits");
        return true;
    }
}
