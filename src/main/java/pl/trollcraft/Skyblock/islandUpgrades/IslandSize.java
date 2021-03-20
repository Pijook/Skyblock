package pl.trollcraft.Skyblock.islandUpgrades;

public class IslandSize {

    private int size;
    private double cost;


    public IslandSize(int size, double cost) {
        this.size = size;
        this.cost = cost;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
