package pl.trollcraft.Skyblock;

import org.bukkit.Location;

public class Storage {

    public static String channel;
    public static String serverName;
    public static String redisCode = "skyblock:%player%";
    public static String redisWorkerCode = "skyblockworker:%player%";
    public static String islandCode = "skyblockisland:%id%";

    public static Location spawn;

    public static Location tutorialLocation;

    public static int startSize = 70;
    public static int maxSize = 70;
    public static int distance = 70;
    public static double height = 70;
    public static String world = "Islands";

    public static boolean dropEnable = true;


}
