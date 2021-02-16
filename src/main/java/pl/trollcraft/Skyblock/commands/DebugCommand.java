package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class DebugCommand implements CommandExecutor {

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("skyblock.debug")){
            ChatUtils.sendMessage(player, "&f&lPlaced blocks:" + skyblockPlayerController.getPlayer(player.getName()).getPlacedBlocks());
        }

        return true;
    }
}
