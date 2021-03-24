package pl.trollcraft.Skyblock.limiter;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.cost.Cost;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class IslandLimiter {


    private HashMap<UUID, Limiter> islandsLimiters = new HashMap<>();

    private HashMap<Integer, Limiter> highestValues = new HashMap<>();

    private ArrayList<Material> limitedBlocks = new ArrayList<>();
    private ArrayList<EntityType> limitedEntities = new ArrayList<>();

    private IslandsController islandsController = Skyblock.getIslandsController();

    private HashMap<Integer, Cost> upgradesCosts = new HashMap<>();

    /**
     * Loads default settings from config limiter.yml
     * Things like highest amount of mobs or blocks for specified limiter level
     */
    public void loadSettings(){

        YamlConfiguration configuration = ConfigUtils.load("limiter.yml", Skyblock.getInstance());

        for(String limiterLevel : configuration.getConfigurationSection("limiter").getKeys(false)){

            upgradesCosts.put(Integer.parseInt(limiterLevel),
                    new Cost(
                    configuration.getDouble("limiter." + limiterLevel + ".cost.level"),
                    configuration.getDouble("limiter." + limiterLevel + ".cost.money")
            ));

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

    /**
     * Loads island limiter from islandLimits.yml
     * @param islandID ID of island to load
     */
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

    /**
     * Saves limiter for specified island and removes it from memory
     * @param islandID ID of island to save
     */
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

    /**
     * @param islandID ID of island of limiter
     * @return Returns limiter or null if limiter doesn't exist
     */
    public Limiter getLimiter(UUID islandID){
        if(islandsLimiters.containsKey(islandID)){
            return islandsLimiters.get(islandID);
        }
        return null;
    }

    /**
     * Checks does block is above specified limit
     * @param islandID ID of island
     * @param material Material to check
     * @return Returns true if block is above limit
     */
    public boolean isBlockAboveLimit(UUID islandID, Material material){

        Limiter limiter = islandsLimiters.get(islandID);

        if(!limiter.getBlocks().containsKey(material)){
            return false;
        }

        return limiter.getBlocksAmount(material) + 1 > highestValues.get(limiter.getLimiterLevel()).getBlocksAmount(material);
    }

    /**
     * Checks does entity is above specified limit
     * @param islandID ID of island
     * @param entityType Entity to check
     * @return Returns true if entity is above limit
     */
    public boolean isEntityAboveLimit(UUID islandID, EntityType entityType){

        Limiter limiter = islandsLimiters.get(islandID);

        if(!limiter.getEntities().containsKey(entityType)){
            return false;
        }

        return limiter.getEntitiesAmount(entityType) + 1 > highestValues.get(limiter.getLimiterLevel()).getEntitiesAmount(entityType);
    }

    /**
     * Checks does entity is limited
     * @param entityType EntityType to check
     * @return Returns true if entity is limited
     */
    public boolean isEntityLimited(EntityType entityType){
        return limitedEntities.contains(entityType);
    }

    /**
     * Checks does material is limited
     * @param material Material to check
     * @return Returns true if material is limited
     */
    public boolean isBlockLimited(Material material){
        return limitedBlocks.contains(material);
    }

    /**
     * Adds block to island limiter
     * @param islandID ID of island
     * @param material Material to add
     */
    public void addBlock(UUID islandID, Material material){
        islandsLimiters.get(islandID).increaseBlocks(material, 1);
    }

    /**
     * Removes block from island limiter
     * @param islandID ID of island
     * @param material Material to remove
     */
    public void removeBlock(UUID islandID, Material material){
        islandsLimiters.get(islandID).decreaseBlocks(material, 1);
    }

    /**
     * Adds entity to island limiter
     * @param islandID ID of island
     * @param entityType EntityType to add
     */
    public void addEntity(UUID islandID, EntityType entityType){
        islandsLimiters.get(islandID).increaseEntities(entityType, 1);
    }

    /**
     * Removes entity from island limiter
     * @param islandID ID of island
     * @param entityType EntityType to remove
     */
    public void removeEntity(UUID islandID, EntityType entityType){
        islandsLimiters.get(islandID).decreaseEntities(entityType, 1);
    }

    /**
     * Creates new empty limiter for new island
     * @param islandID ID of island
     */
    public void createNewLimiter(UUID islandID){
        islandsLimiters.put(islandID, new Limiter(1, new HashMap<>(), new HashMap<>()));
    }

    public boolean canUpgrade(Player player, UUID islandID){

        Worker worker = Skyblock.getWorkerController().getWorkerByName(player.getName());
        Limiter limiter = islandsLimiters.get(islandID);

        if(!upgradesCosts.containsKey(limiter.getLimiterLevel() + 1)){
            return false;
        }

        if(worker.getAverageLevel() >= upgradesCosts.get(limiter.getLimiterLevel() + 1).getPlayerLevel()){
            return true;
        }
        else{
            return false;
        }
    }

    public void upgradeLimiter(UUID islandID){
        islandsLimiters.get(islandID).setLimiterLevel(islandsLimiters.get(islandID).getLimiterLevel() + 1);
    }


}
