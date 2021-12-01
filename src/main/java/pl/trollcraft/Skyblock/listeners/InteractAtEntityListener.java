package pl.trollcraft.Skyblock.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractAtEntityListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event){

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if(entity.getType().equals(EntityType.VILLAGER)){
            Villager villager = (Villager) entity;
            if(villager.getProfession().equals(Villager.Profession.NONE)){
                if(player.getInventory().getItemInMainHand().getType().equals(Material.NAME_TAG)){
                    event.setCancelled(true);
                }
            }
        }

    }

}
