package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.Islands;

import java.util.Collections;
import java.util.List;

public class IsCreateCommand extends Command{

    public IsCreateCommand() {
        super(Collections.singletonList("create"), "Stworz wyspe", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
            if (Islands.isPlayerOwner(player.getName())) {
                sender.sendMessage(ChatUtils.fixColor("&cPosiadasz juz wyspe!"));
            } else {
                sender.sendMessage(ChatUtils.fixColor("&aTworze wyspe..."));
                //CreateIsland.createNew(player);
                BungeeSupport.sendGenerateIslandCommand(player);
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        if( player.length > 0 ){
            execute(player[0], args);
            sender.sendMessage(ChatUtils.fixColor("&aStworzono wyspe jako " + player[0]));
        }
        else {
            sender.sendMessage(ChatUtils.fixColor("&cBlad. Nie znaleziono gracza"));
        }
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}