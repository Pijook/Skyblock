package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.customEvents.RemoveIslandFromMemoryEvent;
import pl.trollcraft.Skyblock.essentials.Debug;

public class RemoveIslandFromMemoryListener implements Listener {

    @EventHandler
    public void onRemove(RemoveIslandFromMemoryEvent event){

        Debug.log("[Custom event]&aRemoved island " + event.getIslandID().toString());

        if(event.getIsland().getServer().equalsIgnoreCase(Storage.serverName)){
            if(event.getIsland().getServer().equalsIgnoreCase(Storage.serverName)){
                Skyblock.getLimitController().saveLimiter(event.getIslandID());
            }

        }
    }
}
