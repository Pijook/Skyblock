package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.customEvents.IslandSaveEvent;
import pl.trollcraft.Skyblock.essentials.Debug;

public class IslandSaveListener implements Listener {

    @EventHandler
    public void onIslandSave(IslandSaveEvent event){
        Debug.log("[Custom event]&aSaved island " + event.getIslandID().toString());

    }
}
