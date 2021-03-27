package pl.trollcraft.Skyblock.dropManager;

import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DropManager {

    private ArrayList<Material> toCheck = new ArrayList<>();
    private HashMap<Integer, ArrayList<String>> drops = new HashMap<>();
    private int range;
    private Random random;

    public void setupGenerator(){
        YamlConfiguration configuration = ConfigUtils.load("drops.yml", Skyblock.getInstance());

        range = configuration.getInt("range");

        for(String key : configuration.getStringList("toCheck")){
            toCheck.add(Material.valueOf(key));
        }

        for(String playerLevel : configuration.getConfigurationSection("drops").getKeys(false)){

            ArrayList<String> materials = new ArrayList<>();

            for(String materialName : configuration.getConfigurationSection("drops." + playerLevel).getKeys(false)){

                for(int i = 0; i < configuration.getInt("drops." + playerLevel + "." + materialName); i++){
                    materials.add(materialName);
                }

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
}
