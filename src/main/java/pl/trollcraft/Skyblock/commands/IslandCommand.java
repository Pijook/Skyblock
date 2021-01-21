package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.generator.DeleteIsland;
import pl.trollcraft.Skyblock.island.Islands;

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
                if(Islands.isPlayerOwner(player.getName())) {
                    Islands.getIslands();
                }
            }
        }
        return true;
    }
}
