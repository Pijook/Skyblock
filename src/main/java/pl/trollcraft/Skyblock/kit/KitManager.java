package pl.trollcraft.Skyblock.kit;

import org.bouncycastle.jcajce.provider.digest.Skein;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class KitManager {

    private HashMap<String, KitSet> availableKits = new HashMap<>();

    public void loadKits(){
        YamlConfiguration configuration = ConfigUtils.load("kits.yml", Skyblock.getInstance());

        for(String kitName : configuration.getConfigurationSection("kits").getKeys(false)){

            HashMap<Integer, Kit> kitSet = new HashMap<>();

            for(String kitLevel : configuration.getConfigurationSection("kits." + kitName).getKeys(false)){

                ArrayList<ItemStack> items = new ArrayList<>();

                for(String itemName : configuration.getConfigurationSection("kits." + kitName + "." + kitLevel + ".items").getKeys(false)){

                    items.add(ConfigUtils.getItemstack(configuration,"kits." + kitName + "." + kitLevel + ".items." + itemName));

                }

                kitSet.put(Integer.parseInt(kitLevel), new Kit(items));
            }

            availableKits.put(kitName, new KitSet(kitSet, configuration.getLong("kits.")));

        }
    }


}
