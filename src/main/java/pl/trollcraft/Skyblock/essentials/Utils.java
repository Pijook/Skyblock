package pl.trollcraft.Skyblock.essentials;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import pl.trollcraft.Skyblock.Main;

import java.util.Random;

public class Utils {

    /**
     * Checks if given arg is Integer
     * @param a String to check
     * @return Returns false if arg is integer
     */
    public static boolean isInteger(final String a) {
        try {
            Integer.parseInt(a);
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Checks if given arg is Material
     * @param a String to check
     * @return Returns true if arg is material
     */
    public static boolean isMaterial(final String a){
        try {
            Material.valueOf(a);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Checks if given arg is Double
     * @param a String to check
     * @return Returns false if arg is double
     */
    public static boolean isDouble(final String a) {
        try {
            Double.parseDouble(a);
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Checks if given arg is Mob
     * @param mob_name String to check
     * @return Returns false if arg is Mob
     */
    public static boolean isMob(String mob_name){
        try{
            EntityType.valueOf(mob_name);
            return false;
        }
        catch (Exception e){
            return true;
        }
    }

    /**
     * Checks if given arg is Enchantment Name
     * @param a String to check
     * @return Returns true if arg is Enchantment
     */
    public static boolean isEnchantment(String a){
        try{
            Enchantment.getByName(a);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Clears full config
     * @param config_name Config name to clear
     */
    public static void clearConfig(String config_name){
        /*File file = new File(Main.getInstance().getDataFolder(), config_name);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);*/

        YamlConfiguration configuration = ConfigUtils.load(config_name, Main.getInstance());

        for(String key : configuration.getKeys(false)){
            configuration.set(key, null);
        }

        ConfigUtils.save(configuration, config_name);
    }

    /**
     * Changes miliseconds to hours
     * @param milis Miliseconds
     * @return Returns hours
     */
    public static int milisToHours(long milis){
        int time = (int) (((milis/1000)/60)/60);
        return time;
    }

    /**
     * Counts entities in specified chunk
     * @param entityType Types of entity to count
     * @param chunk Chunk where function will count entity
     * @return Return amount of entities
     */
    public static int getEntitiesInChunk(EntityType entityType, Chunk chunk){
        int sum = 0;
        for(Entity entity : chunk.getEntities()){
            if(entity.getType().equals(entityType)){
                sum++;
            }
        }
        return sum;
    }

    /**
     * Returns random number in range
     * @param min Lowest possible number
     * @param max Highest possible number
     * @return Returns random number
     */
    public static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String locationToString(Location location){
        return "World: " + location.getWorld().getName() + " x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ();
    }

}
