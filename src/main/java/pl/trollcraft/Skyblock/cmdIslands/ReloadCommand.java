package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command{

    public ReloadCommand() {
        super(Collections.singletonList("reload"), "Przeladuj plugin", "TcSb.command.reload", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Main.getInstance().loadCommands();
        sender.sendMessage(ChatUtils.fixColor("&aKonfiguracja przeladowana"));
    }

    @Override
    public void admin(CommandSender sender, String[] args, Player p) {
        execute(sender, args);
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
