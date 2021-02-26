package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class IsAdminCommand extends Command{

    private final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    public static ArrayList<UUID> currentlyUsedIslands = new ArrayList<>();

    public IsAdminCommand() {
        super(Collections.singletonList("admin"), "Komenda symulujaca wybrana wyspe jako Twoja", "TcSb.command.admin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) { //TODO WYMAGA POPRAWY
        if( args.length == 2 || args.length == 3 ) {
            String memberOf = args[1];
            Island island = null;

            //TODO Pobieranie wyspy do ktorej nalezy @memberOf

            if( island != null) {

                String owner = island.getOwner();
                SkyblockPlayer SBowner = skyblockPlayerController.getPlayer(owner);
                currentlyUsedIslands.add(SBowner.getIslandOrCoop());

                if (args.length == 2) {
                    sender.sendMessage("Otwieram GUI wyspy gracza " + island.getOwner());
                    //TODO Otwieranie GUI
                }
                else {
                    for (Command command : Main.getInstance().getCommandManager().commands) {
                        if (command.aliases.contains(args[2]) && command.enabled) {

                            command.admin(sender, args, island);

                        }
                    }
                }
                currentlyUsedIslands.remove(island.getOwner());
            }
            else{
                boolean sth = false;
                for (Command command : Main.getInstance().getCommandManager().commands) {
                    if (command.aliases.contains(args[2]) && command.enabled) {
                        if ((Bukkit.getPlayer(memberOf) != null) && (Bukkit.getPlayer(memberOf).isOnline())) {
                            Player p = Bukkit.getPlayer(memberOf);
                            sth = true;
                            command.admin(sender, args, null, p);
                        }
                    }
                }
                if( !sth ){
                    sender.sendMessage(ChatUtils.fixColor("Nie znaleziono gracza o tym nicku"));
                }
            }
        }
        else{
            sender.sendMessage(ChatUtils.fixColor("&c/is " + aliases.get(0) + " " + "<nick> <argument>" ));
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
