package pl.trollcraft.Skyblock.listeners;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.Debug;

public class EntityTransformListener implements Listener {

    @EventHandler
    public void onTransform(EntityTransformEvent event){

        if(event.getTransformReason().equals(EntityTransformEvent.TransformReason.CURED)){
            Debug.log(event.getEntity().getType());
            Debug.log(event.getTransformedEntity().getType());
            Villager villager = (Villager) event.getTransformedEntity();
            Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    Skyblock.getVillagerController().customizeVillager(villager);
                }
            }, 1L);
        }

    }

}
