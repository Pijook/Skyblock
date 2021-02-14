package pl.trollcraft.Skyblock.cmdIslands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Command {

    public final List<String> aliases;
    public final String description;
    public final String permission;
    public final boolean player;
    public final boolean enabled = true;

    public Command(List<String> aliases, String description, String permission, boolean player) {
        this.aliases = aliases;
        this.description = description;
        this.permission = permission;
        this.player = player;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public abstract void admin(CommandSender sender, String[] args, Player p);

    public abstract List<String> TabComplete(CommandSender cs, org.bukkit.command.Command cmd, String s, String[] args);
}
