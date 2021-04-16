package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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

        if(player.isFlying()){
            player.setFlying(false);
            ChatUtils.sendMessage(player, "&7Wylaczono fly!");
            return true;
        }
        if(!player.isFlying()){
            player.setAllowFlight(true);
            player.setFlying(true);
            ChatUtils.sendMessage(player, "&7Uruchiomono fly!");
            return true;
        }

        ChatUtils.sendMessage(player, "&7/" + label);
        return true;
    }
}
