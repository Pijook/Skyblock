package pl.trollcraft.Skyblock.skyblockplayer;

import java.util.ArrayList;
import java.util.UUID;

public class SkyblockPlayer {

    private UUID islandID;
    private UUID coopIslandID;
    private ArrayList<String> invites;
    private int placedBlocks;
    private boolean isOnIsland;

    public SkyblockPlayer(UUID islandID, UUID coopIslandID, ArrayList<String> invites, int placedBlocks){
        this.islandID = islandID;
        this.coopIslandID = coopIslandID;
        this.invites = invites;
        this.placedBlocks = placedBlocks;
        this.isOnIsland = false;
    }

    public UUID getIslandID(){
        return islandID;
    }

    public void setIslandID(UUID islandID){
        this.islandID = islandID;
    }

    public UUID getCoopIslandID(){
        return coopIslandID;
    }

    public void setCoopIslandID(UUID coopIslandID){
        this.coopIslandID = coopIslandID;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<String> invites) {
        this.invites = invites;
    }

    public boolean hasIsland(){
        return islandID != null;
    }

    public boolean hasCOOP(){
        return coopIslandID != null;
    }

    public int getPlacedBlocks() {
        return placedBlocks;
    }

    public void setPlacedBlocks(int placedBlocks) {
        this.placedBlocks = placedBlocks;
    }

    public UUID getIslandOrCoop(){
        if(islandID != null){
            return islandID;
        }
        if(coopIslandID != null){
            return coopIslandID;
        }

        return null;
    }

    public boolean hasIslandOrCoop(){
        if(islandID != null){
            return true;
        }
        if(coopIslandID != null){
            return true;
        }

        return false;
    }

    public boolean isOnIsland() {
        return isOnIsland;
    }

    public void setOnIsland(boolean onIsland) {
        isOnIsland = onIsland;
    }
}
