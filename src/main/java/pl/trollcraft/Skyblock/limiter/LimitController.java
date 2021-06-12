package pl.trollcraft.Skyblock.limiter;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.cost.Cost;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LimitController {

    private HashMap<UUID, IslandLimiter> loadedLimiters = new HashMap<>();
    private HashMap<String, HashMap<Integer, Limiter>> defaultLimits = new HashMap<>();
    private HashMap<String, HashMap<Integer, Cost>> upgradesCosts = new HashMap<>();
    private ArrayList<String> crops = new ArrayList<>();
    private ArrayList<String> destroyableByWater = new ArrayList<>();

    public void loadSettings(){
        YamlConfiguration configuration = ConfigUtils.load("limiter.yml", Skyblock.getInstance());

        if(configuration.contains("crops")){
            crops = (ArrayList<String>) configuration.getStringList("crops");
        }

        if(configuration.contains("destroyableByWater")){
            destroyableByWater = (ArrayList<String>) configuration.getStringList("destroyableByWater");
        }

        for(String type : configuration.getConfigurationSection("limiter").getKeys(false)){

            HashMap<Integer, Limiter> tempLimit = new HashMap<>();
            Debug.log("Loading limit for " + type);
            if(!Utils.isMaterial(type) && Utils.isMob(type)){
                Debug.sendError("Wrong material name: limiter." + type);
            }
            for(String level : configuration.getConfigurationSection("limiter." + type).getKeys(false)){
                int amount = configuration.getInt("limiter." + type + "." + level + ".amount");

                tempLimit.put(Integer.parseInt(level), new Limiter(0, amount));
            }

            defaultLimits.put(type, tempLimit);

        }

        for(String type : configuration.getConfigurationSection("upgrades").getKeys(false)){

            HashMap<Integer, Cost> tempValue = new HashMap<>();

            for(String upgradeLevel : configuration.getConfigurationSection("upgrades." + type).getKeys(false)){

                double playerLevel = configuration.getDouble("upgrades." + type + "." + upgradeLevel + ".level");
                double money = configuration.getDouble("upgrades." + type + "." + upgradeLevel + ".money");

                tempValue.put(Integer.parseInt(upgradeLevel), new Cost(playerLevel, money));

            }

            upgradesCosts.put(type, tempValue);

        }
    }

    public void loadLimiter(UUID islandID){
        Debug.log("&a&lLoading limiter " + islandID.toString());
        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Skyblock.getInstance());
        String ID = islandID.toString();

        HashMap<String, Limiter> islandLimiter = new HashMap<>();

        if(!configuration.contains("islands." + ID)){
            //loadedLimiters.put(islandID, new IslandLimiter(islandLimiter));
            createNewLimiter(islandID);
            return;
        }

        for(String type : configuration.getConfigurationSection("islands." + ID).getKeys(false)){
            islandLimiter.put(type, new Limiter(configuration.getInt("islands." + ID + "." + type + ".level"), configuration.getInt("islands." + ID + "." + type + ".amount")));
        }

        IslandLimiter finalIslandLimiter = new IslandLimiter(islandLimiter);

        finalIslandLimiter = verifyLimiter(finalIslandLimiter);
        finalIslandLimiter.debug();
        loadedLimiters.put(islandID, finalIslandLimiter);
    }

    public void saveLimiter(UUID islandID){
        YamlConfiguration configuration = ConfigUtils.load("islandLimits.yml", Skyblock.getInstance());
        String ID = islandID.toString();
        Debug.log("&aSaving limiter " + islandID.toString());
        IslandLimiter islandLimiter = loadedLimiters.get(islandID);

        for(String type : islandLimiter.getIslandLimiters().keySet()){

            Limiter limiter = islandLimiter.getLimiter(type);

            configuration.set("islands." + ID + "." + type + ".level", limiter.getLevel());
            configuration.set("islands." + ID + "." + type + ".amount", limiter.getCurrentAmount());
        }

        loadedLimiters.remove(islandID);
        ConfigUtils.save(configuration, "islandLimits.yml");
    }

    public void saveAllLimiters(){

        for(UUID limiterID : loadedLimiters.keySet()){
            saveLimiter(limiterID);
        }

    }


    public boolean isAboveLimit(String type, UUID islandID){
        IslandLimiter islandLimiter = loadedLimiters.get(islandID);

        return islandLimiter.getLimiter(type).getCurrentAmount() + 1 > defaultLimits.get(type).get(islandLimiter.getLimiter(type).getLevel()).getCurrentAmount();
    }

    public boolean isTypeLimited(String type){
        return defaultLimits.containsKey(type);
    }

    public void decreaseType(String type, UUID islandID){
        if (loadedLimiters.containsKey(islandID)) {
            loadedLimiters.get(islandID).decreaseAmount(type, 1);
        }
    }

    public void increaseType(String type, UUID islandID){
        if(loadedLimiters.containsKey(islandID)){
            loadedLimiters.get(islandID).increaseAmount(type, 1);
        }
    }

    public void increaeLevel(String type, UUID islandID){
        if(loadedLimiters.containsKey(islandID)){
            loadedLimiters.get(islandID).increaseLevel(type, 1);
        }
    }

    public void createNewLimiter(UUID islandID){
        HashMap<String, Limiter> limiters = new HashMap<>();
        IslandLimiter islandLimiter = new IslandLimiter(limiters);

        for(String type : defaultLimits.keySet()){
            limiters.put(type, new Limiter(1, 0));
        }

        loadedLimiters.put(islandID, islandLimiter);
    }

    public IslandLimiter getLimiter(UUID islandID){
        if(loadedLimiters.containsKey(islandID)){
            return loadedLimiters.get(islandID);
        }
        return null;
    }

    public Limiter getDefaultValues(String type, int level){
        return defaultLimits.get(type).get(level);
    }

    public boolean canUpgrade(UUID islandID, String type, Player player){
        Limiter limiter = loadedLimiters.get(islandID).getLimiter(type);

        if(!upgradesCosts.containsKey(type)){
            return false;
        }

        if(!upgradesCosts.get(type).containsKey(limiter.getLevel() + 1)){
            return false;
        }

        Cost upgradeCost = upgradesCosts.get(type).get(limiter.getLevel() + 1);

        Worker worker = Skyblock.getWorkerController().getWorkerByName(player.getName());

        if(worker.getAverageLevel() < upgradeCost.getPlayerLevel()){
            return false;
        }

        if(upgradeCost.getMoney() > Skyblock.getEconomy().getBalance(player)){
            return false;
        }

        return true;

    }

    public void upgradeLimiter(UUID islandID, String type, Player player){

        //increaseType(type, islandID);
        increaeLevel(type, islandID);
        double amount = upgradesCosts.get(type).get(loadedLimiters.get(islandID).getLimiter(type).getLevel()).getMoney();
        Skyblock.getEconomy().withdrawPlayer(player, amount);
    }

    public Cost getLimiterCost(int level, String type){
        if(!upgradesCosts.containsKey(type)){
            Debug.log("[Cost] Cannot find type");
            return null;
        }
        if(upgradesCosts.get(type).containsKey(level)){
            return upgradesCosts.get(type).get(level);
        }
        Debug.log("[Cost] Cannot find level");
        return null;
    }

    public IslandLimiter verifyLimiter(IslandLimiter islandLimiter){
        ArrayList<String> toRemove = new ArrayList<>();
        for(String type : islandLimiter.getIslandLimiters().keySet()){
            if(!defaultLimits.containsKey(type)){
                toRemove.add(type);
            }
        }
        for(String type : toRemove){
            islandLimiter.getIslandLimiters().remove(type);
        }

        for(String defaultType : defaultLimits.keySet()){
            if(!islandLimiter.getIslandLimiters().containsKey(defaultType)){
                islandLimiter.getIslandLimiters().put(defaultType, new Limiter(1, 0));
            }
        }

        return islandLimiter;
    }

    public void removeLimiter(UUID islandID){
        if(loadedLimiters.containsKey(islandID)){
            loadedLimiters.remove(islandID);
        }
    }

    public boolean isCrop(String name){
        return crops.contains(name);
    }

    public boolean isDestroyableByWater(String name){
        return destroyableByWater.contains(name);
    }


}
