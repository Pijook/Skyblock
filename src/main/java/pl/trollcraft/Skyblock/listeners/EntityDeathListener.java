package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class EntityDeathListener implements Listener {

    private final WorkerController workerController = Skyblock.getWorkerController();

    @EventHandler
    public void onDeath(EntityDeathEvent event){

        if(event.getEntity().getKiller() != null){
            Player player = event.getEntity().getKiller();

            /*if(workerController.isMobToHunt(event.getEntityType())){
                workerController.getWorkerByName(player.getName()).increaseHuntedAnimals(1);
            }*/
        }

    }
}
