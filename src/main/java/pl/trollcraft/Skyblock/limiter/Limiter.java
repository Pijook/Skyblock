package pl.trollcraft.Skyblock.limiter;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class Limiter {

    private int limiterLevel;
    private HashMap<Material, Integer> blocks;
    private HashMap<EntityType, Integer> entities;

    public Limiter(int limiterLevel, HashMap<Material, Integer> blocks, HashMap<EntityType, Integer> entities){
        this.limiterLevel = limiterLevel;
        this.blocks = blocks;
        this.entities = entities;
    }

    public int getBlocksAmount(Material material){
        if(blocks.containsKey(material)){
            return blocks.get(material);
        }
        return -1;
    }

    public int getEntitiesAmount(EntityType entityType){
        if(entities.containsKey(entityType)){
            return entities.get(entityType);
        }
        return -1;
    }

    public void increaseBlocks(Material material, int amount){
        if(!blocks.containsKey(material)){
            blocks.put(material, amount);
        }
        else{
            blocks.put(material, blocks.get(material) + amount);
        }

    }

    public void increaseEntities(EntityType entityType, int amount){
        if(!entities.containsKey(entityType)){
            entities.put(entityType, amount);
        }
        else{
            entities.put(entityType, entities.get(entityType) + amount);
        }
    }

    public void decreaseBlocks(Material material, int amount){
        if(!blocks.containsKey(material)){
            return;
        }
        blocks.put(material, blocks.get(material) -  amount);
    }

    public void decreaseEntities(EntityType entityType, int amount){
        if(!entities.containsKey(entityType)){
            return;
        }
        entities.put(entityType, entities.get(entityType) - amount);
    }

    public int getLimiterLevel() {
        return limiterLevel;
    }

    public void setLimiterLevel(int limiterLevel) {
        this.limiterLevel = limiterLevel;
    }

    public HashMap<Material, Integer> getBlocks() {
        return blocks;
    }

    public void setBlocks(HashMap<Material, Integer> blocks) {
        this.blocks = blocks;
    }

    public HashMap<EntityType, Integer> getEntities() {
        return entities;
    }

    public void setEntities(HashMap<EntityType, Integer> entities) {
        this.entities = entities;
    }
}
