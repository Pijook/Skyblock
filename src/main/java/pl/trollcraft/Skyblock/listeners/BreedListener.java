package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.world.StructureGrowEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class BreedListener implements Listener {

    private final WorkerController workerController = Skyblock.getWorkerController();

    @EventHandler
    public void onBreed(EntityBreedEvent event){
        Player player = (Player) event.getBreeder();

        Worker worker = workerController.getWorkerByName(player.getName());
        worker.increaseBreedAnimals(1);

        if(workerController.canLevelUp(worker,  "farmer")){
            workerController.levelUpJob(worker, "farmer");
            ChatUtils.sendMessage(player, "&aOsiagnales nowy lvl pracy!");
        }
    }


}
