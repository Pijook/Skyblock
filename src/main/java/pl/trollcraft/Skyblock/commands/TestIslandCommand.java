package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;

public class TestIslandCommand implements CommandExecutor {

    private final IslandsController islandsController = Skyblock.getIslandsController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("&cKomenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 1){

            if(args[0].equalsIgnoreCase("create")){

                if(islandsController.isPlayerOwner(player.getName())){
                    ChatUtils.sendMessage(player, "&cPosiadasz juz wyspe!");
                    return true;
                }

                if(islandsController.isGeneratorOnCooldown()){
                    ChatUtils.sendMessage(player, "&cSprobuj ponownie za pare sekund...");
                    return true;
                }

                ChatUtils.sendMessage(player, "&aTworze wyspe...");
                islandsController.setGeneratorOnCooldown();
                BungeeSupport.sendGenerateIslandCommand(player);

            }

        }

        return true;
    }
}
