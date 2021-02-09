package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if( args.length > 0 ){
            if( args[0].equalsIgnoreCase("create")){
//TODO
            }
        }


        return true;
    }
}
