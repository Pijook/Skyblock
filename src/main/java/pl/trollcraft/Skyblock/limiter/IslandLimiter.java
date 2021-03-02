package pl.trollcraft.Skyblock.limiter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

import java.util.HashMap;
import java.util.UUID;

public class IslandLimiter {

    private HashMap<UUID, Limiter> islandsLimiters = new HashMap<>();
    private Limiter highestValues;

    public void loadDefault(){
        YamlConfiguration configuration = ConfigUtils.load("limiter.yml", Main.getInstance());

        HashMap<Material, Integer> blocks = new HashMap<>();
        HashMap<EntityType, Integer> entities = new HashMap<>();

        for(String key : configuration.getConfigurationSection("blocks").getKeys(false)){

            if(!Utils.isMaterial(key)){
                Debug.sendError("&cInvalid material in blocks." + key);
                continue;
            }

            int amount = configuration.getInt("blocks." + key);

            blocks.put(Material.valueOf(key), amount);
        }

        for(String key : configuration.getConfigurationSection("entities").getKeys(false)){

            if(Utils.isMob(key)){
                Debug.sendError("&cInvalid mob in entities." + key);
                continue;
            }

            int amount =  configuration.getInt("entities." + key);

            entities.put(EntityType.valueOf(key), amount);

        }

        highestValues = new Limiter(entities, blocks);
    }

    public void loadIsland(UUID uuid){

        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Main.getInstance());

        HashMap<Material, Integer> blocks = new HashMap<>();
        HashMap<EntityType, Integer> entities = new HashMap<>();

        String uuidString = uuid.toString();

        for(String key : configuration.getConfigurationSection(uuidString + ".blocks").getKeys(false)){
            blocks.put(Material.valueOf(key), configuration.getInt(uuidString + ".blocks." + key));
        }

        for(String key : configuration.getConfigurationSection(uuidString + ".entities").getKeys(false)){
            entities.put(EntityType.valueOf(key), configuration.getInt(uuidString + ".entities." + key));
        }

        islandsLimiters.put(uuid, new Limiter(entities, blocks));
    }

    public void saveIsland(UUID uuid){
        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Main.getInstance());

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

        UUID uuid = Main.getIslandsController().getIslandIDByLocation(entity.getLocation());

        Limiter limiter = islandsLimiters.get(uuid);

        int currentAmount = limiter.getEntitiesAmount(entity.getType());

        if(currentAmount + 1 > highestValues.getEntitiesAmount(entity.getType())){
            return true;
        }
        else{
            limiter.addEntity(entity.getType());
            return false;
        }
    }

    public boolean isBlockAboveLimit(Block block){

        UUID uuid = Main.getIslandsController().getIslandIDByLocation(block.getLocation());

        Limiter limiter = islandsLimiters.get(uuid);

        int currentAmount = limiter.getBlocksAmount(block.getType());

        if(currentAmount + 1 > highestValues.getBlocksAmount(block.getType())){
            return true;
        }
        else{
            limiter.addBlock(block.getType());
            return false;
        }

    }

    public boolean isEntityLimited(EntityType entityType){
        return highestValues.getCurrentEntities().containsKey(entityType);
    }

    public boolean isBlockLimited(Material material){
        return highestValues.getCurrentBlocks().containsKey(material);
    }

    public void removeBlock(Block block){
        if(highestValues.getCurrentBlocks().containsKey(block.getType())){
            Limiter limiter = islandsLimiters.get(Main.getIslandsController().getIslandIDByLocation(block.getLocation()));
            limiter.removeMaterial(block.getType());
        }
    }

    public void removeEntity(Entity entity){
        if(highestValues.getCurrentEntities().containsKey(entity.getType())){
            Limiter limiter = islandsLimiters.get(Main.getIslandsController().getIslandIDByLocation(entity.getLocation()));
            limiter.removeEntity(entity.getType());
        }
    }

    public void createNewLimiter(UUID uuid){
        HashMap<EntityType, Integer> entities = new HashMap<>();
        HashMap<Material, Integer> blocks = new HashMap<>();

        islandsLimiters.put(uuid, new Limiter(entities, blocks));
    }


}
