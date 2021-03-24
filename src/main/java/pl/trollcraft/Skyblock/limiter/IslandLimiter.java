package pl.trollcraft.Skyblock.limiter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;

import javax.activation.MailcapCommandMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class IslandLimiter {


    private HashMap<UUID, Limiter> islandsLimiters = new HashMap<>();

    private HashMap<Integer, Limiter> highestValues = new HashMap<>();

    private ArrayList<Material> limitedBlocks = new ArrayList<>();
    private ArrayList<EntityType> limitedEntities = new ArrayList<>();

    private IslandsController islandsController = Skyblock.getIslandsController();


    public void loadSettings(){

        YamlConfiguration configuration = ConfigUtils.load("limiter.yml", Skyblock.getInstance());

        for(String limiterLevel : configuration.getConfigurationSection("limiter").getKeys(false)){

            HashMap<Material, Integer> blocks = new HashMap<>();
            HashMap<EntityType, Integer> entities = new HashMap<>();

            for(String blockType : configuration.getConfigurationSection("limiter." + limiterLevel + ".blocks").getKeys(false)){

                if(!Utils.isMaterial(blockType)){
                    Debug.sendError("&cWrong material name: limiter." + limiterLevel + ".blocks." + blockType);
                    continue;
                }

                if(!limitedBlocks.contains(Material.valueOf(blockType))){
                    limitedBlocks.add(Material.valueOf(blockType));
                }

                blocks.put(
                        Material.valueOf(blockType),
                        configuration.getInt("limiter." + limiterLevel + ".blocks." + blockType)
                );

            }

            for(String entityType : configuration.getConfigurationSection("limiter." + limiterLevel + ".entities").getKeys(false)){

                if(Utils.isMob(entityType)){
                    Debug.sendError("&cWrong material name: limiter." + limiterLevel + ".entities." + entityType);
                    continue;
                }

                if(!limitedEntities.contains(EntityType.valueOf(entityType))){
                    limitedEntities.add(EntityType.valueOf(entityType));
                }

                entities.put(
                        EntityType.valueOf(entityType),
                        configuration.getInt("limiter." + limiterLevel + ".blocks." + entityType)
                );

            }

            highestValues.put(Integer.parseInt(limiterLevel), new Limiter(0, blocks, entities));

        }
    }

    public void loadLimiter(UUID islandID){

        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Skyblock.getInstance());

        String ID = islandID.toString();

        if(!configuration.contains("islands." + ID)){
            islandsLimiters.put(islandID, new Limiter(1, new HashMap<>(), new HashMap<>()));
            return;
        }

        int limiterLevel = 1;
        HashMap<Material, Integer> blocks = new HashMap<>();
        HashMap<EntityType, Integer> entities = new HashMap<>();

        limiterLevel = configuration.getInt("islands." + ID + ".level");

        for(String blockType : configuration.getConfigurationSection("islands." + ID + ".blocks").getKeys(false)){

            blocks.put(
                    Material.valueOf(blockType),
                    configuration.getInt("islands." + ID + ".blocks." + blockType)
            );

        }

        for(String entityType : configuration.getConfigurationSection("islands." + ID + ".entities").getKeys(false)){

            entities.put(
                    EntityType.valueOf(entityType),
                    configuration.getInt("islands." + ID + ".entities." + entityType)
            );

        }

        islandsLimiters.put(islandID, new Limiter(limiterLevel, blocks, entities));

    }

    public void saveLimiter(UUID islandID){

        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Skyblock.getInstance());

        String ID = islandID.toString();
        Limiter limiter = islandsLimiters.get(islandID);

        configuration.set("islands." + ID, null);

        configuration.set("islands." + ID + ".level", limiter.getLimiterLevel());

        for(Material material : limiter.getBlocks().keySet()){
            configuration.set("islands." + ID + ".blocks." + material.getData().getName(), limiter.getBlocks().get(material));
        }

        for(EntityType entityType : limiter.getEntities().keySet()){
            configuration.set("islands." + ID + ".entities." + entityType.getEntityClass().getName(), limiter.getEntities().get(entityType));
        }

        islandsLimiters.remove(islandID);
        ConfigUtils.save(configuration, "islandLimits.yml");
    }

    public Limiter getLimiter(UUID islandID){
        if(islandsLimiters.containsKey(islandID)){
            return islandsLimiters.get(islandID);
        }
        return null;
    }

    public boolean isBlockAboveLimit(UUID islandID, Material material){

        Limiter limiter = islandsLimiters.get(islandID);

        if(!limiter.getBlocks().containsKey(material)){
            return false;
        }

        return limiter.getBlocksAmount(material) + 1 > highestValues.get(limiter.getLimiterLevel()).getBlocksAmount(material);
    }

    public boolean isEntityAboveLimit(UUID islandID, EntityType entityType){

        Limiter limiter = islandsLimiters.get(islandID);

        if(!limiter.getEntities().containsKey(entityType)){
            return false;
        }

        return limiter.getEntitiesAmount(entityType) + 1 > highestValues.get(limiter.getLimiterLevel()).getEntitiesAmount(entityType);
    }

    public boolean isEntityLimited(EntityType entityType){
        return limitedEntities.contains(entityType);
    }

    public boolean isBlockLimited(Material material){
        return limitedBlocks.contains(material);
    }

    public void addBlock(UUID islandID, Material material){
        islandsLimiters.get(islandID).increaseBlocks(material, 1);
    }

    public void removeBlock(UUID islandID, Material material){
        islandsLimiters.get(islandID).decreaseBlocks(material, 1);
    }

    public void addEntity(UUID islandID, EntityType entityType){
        islandsLimiters.get(islandID).increaseEntities(entityType, 1);
    }

    public void removeEntity(UUID islandID, EntityType entityType){
        islandsLimiters.get(islandID).decreaseEntities(entityType, 1);
    }


}
