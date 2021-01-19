package pl.trollcraft.Skyblock.skyblockplayer;

public class SkyblockPlayer {

    private int islandID;

    public SkyblockPlayer(int islandID){
        this.islandID = islandID;
    }

    public int getIslandID() {
        return islandID;
    }

    public void setIslandID(int islandID) {
        this.islandID = islandID;
    }
}
