package pl.trollcraft.Skyblock.island;

import org.bukkit.Location;

import java.util.ArrayList;

public class Island {

    private String owner;
    private ArrayList<String> members;
    private Location center;
    private Location spawn;
    private int islandLevel;

    public Island(String owner, ArrayList<String> members, Location center, Location spawn, int islandLevel){
        this.owner = owner;
        this.members = members;
        this.center = center;
        this.spawn = spawn;
        this.islandLevel = islandLevel;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public Location getHome() {
        return spawn;
    }

    public void setHome(Location home) {
        this.spawn = home;
    }

    public int getIslandLevel() {
        return islandLevel;
    }

    public void setIslandLevel(int islandLevel) {
        this.islandLevel = islandLevel;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

}
