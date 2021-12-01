package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;

import java.util.Collections;
import java.util.List;

public class IsInfoCommand extends Command{

    public IsInfoCommand(){
        super(Collections.singletonList("info"), "Komenda do pozyskiwania informacji o wyspie", PermissionStorage.otherCommandPermission + ".admin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)){
            ChatUtils.sendMessage(sender, "&cTa komenda jest tylko dla graczy!");
            return;
        }

        if(args.length == 1){
            Player player = (Player) sender;
            BungeeSupport.sendGetIslandInfoCommand(player);
            ChatUtils.sendMessage(player, "&cOczekuje na informacje zwrotna od serwera...");
            return;
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) ));
        }

    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        sender.sendMessage(ChatUtils.fixColor("&cBlad. Komenda admin nie moze uzyc komendy admin"));
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
