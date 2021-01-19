package pl.trollcraft.Skyblock.generator;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class CreateIsland {

    private static int id;
    private static String world;
    private static double x;
    private static double y;
    private static double z;
    private static int distance;
    private static int maxSize;
    private static int goNext;
    private static int last;
    private static int check;
    private static boolean isAv;

    private static void getNextIsland(){
        YamlConfiguration configuration = ConfigUtils.load("nextisland.yml", Main.getInstance());

        id = configuration.getInt("nextIsland.id");

        world = configuration.getString("nextIsland.world");
        x = configuration.getDouble("nextIsland.x");
        y = configuration.getDouble("nextIsland.y");
        z = configuration.getDouble("nextIsland.z");

    }






}
