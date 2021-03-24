package pl.trollcraft.Skyblock.cost;

public class Cost {

    private double playerLevel;
    private double money;

    public Cost(double playerLevel, double money){
        this.playerLevel = playerLevel;
        this.money = money;
    }

    public double getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(double playerLevel) {
        this.playerLevel = playerLevel;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
