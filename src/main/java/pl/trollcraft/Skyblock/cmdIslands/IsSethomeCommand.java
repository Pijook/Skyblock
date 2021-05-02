package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsSethomeCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsSethomeCommand() {
        super(Collections.singletonList("sethome"), "Ustaw spawn wyspy", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( !(sender instanceof Player) ){
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
        Player player = (Player) sender;
        SkyblockPlayer skyblockOwner = skyblockPlayerController.getPlayer(player.getName());
        Island island = islandsController.getIslandById(skyblockOwner.getIslandOrCoop());
        if( skyblockOwner.hasIsland() ) {
            if (island.getOwner().equalsIgnoreCase(player.getName())){
                if (skyblockOwner.isOnIsland()) {
                    island.setHome(player.getLocation());
                    ChatUtils.sendMessage(player, "&aUstawiono nowy punkt domowy wyspy");
                    BungeeSupport.sendSyncHomeCommand(skyblockOwner.getIslandOrCoop(), player);
                }
                else {
                    ChatUtils.sendMessage(player, "&cPunkt domowy wyspy musi znajdować się na wyspie");
                }
            }
            else{
                ChatUtils.sendMessage(player, "&cMusisz być właścicielem wyspy by ustawic punkt domowy");
            }
        }
        else {
            ChatUtils.sendMessage(player, "&cNie posiadasz wyspy");
        }
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
