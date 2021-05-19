package pl.trollcraft.Skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class AdminWorkCommand implements CommandExecutor {

    private final WorkerController workerController = Skyblock.getWorkerController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if( !sender.hasPermission(PermissionStorage.admCommands)){
            ChatUtils.sendMessage(sender, "&cNie masz dostepu do tej komendy!");
            return true;
        }

        if(args.length == 3){

            Worker worker = workerController.getWorkerByName(player.getName());

            String jobName = args[1];

            if(Utils.isInteger(args[2])){
                ChatUtils.sendMessage(player, "&cValue must be integer");
                return true;
            }

            int value = Integer.parseInt(args[2]);

            if(!worker.doesJobExist(jobName)){
                ChatUtils.sendMessage(player, "&cPraca o takiej nazwie nie istnieje!");
                return true;
            }

            if(args[0].equalsIgnoreCase("level")){
                worker.setJobLevel(jobName, value);
                ChatUtils.sendMessage(player, "&aUstawiono level " + jobName + " na " + value);
                return true;
            }
            if(args[0].equalsIgnoreCase("score")){
                worker.setJobScore(jobName, value);
                ChatUtils.sendMessage(player, "&aUstawiono score " + jobName + " na " + value);
                return true;
            }

        }

        ChatUtils.sendMessage(player, "&7/" + label + " level <jobName> <value>");
        ChatUtils.sendMessage(player, "&7/" + label + " score <jobName> <value>");
        return true;
    }
}
