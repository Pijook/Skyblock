package pl.trollcraft.Skyblock.listeners.customListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.Skyblock.customEvents.PlayerSaveEvent;
import pl.trollcraft.Skyblock.essentials.Debug;

public class PlayerSaveListener implements Listener {

    @EventHandler
    public void onPlayerSave(PlayerSaveEvent event){
        Debug.log("[Custom event]&aSaved player " + event.getPlayer().getName());
    }
}
