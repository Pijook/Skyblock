package pl.trollcraft.Skyblock.dropManager;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DropManager {
    //Material
    private ArrayList<Material> toCheck = new ArrayList<>();
    private HashMap<Integer, ArrayList<String>> drops = new HashMap<>();
    private int range;
    private Random random;
    //Amount
    private HashMap<Integer, Integer> fortune = new HashMap<>();
    private int amountRange;
    private int percentRange;

    public void setupGenerator(){
        YamlConfiguration configuration = ConfigUtils.load("drop.yml", Skyblock.getInstance());

        Storage.dropEnable = configuration.getBoolean("range");

        range = configuration.getInt("range");

        for(String key : configuration.getStringList("toCheck")){
            toCheck.add(Material.valueOf(key));
        }

        amountRange = configuration.getInt("fortune.amountRange");
        percentRange = configuration.getInt("fortune.percentRange");
        for( String sFortuneLevel : configuration.getConfigurationSection("fortune.level").getKeys(false)){
            fortune.put(Integer.parseInt(sFortuneLevel), configuration.getInt("fortune.level." + sFortuneLevel));
        }

        for(String playerLevel : configuration.getConfigurationSection("drops").getKeys(false)){

            ArrayList<String> materials = new ArrayList<>();

            int cobble = range;

            for(String materialName : configuration.getConfigurationSection("drops." + playerLevel).getKeys(false)){

                for(int i = 0; i < configuration.getInt("drops." + playerLevel + "." + materialName); i++){
                    materials.add(materialName);
                    cobble--;
                }
            }
            for( int i = 0 ; i < cobble ; i++ ){
                materials.add(Material.COBBLESTONE.toString());
            }

            drops.put(Integer.parseInt(playerLevel), materials);

        }

        random = new Random();
    }

    public Material generateMaterial(int playerLevel){
        return Material.valueOf(drops.get(playerLevel).get(random.nextInt(range)));
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

}
