package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsRemoveCommand extends Command {

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsRemoveCommand() {
        super(Collections.singletonList("remove"), "Usun czlonka swojej wyspy", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( !(sender instanceof Player) ){
            sender.sendMessage(ChatUtils.fixColor("&cKomenda tylko dla graczy"));
        }
        Player player = (Player) sender;


        if( args.length > 1 ) {

            Island island = islandsController.getIslandById(skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop());

            if(island == null){
                ChatUtils.sendMessage(player, "&cNie posiadasz wyspy!");
                return;
            }

            if (island.getOwner().equalsIgnoreCase(player.getName())) { //If player is owner of island

                if(!islandsController.isPlayerOnHisIsland(player)){
                    ChatUtils.sendMessage(player, "&cMusisz byc na swojej wyspie aby to zrobic!");
                    return;
                }

                String memberNickname = args[1];

                if(memberNickname.equalsIgnoreCase(player.getName())){
                    ChatUtils.sendMessage(player, "&cNie mozesz tego zrobic!");
                    return;
                }

                if(island.getMembers() == null){
                    ChatUtils.sendMessage(player, "&cGracz" + memberNickname + " nie nalezy do twojej wyspy!");
                    return;
                }
                if(!island.getMembers().contains(memberNickname)){
                    ChatUtils.sendMessage(player, "&cGracz" + memberNickname + " nie nalezy do twojej wyspy!");
                    return;
                }

                island.removeMember(memberNickname);

                Player member = Bukkit.getPlayer(memberNickname);

                //When member is online on same server as owner
                if(member != null && member.isOnline()){
                    SkyblockPlayer skyblockMember = skyblockPlayerController.getPlayer(memberNickname);
                    skyblockMember.setOnIsland(false);
                    skyblockMember.setIslandID(null);

                    ChatUtils.sendMessage(member, "&cZostales usuniety z wyspy");
                    ChatUtils.sendMessage(player, "&cGracz " + memberNickname + " zostal usuniety z wyspy");
                    //member.teleport(Storage.spawn);
                    member.teleport(new Location(member.getWorld(), Storage.spawn.getX(), Storage.spawn.getY(), Storage.spawn.getZ()));
                    return;
                }//When member is on different server
                else{
                    BungeeSupport.sendRemoveMemberCommand(memberNickname, player);
                    ChatUtils.sendMessage(player, "&cGracz " + memberNickname + " zostal usuniety z wyspy");
                    return;
                }

            }
            else{
                ChatUtils.sendMessage((Player) sender, "&cNie jestes wlascicielem wyspy");
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) + " " + "<nick>" ));
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
