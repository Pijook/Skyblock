package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.world.StructureGrowEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.Worker;
import pl.trollcraft.Skyblock.worker.WorkerController;

public class BreedListener implements Listener {

    private final WorkerController workerController = Skyblock.getWorkerController();
    private final LimitController limitController = Skyblock.getLimitController();
    private final IslandsController islandsController = Skyblock.getIslandsController();
    private final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    @EventHandler
    public void onBreed(EntityBreedEvent event){
        Player player = (Player) event.getBreeder();

        if(!islandsController.isPlayerOnHisIsland(player)){
            event.setCancelled(true);
            return;
        }

        String type = event.getEntity().getType().name();

        if(limitController.isTypeLimited(type)){
//            Debug.log("Entity is limited!");
            if(limitController.isAboveLimit(type, skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop())){
                event.setCancelled(true);
                ChatUtils.sendMessage(player, "&cOsiagnieto limit tych zwierzat!");
                return;
            }
            else{
                //limitController.increaseType(type, skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop());
            }
        }

        Worker worker = workerController.getWorkerByName(player.getName());
        worker.increaseBreedAnimals(1);

        if(workerController.canLevelUp(worker,  "breeder")){
            workerController.levelUpJob(worker, "breeder");
            ChatUtils.sendMessage(player, "&aOsiagnales nowy lvl pracy!");
        }
    }


}
