package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.Debug;
import scala.annotation.meta.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandManager implements CommandExecutor, TabCompleter {

    public List<Command> commands = new ArrayList<>();

    public CommandManager(String command){
        try {
            Main.getInstance().getCommand(command).setExecutor(this);
        } catch ( NullPointerException e){
            e.printStackTrace();
            Debug.sendError("Blad przy rejestracji " + command);
        }
    }

    public void registerCommands() {
        Arrays.stream(Main.getInstance().getCommands().getClass().getFields())
                .map(field -> {
                    try {
                        return (Command) field.get(Main.getInstance().getCommands());
                    } catch (IllegalAccessException exception) {
                        exception.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(command -> command.getClass().getSuperclass() == Command.class)
                .forEach(this::registerCommand);
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void unRegisterCommand(Command command) {
        commands.remove(command);
    }

    @Override
    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        if ( !(cs.hasPermission("test.komenda"))){
            cs.sendMessage("Brak perimsji");
            return false;
        }
        if (args.length != 0) {
            for (Command command : commands) {
                if (command.aliases.contains(args[0]) && command.enabled) {
                    if (command.player && !(cs instanceof Player)) {
                        // Must be a player
                        cs.sendMessage("%prefix% Musisz byc graczem"
                                .replace("%prefix%", "TcSB"));
                        return true;
                    }
                    if ((cs.hasPermission(command.permission) || command.permission
                            .equalsIgnoreCase("") || command.permission
                            .equalsIgnoreCase("TcSB.basic")) && command.enabled) {
                        command.execute(cs, args);
                    } else {
                        // No permission
                        cs.sendMessage("%prefix% Brak uprawnien"
                                .replace("%prefix%", "TcSB"));
                    }
                    return true;
                }
            }
            Debug.log("&aSprawdzilem wszystkie komendy");
        }
        else {
            cs.sendMessage("%prefix% Podaj argument"
                    .replace("%prefix%", "TcS"));
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args) {
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<>();
            for (Command command : commands) {
                for (String alias : command.aliases) {
                    if (alias.toLowerCase().startsWith(args[0].toLowerCase()) && (
                            command.enabled && (cs.hasPermission(command.permission)
                                    || command.permission.equalsIgnoreCase("") || command.permission
                                    .equalsIgnoreCase("TcSB.basic")))) {
                        result.add(alias);
                    }
                }
            }
            return result;
        }
        for (Command command : commands) {
            if (command.aliases.contains(args[0]) && (command.enabled && (
                    cs.hasPermission(command.permission) || command.permission.equalsIgnoreCase("")
                            || command.permission.equalsIgnoreCase("TcSB.basic")))) {
                return command.TabComplete(cs, cmd, s, args);
            }
        }
        return null;
    }
}
