package pl.trollcraft.Skyblock;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Storage {

    public static String channel;
    public static String serverName;
    public static String redisCode = "skyblock:%player%";
    public static String redisWorkerCode = "skyblockworker:%player%";
    public static String islandCode = "skyblockisland:%id%";

    //Top
    public static boolean topShow;
    public static String topCode = "skyblock:top";
    public static Hologram topHologram = null;
    public static Location topLocation;

    public static Location spawn;

    public static Location tutorialLocation;

    public static LinkedHashMap<String, Integer> islandsTop;

    public static int startSize = 70;
    public static int maxSize = 70;
    public static int distance = 70;
    public static double height = 70;
    public static String world = "Islands";

    public static boolean dropEnable = true;

    public static boolean kitsEnabled = false;


}
