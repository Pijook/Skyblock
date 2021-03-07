package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsRemoveCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsRemoveCommand() {
        super(Collections.singletonList("remove"), "Usun czlonka swojej wyspy", "TcSb.basic", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( !(sender instanceof Player) ){
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
        Player player = (Player) sender;


        if( args.length > 1 ) {
            if (islandsController.isPlayerOwner(player.getName())) { //If player is owner of island
                String member = args[1];
                if (skyblockPlayerController.getPlayer(member).hasIsland()) {  //If argument has island/is member of island
                    SkyblockPlayer SBmember = skyblockPlayerController.getPlayer(member);
                    if (islandsController.getIslandById(SBmember.getIslandOrCoop()).getOwner().equalsIgnoreCase(member)){
                        skyblockPlayerController.getPlayer(member).setIslandID(null);
                        islandsController.remMember(player.getName(), member);
                        sender.sendMessage(ChatUtils.fixColor("&cUsunieto " + member + " z wyspy"));
                    }
                    else{
                        sender.sendMessage(ChatUtils.fixColor("&c" + member + " nie jest czlonkiem Twojej wyspy"));
                    }
                }
                else{
                    sender.sendMessage(ChatUtils.fixColor("&c" + member + " nie jest czlonkiem Twojej wyspy. Upewnij sie czy wpisales poprawny nick( Wielkosc liter ma znaczenie )"));
                    SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
                    List<String> members = islandsController.getIslandById(skyblockPlayer.getIslandOrCoop()).getMembers();
                    sender.sendMessage(ChatUtils.fixColor("&cCzlonkowie Twojej wyspy: "));
                    for( String mbr : members ){
                        sender.sendMessage(ChatUtils.fixColor("&3" + mbr));
                    }
                }
            }
            else{
                sender.sendMessage(ChatUtils.fixColor("&cNie jestes wlascicielem wyspy"));
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) + " " + "<nick>" ));
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Island island, Player... player) {

    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
