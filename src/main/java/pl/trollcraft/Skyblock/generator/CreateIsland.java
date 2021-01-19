package pl.trollcraft.Skyblock.generator;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class CreateIsland {

    int id;
    double x;
    double y;
    double z;
    int distance;
    int maxSize;
    int goNext;

    private static void getNextIsland(){
        YamlConfiguration configuration = ConfigUtils.load("nextisland.yml", Main.getInstance());



    }






}
