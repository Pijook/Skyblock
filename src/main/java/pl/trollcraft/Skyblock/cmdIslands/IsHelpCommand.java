package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;

import java.util.Collections;
import java.util.List;

public class IsHelpCommand extends Command {

    public IsHelpCommand() {
        super(Collections.singletonList("help"), "Sprawdz dostepne komendy", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( !(sender instanceof Player)) {
            ChatUtils.sendMessage(sender, "&cMusisz byc graczem zeby to zrobic");
            return;
        }

        Player player = (Player) sender;
        player.performCommand("help island");

    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        ChatUtils.sendMessage(sender, "&cNadal pracujemy nad ta komenda");
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
