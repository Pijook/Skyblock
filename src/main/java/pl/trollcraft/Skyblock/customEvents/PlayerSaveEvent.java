package pl.trollcraft.Skyblock.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;

public class PlayerSaveEvent extends Event {

    private final Player player;
    private final SkyblockPlayer skyblockPlayer;

    public PlayerSaveEvent(Player player, SkyblockPlayer skyblockPlayer){
        this.player = player;
        this.skyblockPlayer = skyblockPlayer;
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

    public SkyblockPlayer getSkyblockPlayer() {
        return skyblockPlayer;
    }

    public Player getPlayer() {
        return player;
    }
}
