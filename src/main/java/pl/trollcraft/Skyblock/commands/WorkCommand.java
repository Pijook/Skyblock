package pl.trollcraft.Skyblock.commands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class WorkCommand implements CommandExecutor {

    private final WorkerController workerController = Skyblock.getWorkerController();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)){
            Debug.log("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0){

            //Worker worker = workerController.getWorkerByName(player.getName());

            workerController.showGUIToPlayer(player);
            return true;
        }

        ChatUtils.sendMessage(player, "&7/" + label);
        return true;
    }
}
