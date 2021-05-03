package pl.trollcraft.Skyblock.island.bungeeIsland;

import java.util.ArrayList;
import java.util.List;

public class BungeeIsland {

    private String owner;
    private List<String> members;
    private Location center;
    private Location spawn;
    private int islandLevel;
    private Location point1;
    private Location point2;
    private String server;
    private int points;

    public BungeeIsland(String owner, List<String> members, Location center, Location spawn, int islandLevel, Location point1, Location point2, String server, int points){
        this.owner = owner;
        this.members = members;
        this.center = center;
        this.spawn = spawn;
        this.islandLevel = islandLevel;
        this.point1 = point1;
        this.point2 = point2;
        this.server = server;
        this.points = points;
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

    public void setMembers(ArrayList<String> members) {
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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
