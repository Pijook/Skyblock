package pl.trollcraft.Skyblock.limiter;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class Limiter {

    private HashMap<EntityType, Integer> currentEntities;
    private HashMap<Material, Integer> currentBlocks;
    private int limiterLevel;

    public Limiter(HashMap<EntityType, Integer> currentEntities, HashMap<Material, Integer> currentBlocks, int limiterLevel){
        this.currentEntities = currentEntities;
        this.currentBlocks = currentBlocks;
        this.limiterLevel = limiterLevel;
    }

    public HashMap<EntityType, Integer> getCurrentEntities() {
        return currentEntities;
    }

    public void setCurrentEntities(HashMap<EntityType, Integer> currentEntities) {
        this.currentEntities = currentEntities;
    }

    public HashMap<Material, Integer> getCurrentBlocks() {
        return currentBlocks;
    }

    public void setCurrentBlocks(HashMap<Material, Integer> currentBlocks) {
        this.currentBlocks = currentBlocks;
    }

    public void addEntity(EntityType entityType){
        if(currentEntities.containsKey(entityType)){
            currentEntities.put(entityType, currentEntities.get(entityType) + 1);
        }
        else{
            currentEntities.put(entityType, 1);
        }
    }

    public void removeEntity(EntityType entityType){
        if(currentEntities.containsKey(entityType)){
            int amount = currentEntities.get(entityType);
            if(amount > 0){
                currentEntities.put(entityType, amount - 1);
            }
        }
    }

    public void addBlock(Material material){
        if(currentBlocks.containsKey(material)){
            currentBlocks.put(material, currentBlocks.get(material) + 1);
        }
        else{
            currentBlocks.put(material, 1);
        }
    }

    public void removeMaterial(Material material){
        if(currentBlocks.containsKey(material)){
            int amount = currentBlocks.get(material);
            if(amount > 0){
                currentBlocks.put(material, amount - 1);
            }
        }
    }

    public int getEntitiesAmount(EntityType entityType){
        if(currentEntities.containsKey(entityType)){
            return currentEntities.get(entityType);
        }

        return 0;
    }

    public int getBlocksAmount(Material material){
        if(currentBlocks.containsKey(material)){
            return currentBlocks.get(material);
        }

        return 0;
    }

    public int getLimiterLevel() {
        return limiterLevel;
    }

    public void setLimiterLevel(int limiterLevel) {
        this.limiterLevel = limiterLevel;
    }
}
