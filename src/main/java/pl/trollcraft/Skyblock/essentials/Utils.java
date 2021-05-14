package pl.trollcraft.Skyblock.essentials;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import pl.trollcraft.Skyblock.Skyblock;

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

    public static boolean isVillagerProfession(String a){
        try{
            Villager.Profession.valueOf(a);
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

        YamlConfiguration configuration = ConfigUtils.load(config_name, Skyblock.getInstance());

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
    public static Enchantment getEnchantmentByCommonName(String name){
        name = name.toLowerCase();
        if(name.equalsIgnoreCase("fire_protection")) return Enchantment.PROTECTION_FIRE;
        if(name.equalsIgnoreCase("blast_protection")) return Enchantment.PROTECTION_EXPLOSIONS;
        if(name.equalsIgnoreCase("projectile_protection")) return Enchantment.PROTECTION_PROJECTILE;
        if(name.equalsIgnoreCase("protection")) return Enchantment.PROTECTION_ENVIRONMENTAL;
        if(name.equalsIgnoreCase("feather_falling")) return Enchantment.PROTECTION_FALL;
        if(name.equalsIgnoreCase("respiration")) return Enchantment.OXYGEN;
        if(name.equalsIgnoreCase("aqua_affinity")) return Enchantment.WATER_WORKER;
        if(name.equalsIgnoreCase("sharpness")) return Enchantment.DAMAGE_ALL;
        if(name.equalsIgnoreCase("smite")) return Enchantment.DAMAGE_UNDEAD;
        if(name.equalsIgnoreCase("bane_of_arthropods")) return Enchantment.DAMAGE_ARTHROPODS;
        if(name.equalsIgnoreCase("knockback")) return Enchantment.KNOCKBACK;
        if(name.equalsIgnoreCase("fire_aspect")) return Enchantment.FIRE_ASPECT;
        if(name.equalsIgnoreCase("looting")) return Enchantment.LOOT_BONUS_MOBS;
        if(name.equalsIgnoreCase("power")) return Enchantment.ARROW_DAMAGE;
        if(name.equalsIgnoreCase("punch")) return Enchantment.ARROW_KNOCKBACK;
        if(name.equalsIgnoreCase("flame")) return Enchantment.ARROW_FIRE;
        if(name.equalsIgnoreCase("infinity")) return Enchantment.ARROW_INFINITE;
        if(name.equalsIgnoreCase("efficiency")) return Enchantment.DIG_SPEED;
        if(name.equalsIgnoreCase("unbreaking")) return Enchantment.DURABILITY;
        if(name.equalsIgnoreCase("silk_touch")) return Enchantment.SILK_TOUCH;
        if(name.equalsIgnoreCase("fortune")) return Enchantment.LOOT_BONUS_BLOCKS;
        if(name.equalsIgnoreCase("thorns")) return Enchantment.THORNS;
        if(name.equalsIgnoreCase("mending")) return Enchantment.MENDING;
        return null;
    }

}
