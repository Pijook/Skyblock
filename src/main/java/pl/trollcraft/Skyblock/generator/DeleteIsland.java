package pl.trollcraft.Skyblock.generator;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class DeleteIsland {


    public static void deleteIs(String player){
        YamlConfiguration configuration = ConfigUtils.load("islands.yml", Main.getInstance());

        for( String owner : configuration.getConfigurationSection("islands").getKeys(false)){
            if( owner.equalsIgnoreCase(player)){

                double x =  configuration.getDouble("islands." + owner + ".center.x");
                double y = configuration.getDouble("islands." + owner + ".center.y");
                double z = configuration.getDouble("islands." + owner + ".center.z");
                String world = configuration.getString("islands." + owner + ".center.world");

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
                configuration.set("islands." + owner, null);
                break;
            }
        }
        ConfigUtils.save(configuration, "islands.yml");
    }
}
