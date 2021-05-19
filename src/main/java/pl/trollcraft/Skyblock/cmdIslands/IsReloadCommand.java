package pl.trollcraft.Skyblock.cmdIslands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Settings;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.dropManager.DropManager;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;

import java.util.Collections;
import java.util.List;

public class IsReloadCommand extends Command {

    public IsReloadCommand() {
        super(Collections.singletonList("reload"), "Przeladuj plugin", "" + PermissionStorage.otherCommandPermission + ".reload", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Skyblock.getInstance().loadCommands();
        Debug.log("&aPrzeladowano komendy!");
        Settings.load();
        Debug.log("&aPrzeladowano settings!");;
        ChatUtils.sendMessage((Player) sender, "&aKonfiguracja przeladowana");
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        ChatUtils.sendMessage(sender, "&cNadal pracujemy nad ta komenda");
//        execute(sender, args);
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
