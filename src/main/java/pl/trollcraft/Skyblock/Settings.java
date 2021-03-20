package pl.trollcraft.Skyblock;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

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
        Storage.maxSize = configuration.getInt("island.maxSize");;
        Storage.distance = configuration.getInt("island.distance");;
        Storage.height = configuration.getDouble("island.height");;
        Storage.world = configuration.getString("island.world");;


    }
}
