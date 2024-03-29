package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsCreateCommand extends Command {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsCreateCommand() {
        super(Collections.singletonList("create"), "Stworz wyspe", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if( sender instanceof Player) {
            Player player = (Player) sender;
//            if(!player.hasPermission(PermissionStorage.thisIsSpawn)){
            if(!Storage.isSpawn){
                ChatUtils.sendMessage(player, "&cTa komenda dostepna jest tylko na spawnie!");
                return;
            }
            long cooldown = islandsController.getIslandCreateCooldown(player.getName());
            if(cooldown == -1 || cooldown > Storage.createCooldown){
                if (islandsController.isPlayerOwner(player.getName())) {
                    ChatUtils.sendMessage(player, "&cPosiadasz juz wyspe!");
                } else {
                    if(!islandsController.isGeneratorOnCooldown()){
                        ChatUtils.sendMessage(player, "&aTworze wyspe...");
                        //CreateIsland.createNew(player);
                        BungeeSupport.sendGenerateIslandCommand(player);
                    }
                    else {
                        ChatUtils.sendMessage(player, "&cSprobuj ponownie za pare sekund...");
                    }

                }
            }
            else{
                long time = (Storage.createCooldown - cooldown);
                long h = time / 60 / 60;
                long m = (time - ( h * 60 * 60 ) ) / 60;
                long s = (time - ( h * 60 * 60 ) - ( m * 60 ) );
                StringBuilder message = new StringBuilder("");
                if( h != 0){
                    message.append("").append(h).append(" godzin, ");
                }
                if( m != 0){
                    message.append("").append(m).append(" minut, ");
                }
                message.append("").append(s).append(" sekund");
                ChatUtils.sendMessage(player, "&cPonowne stworzenie wyspy bedzie mozliwe za " + message );
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
        if( player.length > 0 ){
            execute(player[0], args);
            ChatUtils.sendMessage((Player) sender, "&aStworzono wyspe jako " + player[0]);
        }
        else {
            ChatUtils.sendMessage((Player) sender, "&cBlad. Nie znaleziono gracza");
        }
         */
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
