package pl.trollcraft.Skyblock.limiter;

import pl.trollcraft.Skyblock.essentials.Debug;

import java.util.HashMap;

public class IslandLimiter {

    private HashMap<String, Limiter> islandLimiters;

    public IslandLimiter(HashMap<String, Limiter> islandLimiters){
        this.islandLimiters = islandLimiters;
    }

    public HashMap<String, Limiter> getIslandLimiters() {
        return islandLimiters;
    }

    public void setIslandLimiters(HashMap<String, Limiter> islandLimiters) {
        this.islandLimiters = islandLimiters;
    }

    public Limiter getLimiter(String type){
        if(islandLimiters.containsKey(type)){
            return islandLimiters.get(type);
        }
        return null;
    }

    public void addLimiter(Limiter limiter, String type){
        islandLimiters.put(type, limiter);
    }

    public void increaseAmount(String type, int amount){
        islandLimiters.get(type).increaseAmount(amount);
    }

    public void increaseLevel(String type, int amount){
        islandLimiters.get(type).increaseLevel(amount);
    }

    public void decreaseAmount(String type, int amount){
        if(islandLimiters.containsKey(type)){
            islandLimiters.get(type).decreaseAmount(amount);
        }
        else{
            islandLimiters.put(type, new Limiter(1, 0));
        }
    }

    public void decreaseLevel(String type, int amount){
        islandLimiters.get(type).decreaseLevel(amount);
    }

    public void debug(){
        for(String type : islandLimiters.keySet()){
            Debug.log("(" + type + ")" + " Level: " + islandLimiters.get(type).getLevel() + " Amount: " + islandLimiters.get(type).getCurrentAmount());
        }
    }

}
