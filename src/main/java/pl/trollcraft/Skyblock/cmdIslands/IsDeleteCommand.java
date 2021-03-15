package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.generator.DeleteIsland;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsDeleteCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsDeleteCommand() {
        super(Collections.singletonList("delete"), "Usun wyspe", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
            SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
            if(islandsController.isPlayerOwner(player.getName())) {
                if( islandsController.getIslandByLocation(player.getLocation()).getOwner().equalsIgnoreCase(player.getName())) {
                    BungeeSupport.sendDeleteIslandCommand(islandsController.getIslandIdByOwnerOrMember(player.getName()), player);
                    DeleteIsland.deleteIs(islandsController.getIslandById(skyblockPlayer.getIslandOrCoop()));
                    sender.sendMessage(ChatUtils.fixColor("&aUsunieto wyspe"));
                }
                else{
                    player.sendMessage(ChatUtils.fixColor("&cMusisz znajdowac sie na swojej wyspie"));
                }
            }
            else{
                sender.sendMessage(ChatUtils.fixColor("&cNie jestes wlascicielem wyspy"));
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        String owner = island.getOwner();
        BungeeSupport.sendDeleteIslandCommand( islandsController.getIslandIdByOwnerOrMember( owner ), Bukkit.getPlayer(owner));
        DeleteIsland.deleteIs( islandsController.getIslandByOwnerOrMember( owner) );
        sender.sendMessage(ChatUtils.fixColor("&aUsunieto wyspe gracza " + owner));
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
