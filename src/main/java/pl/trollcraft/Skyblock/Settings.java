package pl.trollcraft.Skyblock;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class Settings {

    public static boolean spawnOnJoin;

    public static void load(){

        YamlConfiguration configuration = ConfigUtils.load("config.yml", Main.getInstance());

        spawnOnJoin = configuration.getBoolean("teleportToSpawn");
        Storage.spawn = ConfigUtils.getLocationFromConfig(configuration, "spawn");

    }
}
