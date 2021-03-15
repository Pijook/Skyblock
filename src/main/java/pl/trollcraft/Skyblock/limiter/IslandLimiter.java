package pl.trollcraft.Skyblock.limiter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

import java.util.HashMap;
import java.util.UUID;

public class IslandLimiter {

    private HashMap<UUID, Limiter> islandsLimiters = new HashMap<>();
    private HashMap<Integer, Limiter> highestValues = new HashMap<>();

    public void loadDefault(){
        YamlConfiguration configuration = ConfigUtils.load("limiter.yml", Skyblock.getInstance());

        HashMap<Material, Integer> blocks = new HashMap<>();
        HashMap<EntityType, Integer> entities = new HashMap<>();

        for(String limiterLevel: configuration.getConfigurationSection("limiter").getKeys(false)){
            for(String key : configuration.getConfigurationSection("limiter." + limiterLevel + ".blocks").getKeys(false)){

                if(!Utils.isMaterial(key)){
                    Debug.sendError("&cInvalid material in blocks." + key);
                    continue;
                }

                int amount = configuration.getInt("blocks." + key);

                blocks.put(Material.valueOf(key), amount);
            }

            for(String key : configuration.getConfigurationSection("limiter." + limiterLevel + ".entities").getKeys(false)){

                if(Utils.isMob(key)){
                    Debug.sendError("&cInvalid mob in entities." + key);
                    continue;
                }

                int amount =  configuration.getInt("entities." + key);

                entities.put(EntityType.valueOf(key), amount);

            }

            highestValues.put(Integer.parseInt(limiterLevel), new Limiter(entities, blocks, 1));
        }

    }

    public void loadIsland(UUID uuid){

        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Skyblock.getInstance());

        HashMap<Material, Integer> blocks = new HashMap<>();
        HashMap<EntityType, Integer> entities = new HashMap<>();

        int limiterLevel = 1;

        String uuidString = uuid.toString();
        if(configuration.contains(uuidString)){
            limiterLevel = configuration.getInt(uuidString + ".limiterLevel");
            for(String key : configuration.getConfigurationSection(uuidString + ".blocks").getKeys(false)){
                blocks.put(Material.valueOf(key), configuration.getInt(uuidString + ".blocks." + key));
            }

            for(String key : configuration.getConfigurationSection(uuidString + ".entities").getKeys(false)){
                entities.put(EntityType.valueOf(key), configuration.getInt(uuidString + ".entities." + key));
            }
        }


        islandsLimiters.put(uuid, new Limiter(entities, blocks, limiterLevel));
    }

    public void saveIsland(UUID uuid){
        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Skyblock.getInstance());

        String uuidString = uuid.toString();

        Limiter limiter = islandsLimiters.get(uuid);

        configuration.set(uuidString, null);
        if(limiter.getCurrentBlocks() != null){
            for(Material material : limiter.getCurrentBlocks().keySet()){
                configuration.set(uuidString + "." + material.getData().getName(), limiter.getBlocksAmount(material));
            }
        }

        if(limiter.getCurrentEntities() != null){
            for(EntityType entityType : limiter.getCurrentEntities().keySet()){
                configuration.set(uuidString + "." + entityType.getName(), limiter.getEntitiesAmount(entityType));
            }
        }


        ConfigUtils.save(configuration, "islandLimits.yml");
    }

    public boolean isEntityAboveLimit(Entity entity){

        UUID uuid = Skyblock.getIslandsController().getIslandIDByLocation(entity.getLocation());

        Limiter limiter = islandsLimiters.get(uuid);

        int currentAmount = limiter.getEntitiesAmount(entity.getType());

        if(currentAmount + 1 > highestValues.get(limiter.getLimiterLevel()).getEntitiesAmount(entity.getType())){
            return true;
        }
        else{
            limiter.addEntity(entity.getType());
            return false;
        }
    }

    public boolean isBlockAboveLimit(Block block){

        UUID uuid = Skyblock.getIslandsController().getIslandIDByLocation(block.getLocation());

        Limiter limiter = islandsLimiters.get(uuid);

        int currentAmount = limiter.getBlocksAmount(block.getType());

        if(currentAmount + 1 > highestValues.get(limiter.getLimiterLevel()).getBlocksAmount(block.getType())){
            return true;
        }
        else{
            limiter.addBlock(block.getType());
            return false;
        }

    }

    public boolean isEntityLimited(EntityType entityType){
        return highestValues.get(1).getCurrentEntities().containsKey(entityType);
    }

    public boolean isBlockLimited(Material material){
        return highestValues.get(1).getCurrentBlocks().containsKey(material);
    }

    public void removeBlock(Block block){
        if(isBlockLimited(block.getType())){
            Limiter limiter = islandsLimiters.get(Skyblock.getIslandsController().getIslandIDByLocation(block.getLocation()));
            limiter.removeMaterial(block.getType());
        }
    }

    public void removeEntity(Entity entity){
        if(isEntityAboveLimit(entity)){
            Limiter limiter = islandsLimiters.get(Skyblock.getIslandsController().getIslandIDByLocation(entity.getLocation()));
            limiter.removeEntity(entity.getType());
        }
    }

    public void createNewLimiter(UUID uuid){
        HashMap<EntityType, Integer> entities = new HashMap<>();
        HashMap<Material, Integer> blocks = new HashMap<>();

        islandsLimiters.put(uuid, new Limiter(entities, blocks, 1));
    }


}
