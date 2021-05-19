package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

public class TradesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

//        if(!(sender instanceof Player)){
//            Debug.log("&cKomenda tylko dla graczy!");
//            return true;
//        }
        if( !sender.hasPermission(PermissionStorage.admCommands)){
            ChatUtils.sendMessage(sender, "&cNie masz dostepu do tej komendy!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                Skyblock.getVillagerController().load();
                ChatUtils.sendMessage(player, "&aPrzeladowano trade'y!");
                return true;
            }
        }

        ChatUtils.sendMessage(player, "&7/" + label + " reload");
        return true;
    }
}
