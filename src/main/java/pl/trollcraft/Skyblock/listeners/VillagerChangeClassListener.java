package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.villagercontroller.VillagerController;

public class VillagerChangeClassListener implements Listener {

    private final VillagerController villagerController = Skyblock.getVillagerController();

    @EventHandler
    public void onChange(VillagerCareerChangeEvent event){

        if(event.getEntity().getProfession().equals(Villager.Profession.NONE)){
            Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    villagerController.customizeVillager(event.getEntity());
                }
            }, 1L);
        }
        else{
            event.setCancelled(true);
        }

    }
}
