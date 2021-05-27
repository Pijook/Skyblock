package pl.trollcraft.Skyblock;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

public class Settings {

    public static boolean spawnOnJoin;

    /**
     * Loads basic settings for plugin from config.yml
     */
    public static void load(){

        YamlConfiguration configuration = ConfigUtils.load("config.yml", Skyblock.getInstance());

        spawnOnJoin = configuration.getBoolean("teleportToSpawn");
        Storage.spawn = ConfigUtils.getLocationFromConfig(configuration, "spawn");


        Storage.startSize = configuration.getInt("island.startSize");
        Storage.maxSize = configuration.getInt("island.maxSize");
        Storage.distance = configuration.getInt("island.distance");
        Storage.height = configuration.getDouble("island.height");
        Storage.world = configuration.getString("island.world");
        Storage.tutorialLocation = ConfigUtils.getLocationFromConfig(configuration, "tutorialLocation");

        Storage.topShow = configuration.getBoolean("showTop");
        if(Storage.topShow){
            Storage.topLocation = ConfigUtils.getLocationFromConfig(configuration, "topLocation");
        }

        Debug.log("DropEnable = " + Storage.dropEnable);
        Debug.log("Ustawiam na " + ConfigUtils.load("drop.yml", Skyblock.getInstance()).getBoolean("dropEnable"));
        Storage.dropEnable = ConfigUtils.load("drop.yml", Skyblock.getInstance()).getBoolean("dropEnable");

        Storage.createCooldown = configuration.getInt("createCooldown");


        for(World world : Bukkit.getWorlds()){
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }



    }
}
