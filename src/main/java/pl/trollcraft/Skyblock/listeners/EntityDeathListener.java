package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

import java.util.UUID;

public class EntityDeathListener implements Listener {

    private final WorkerController workerController = Skyblock.getWorkerController();
    private final LimitController limitController = Skyblock.getLimitController();
    private final IslandsController islandsController = Skyblock.getIslandsController();

    @EventHandler
    public void onDeath(EntityDeathEvent event){

        Entity entity = event.getEntity();

        if(event.getEntity().getKiller() != null){
            Player player = event.getEntity().getKiller();

            if(entity.getType().equals(EntityType.COW)
                    || entity.getType().equals(EntityType.SHEEP)
                    || entity.getType().equals(EntityType.PIG)
                    || entity.getType().equals(EntityType.CHICKEN)){

                Skyblock.getSkyblockPlayerController().getPlayer(player.getName()).increaseKilledAnimals(1);

            }

            if(workerController.isMobToHunt(event.getEntityType())){
                Worker worker = workerController.getWorkerByName(player.getName());
                worker.increaseHuntedAnimals(1);

                if(workerController.canLevelUp(worker, "hunter")){
                    workerController.levelUpJob(worker, "hunter");
                    ChatUtils.sendMessage(player, "&aOsiagnales nowy lvl pracy!");
                }
            }
        }

        String type = event.getEntityType().name();
        if(limitController.isTypeLimited(entity.getType().name())){
            UUID islandID = islandsController.getIslandIDByLocation(entity.getLocation());
            if(islandID == null){
                return;
            }
            limitController.decreaseType(type, islandID);
        }

    }
}
