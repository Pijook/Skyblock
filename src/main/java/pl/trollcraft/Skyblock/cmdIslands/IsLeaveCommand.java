package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.Collections;
import java.util.List;

public class IsLeaveCommand extends Command{

    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    public IsLeaveCommand() {
        super(Collections.singletonList("leave"), "Odejdz z wyspy, której jesteś członkiem", "" + PermissionStorage.basicCommandPermission, true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if( args.length != 1 ){
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) ));
            return;
        }
        Player player = (Player) sender;
        SkyblockPlayer skyblockMember = skyblockPlayerController.getPlayer(player.getName());

        /*if (islandsController.isPlayerOwner(player.getName())) {
            ChatUtils.sendMessage(player, "&cNie mozesz odejsc ze swojej wyspy!");
            return;
        }
        if(!islandsController.isPlayerOnHisIsland(player)){
            ChatUtils.sendMessage(player, "&cMusisz byc na swojej wyspie aby to zrobic!");
            return;
        }*/

        if(!skyblockMember.hasIslandOrCoop()){
            ChatUtils.sendMessage(player, "&cNie nalezysz do zadnej wyspy!");
            return;
        }

        Island island = islandsController.getIslandById(skyblockMember.getIslandOrCoop());

        if(island == null){
            ChatUtils.sendMessage(player, "&cNie nalezysz do zadnej wyspy!");
            return;
        }

        if(!islandsController.isLocationOnIsland(player.getLocation(), skyblockMember.getIslandOrCoop())){
            ChatUtils.sendMessage(player, "&cMusisz byc na swojej wyspie aby to zrobic!");
            return;
        }

        if(island.getOwner().equalsIgnoreCase(player.getName())){
            ChatUtils.sendMessage(player, "&cNie mozesz odejsc ze swojej wyspy!");
            return;
        }

        island.removeMember(player.getName());
        skyblockMember.setOnIsland(false);
        skyblockMember.setIslandID(null);

        ChatUtils.sendMessage(player, "&aNie jestes juz dluzej czlonkiem wyspy gracza " + island.getOwner() );

        Player owner = Bukkit.getPlayer(island.getOwner());
        if( owner != null && owner.isOnline() ){
            ChatUtils.sendMessage(owner, "&aGracz " + player.getName() + " odszedl z Twojej wyspy" );
        }
        for( String memberName : island.getMembers() ){
            Player member = Bukkit.getPlayer(memberName);
            if( member != null && member.isOnline() ){
                ChatUtils.sendMessage(member, "&aGracz " + player.getName() + " odszedl z Twojej wyspy" );
            }
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
