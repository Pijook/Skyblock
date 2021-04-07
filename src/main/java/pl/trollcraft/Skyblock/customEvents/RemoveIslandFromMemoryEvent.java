package pl.trollcraft.Skyblock.customEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.island.Island;

import java.util.UUID;

public class RemoveIslandFromMemoryEvent extends Event {

    private final UUID islandID;
    private final Island island;

    public RemoveIslandFromMemoryEvent(UUID islandID, Island island){
        this.islandID = islandID;
        this.island = island;
    }

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList(){
        return HANDLER_LIST;
    }

    public UUID getIslandID() {
        return islandID;
    }

    public Island getIsland() {
        return island;
    }
}
