package pl.trollcraft.Skyblock.island;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Island {

    private String owner;
    private List<String> members;
    private Location center;
    private Location spawn;
    private int islandLevel;
    private Location point1;
    private Location point2;

    public Island(String owner, List<String> members, Location center, Location spawn, int islandLevel, Location point1, Location point2){
        this.owner = owner;
        this.members = members;
        this.center = center;
        this.spawn = spawn;
        this.islandLevel = islandLevel;
        this.point1 = point1;
        this.point2 = point2;
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Location getPoint2() {
        return point2;
    }

    public void setPoint2(Location point2) {
        this.point2 = point2;
    }

    public Location getPoint1() {
        return point1;
    }

    public void setPoint1(Location point1) {
        this.point1 = point1;
    }
}
