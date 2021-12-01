package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.villagercontroller.VillagerController;

public class CreatureSpawnListener implements Listener {

    private final VillagerController villagerController = Skyblock.getVillagerController();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event){

        if(event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CURED)){
            if(event.getEntity().getType().equals(EntityType.VILLAGER)){
                Villager villager = (Villager) event.getEntity();
                if(villager.getProfession().equals(Villager.Profession.NONE)){
                    Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            villagerController.customizeVillager(villager);
                        }
                    }, 1L);
                }
            }
        }

    }

}
