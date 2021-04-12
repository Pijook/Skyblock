package pl.trollcraft.Skyblock.limiter;

import pl.trollcraft.Skyblock.essentials.Debug;

public class Limiter {

    private int level;
    private int currentAmount;

    public Limiter(int level, int currentAmount){
        this.level = level;
        this.currentAmount = currentAmount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public void increaseLevel(int amount){
        level = level + amount;
    }

    public void increaseAmount(int amount){
        currentAmount = currentAmount + amount;
    }

    public void decreaseLevel(int amount){
        level = level - amount;
        if(level < 1){
            level = 1;
        }
    }

    public void decreaseAmount(int amount){
        currentAmount = currentAmount - amount;
        if(currentAmount < 0){
            currentAmount = 0;
        }
    }

}
