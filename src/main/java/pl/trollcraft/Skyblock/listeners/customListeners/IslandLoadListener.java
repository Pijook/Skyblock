package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.customEvents.IslandLoadEvent;
import pl.trollcraft.Skyblock.essentials.Debug;

public class IslandLoadListener implements Listener {

    @EventHandler
    public void onIslandLoad(IslandLoadEvent event){
        Debug.log("[Custom event]&aLoaded island " + event.getIslandID().toString());

        if(event.getIsland().getServer().equalsIgnoreCase(Storage.serverName)){
            Main.getIslandLimiter().loadIsland(event.getIslandID());
        }
    }
}
