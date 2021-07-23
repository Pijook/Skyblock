package pl.trollcraft.Skyblock;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.listeners.IslandSecurityListener;

import java.util.ArrayList;
import java.util.List;

public class Settings {

    public static boolean spawnOnJoin;

    /**
     * Loads basic settings for plugin from config.yml
     */
    public static void load(){

        YamlConfiguration configuration = ConfigUtils.load("config.yml", Skyblock.getInstance());
        YamlConfiguration network = ConfigUtils.loadFromNetworkGlobalFolder("networkIslandConfig.yml");

        spawnOnJoin = configuration.getBoolean("teleportToSpawn");
        Storage.spawn = ConfigUtils.getLocationFromConfig(configuration, "spawn");


        Storage.startSize = configuration.getInt("island.startSize");
        Storage.maxSize = configuration.getInt("island.maxSize");
        Storage.distance = configuration.getInt("island.distance");
        Storage.height = configuration.getDouble("island.height");
        Storage.world = configuration.getString("island.world");
        Storage.tutorialLocation = ConfigUtils.getLocationFromConfig(configuration, "tutorialLocation");

        Storage.playersPerIsland = configuration.getInt("playersPerIsland");

        Storage.topShow = configuration.getBoolean("showTop");
        if(Storage.topShow){
            Storage.topLocation = ConfigUtils.getLocationFromConfig(configuration, "topLocation");
        }

        Debug.log("DropEnable = " + Storage.dropEnable);
        Debug.log("Ustawiam na " + ConfigUtils.load("drop.yml", Skyblock.getInstance()).getBoolean("dropEnable"));
        Storage.dropEnable = ConfigUtils.load("drop.yml", Skyblock.getInstance()).getBoolean("dropEnable");

        assert network != null;
        Storage.createCooldown = network.getInt("createCooldown");
        if( configuration.contains("isSpawn")) {
            Storage.isSpawn = configuration.getBoolean("isSpawn");
        }
        else{
            Storage.isSpawn = false;
        }


        for(World world : Bukkit.getWorlds()){
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }

        loadSecuredBlocks();
        loadFood();
        loadSigns();

    }

    private static void loadSecuredBlocks(){


    }

    private static void loadSigns(){
        ArrayList<Material> list = new ArrayList<>();
        list.add(Material.ACACIA_WALL_SIGN);
        list.add(Material.BIRCH_WALL_SIGN);
        list.add(Material.CRIMSON_WALL_SIGN);
        list.add(Material.SPRUCE_WALL_SIGN);
        list.add(Material.BIRCH_WALL_SIGN);
        list.add(Material.DARK_OAK_WALL_SIGN);
        list.add(Material.JUNGLE_WALL_SIGN);
        list.add(Material.OAK_WALL_SIGN);
        list.add(Material.WARPED_WALL_SIGN);
        IslandSecurityListener.signs = list;
    }

    private static void loadFood(){
        ArrayList<Material> food = new ArrayList<Material>();
        food.add(Material.APPLE);
        food.add(Material.MUSHROOM_STEW);
        food.add(Material.BREAD);
        food.add(Material.COOKED_PORKCHOP);
        food.add(Material.GOLDEN_APPLE);
        food.add(Material.COD);
        food.add(Material.SALMON);
        food.add(Material.TROPICAL_FISH);
        food.add(Material.PUFFERFISH);
        food.add(Material.COOKED_COD);
        food.add(Material.COOKED_SALMON);
        food.add(Material.COOKIE);
        food.add(Material.MELON_SLICE);
        food.add(Material.DRIED_KELP);
        food.add(Material.BEEF);
        food.add(Material.COOKED_BEEF);
        food.add(Material.CHICKEN);
        food.add(Material.COOKED_CHICKEN);
        food.add(Material.ROTTEN_FLESH);
        food.add(Material.SPIDER_EYE);
        food.add(Material.CARROT);
        food.add(Material.POTATO);
        food.add(Material.BAKED_POTATO);
        food.add(Material.PUMPKIN_PIE);
        food.add(Material.RABBIT);
        food.add(Material.COOKED_RABBIT);
        food.add(Material.RABBIT_STEW);
        food.add(Material.MUTTON);
        food.add(Material.COOKED_MUTTON);
        food.add(Material.BEETROOT);
        food.add(Material.BEETROOT_SOUP);
        IslandSecurityListener.food = food;
    }
}
