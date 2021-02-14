package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;

import java.util.Collections;
import java.util.List;

public class AdminCommand extends Command{


    public AdminCommand() {
        super(Collections.singletonList("admin"), "Komenda symulujaca wybrana wyspe jako Twoja", "TcSb.command.admin", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) { //TODO WYMAGA POPRAWY
        if( args.length == 2 || args.length == 3 ){
            if( Bukkit.getPlayer(args[1]) != null){
                if( Bukkit.getPlayer(args[1]).isOnline() ){
                    Player p = Bukkit.getPlayer(args[1]);
                    if( args.length == 2) {
                        sender.sendMessage("Otwieram GUI wyspy gracza " + p.getName());
                    }
                    else{
                        for (Command command : Main.getInstance().getCommandManager().commands) {
                            if (command.aliases.contains(args[2]) && command.enabled) {
                                if ((sender.hasPermission(command.permission) || command.permission.equalsIgnoreCase("") ||
                                        command.permission.equalsIgnoreCase("tester.")) && command.enabled) {
                                    command.admin(sender, args, p);
                                } else {
                                    sender.sendMessage("Brak uprawnien");
                                }
                            }
                        }
                    }
                }
                else{
                    sender.sendMessage("Gracz nie jest online");
                }
            }
            else{
                sender.sendMessage("Brak gracza");
            }
        }
        else{
            sender.sendMessage("/is admin <nick> <argument>");
        }
    }

    @Override
    public void admin(CommandSender sender, String[] args, Player p) {
        sender.sendMessage("BLAD nie mozesz wykonac komendy admina na sobie");
//        execute(sender, args);
    }

    @Override
    public List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        return null;
    }
}
