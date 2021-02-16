package pl.trollcraft.Skyblock.skyblockplayer;

import java.util.ArrayList;

public class SkyblockPlayer {

    private String islandID;
    private String coopIslandID;
    private ArrayList<String> invites;
    private int placedBlocks;

    public SkyblockPlayer(String islandID, String coopIslandID, ArrayList<String> invites, int placedBlocks){
        this.islandID = islandID;
        this.coopIslandID = coopIslandID;
        this.invites = invites;
        this.placedBlocks = placedBlocks;

    }

    public String getIslandID() {
        return islandID;
    }

    public void setIslandID(String islandID) {
        this.islandID = islandID;
    }

    public String getCoopIslandID() {
        return coopIslandID;
    }

    public void setCoopIslandID(String coopIslandID) {
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

    public String getIslandOrCoop(){
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
}
