package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.customEvents.IslandSaveEvent;
import pl.trollcraft.Skyblock.essentials.Debug;

public class IslandSaveListener implements Listener {

    @EventHandler
    public void onIslandSave(IslandSaveEvent event){
        Debug.log("[Custom event]&aSaved island " + event.getIslandID().toString());

        if(event.getIsland().getServer().equalsIgnoreCase(Storage.serverName)){
            Main.getIslandLimiter().saveIsland(event.getIslandID());
        }
    }
}
