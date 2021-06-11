package pl.trollcraft.Skyblock.dropManager;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.cost.Cost;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.worker.Worker;
import scala.Int;

import java.util.*;

public class DropManager {
    //Material
    private ArrayList<Material> toCheck = new ArrayList<>();
    private HashMap<Integer, LinkedHashMap<Material, Double>> drops = new LinkedHashMap<>();
    private int range;
    private Random random;
    //Amount
    private HashMap<Integer, Integer> fortune = new HashMap<>();
    private int amountRange;
    private int percentRange;
    //Cost
    private HashMap<Integer, Cost> upgradesCost = new HashMap<>();
    //MaxLevel
    private int maxLevel;

    public void setupGenerator(){
        toCheck.clear();
        drops.clear();
        fortune.clear();
        upgradesCost.clear();

        YamlConfiguration configuration = ConfigUtils.load("drop.yml", Skyblock.getInstance());

        //range = configuration.getInt("range");

        for(String key : configuration.getStringList("toCheck")){
            toCheck.add(Material.valueOf(key));
        }

        amountRange = configuration.getInt("fortune.amountRange");
        percentRange = configuration.getInt("fortune.percentRange");
        for( String sFortuneLevel : configuration.getConfigurationSection("fortune.level").getKeys(false)){
            fortune.put(Integer.parseInt(sFortuneLevel), configuration.getInt("fortune.level." + sFortuneLevel));
        }

        for(String playerLevel : configuration.getConfigurationSection("drops").getKeys(false)){

            LinkedHashMap<Material, Double> chances = new LinkedHashMap<>();

            for(String materialName : configuration.getConfigurationSection("drops." + playerLevel).getKeys(false)){
                chances.put(
                        Material.valueOf(materialName),
                        configuration.getDouble("drops." + playerLevel + "." + materialName)
                );

            }
            drops.put(Integer.valueOf(playerLevel), chances);
            maxLevel = Integer.parseInt(playerLevel);
        }

        random = new Random();
    }

    public void loadUpgrades(){
        YamlConfiguration configuration = ConfigUtils.load("drop.yml", Skyblock.getInstance());

        for(String upgradeLevel : configuration.getConfigurationSection("upgrades").getKeys(false)){

            double playerLevel = configuration.getDouble("upgrades." + upgradeLevel + ".level");
            double money = configuration.getDouble("upgrades." + upgradeLevel + ".money");
            upgradesCost.put(Integer.parseInt(upgradeLevel), new Cost(playerLevel, money));
        }
    }

    public Material generateMaterial(int playerLevel){
        if( !drops.containsKey(playerLevel)){
            if(playerLevel > maxLevel){
                playerLevel = maxLevel;
            }
            else{
                Debug.log("&cNie posiadam szans dla poziomu " + playerLevel + " gornika");
                return Material.COBBLESTONE;
            }
        }
        LinkedHashMap<Material, Double> chances = drops.get(playerLevel);
        double a = random.nextDouble() * 100;
        for(Material material : chances.keySet()){
            if(a <= chances.get(material)){
                return material;
            }
        }
        return Material.COBBLESTONE;
    }

    public boolean countsAsDrop(Material material){
        return toCheck.contains(material);
    }

    public int amountByFortune(int fortuneLevel){
        int amount = random.nextInt(amountRange)+1;
        if( random.nextInt(percentRange)+1 <= fortune.get(fortuneLevel) ){
            return amount;
        }
        return 1;
    }

    public boolean canUpgrade(Player player){
        SkyblockPlayer skyblockPlayer = Skyblock.getSkyblockPlayerController().getPlayer(player.getName());

        if(!upgradesCost.containsKey(skyblockPlayer.getDropLevel() + 1)){
            return false;
        }

        Worker worker = Skyblock.getWorkerController().getWorkerByName(player.getName());

        Cost upgradeCost = upgradesCost.get(skyblockPlayer.getDropLevel() + 1);

        if(worker.getAverageLevel() < upgradeCost.getPlayerLevel()){
            return false;
        }

        //TODO Add money cost

        return true;
    }

    public void upgradeDrop(Player player){
        SkyblockPlayer skyblockPlayer = Skyblock.getSkyblockPlayerController().getPlayer(player.getName());

        skyblockPlayer.setDropLevel(skyblockPlayer.getDropLevel() + 1);

        //TODO Remove money from account
    }

}
