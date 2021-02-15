package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.generator.DeleteIsland;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.Islands;

import java.util.Collections;
import java.util.List;

public class IsDeleteCommand extends Command{

    public IsDeleteCommand() {
        super(Collections.singletonList("create"), "Stworz wyspe", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
            if(Islands.isPlayerOwner(player.getName())) {
                DeleteIsland.deleteIs( Islands.getIslandById(player.getName()) ); //TODO Poprawic na BungeeSupport
                sender.sendMessage(ChatUtils.fixColor("&aUsunieto wyspe"));
            }
            else{
                sender.sendMessage(ChatUtils.fixColor("&cNie jestes walscicielem wyspy"));
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        String owner = island.getOwner();
        DeleteIsland.deleteIs( island ); //To zadziala cnie? xD
        sender.sendMessage(ChatUtils.fixColor("&aUsunieto wyspe gracza " + owner));
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
