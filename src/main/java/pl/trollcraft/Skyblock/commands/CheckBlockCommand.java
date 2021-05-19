package pl.trollcraft.Skyblock.commands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

public class CheckBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("&cKomenda tylko dla graczy!");
            return true;
        }
        if( !sender.hasPermission(PermissionStorage.admCommands)){
            ChatUtils.sendMessage(sender, "&cNie masz dostepu do tej komendy!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0){
            Block block = player.getTargetBlock(null, 200);

            if(block == null){
                ChatUtils.sendMessage(player, "&cMusisz patrzec na blok!");
                return true;
            }

            ChatUtils.sendMessage(player, block.getType().name());
            return true;
        }

        ChatUtils.sendMessage(player, "&7/" + label);
        return true;
    }
}
