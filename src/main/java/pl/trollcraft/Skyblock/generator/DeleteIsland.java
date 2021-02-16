package pl.trollcraft.Skyblock.generator;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;

public class DeleteIsland {

    private static final IslandsController islandsController = Main.getIslandsController();

    public static void deleteIs(Island island){
        String owner = island.getOwner();

        Location center = island.getCenter();

//        double x =  configuration.getDouble("islands." + owner + ".center.x");
//        double y = configuration.getDouble("islands." + owner + ".center.y");
//        double z = configuration.getDouble("islands." + owner + ".center.z");
//        String world = configuration.getString("islands." + owner + ".center.world");
        double x =  center.getX();
        double y = center.getY();
        double z = center.getZ();
        String world = center.getWorld().getName();

        YamlConfiguration freePosistions = ConfigUtils.load("freeislands.yml", Main.getInstance());

        int freeIsId = 0;
        if( freePosistions.getConfigurationSection("free") != null ){
            for( String id : freePosistions.getConfigurationSection("free").getKeys(false)){
                freeIsId =  Integer.parseInt(id);
            }
        }
        freeIsId++;

        freePosistions.set("free." + freeIsId + ".x", x);
        freePosistions.set("free." + freeIsId + ".y", y);
        freePosistions.set("free." + freeIsId + ".z", z);
        freePosistions.set("free." + freeIsId + ".world", world);

        ConfigUtils.save(freePosistions, "freeislands.yml");
        islandsController.getIslands().remove(owner);
    }
}
