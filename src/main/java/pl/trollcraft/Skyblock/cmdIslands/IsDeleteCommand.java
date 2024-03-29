package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.DeleteIsland;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsDeleteCommand extends Command {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsDeleteCommand() {
        super(Collections.singletonList("delete"), "Usun wyspe", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
            SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
            if(islandsController.isPlayerOwner(player.getName())) {
                if( islandsController.getIslandByLocation(player.getLocation()) == null ){
                    ChatUtils.sendMessage(player, "&cMusisz znajdowac sie na swojej wyspie, jesli to jest Twoja wyspa, zglos ten fakt administracji");
                    return;
                }
                if( islandsController.getIslandByLocation(player.getLocation()).getOwner().equalsIgnoreCase(player.getName())) {
                    DeleteIsland.deleteIs(islandsController.getIslandById(skyblockPlayer.getIslandOrCoop()));
                    islandsController.setGeneratorOnCooldown();
                    BungeeSupport.sendDeleteIslandCommand(islandsController.getIslandIdByOwnerOrMember(player.getName()), player);
                    islandsController.saveCooldown(player.getName());
                    Debug.log("&aZapisuje cooldown gracza " + player.getName());
                    ChatUtils.sendMessage(player, "&aUsunieto wyspe. Ponowne utworzenie wyspy nie bedzie mozliwe przez jakis czas");
                }
                else{
                    ChatUtils.sendMessage(player, "&cMusisz znajdowac sie na swojej wyspie");
                }
            }
            else{
                ChatUtils.sendMessage(player, "&cNie jestes wlascicielem wyspy");
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {
        ChatUtils.sendMessage(sender, "&cNadal pracujemy nad ta komenda");
        /*

        String owner = island.getOwner();
        BungeeSupport.sendDeleteIslandCommand( islandsController.getIslandIdByOwnerOrMember( owner ), Bukkit.getPlayer(owner));
        DeleteIsland.deleteIs( islandsController.getIslandByOwnerOrMember( owner) );
        ChatUtils.sendMessage((Player) sender, ChatUtils.fixColor("&aUsunieto wyspe gracza " + owner));

         */
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
