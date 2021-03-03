package pl.trollcraft.Skyblock.customEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;

public class PlayerLoadEvent extends Event {

    private final Player player;
    private final SkyblockPlayer skyblockPlayer;

    public PlayerLoadEvent(Player player, SkyblockPlayer skyblockPlayer){
        this.player = player;
        this.skyblockPlayer = skyblockPlayer;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

    public SkyblockPlayer getSkyblockPlayer() {
        return skyblockPlayer;
    }

    public Player getPlayer() {
        return player;
    }
}
