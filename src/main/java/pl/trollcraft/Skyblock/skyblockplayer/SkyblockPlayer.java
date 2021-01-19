package pl.trollcraft.Skyblock.skyblockplayer;

public class SkyblockPlayer {

    private String islandID;
    private String coopIslandID;

    public SkyblockPlayer(String islandID, String coopIslandID){
        this.islandID = islandID;
        this.coopIslandID = coopIslandID;
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

    public boolean hasIsland(){
        return islandID != null;
    }

    public boolean hasCOOP(){
        return coopIslandID != null;
    }
}
