package pl.trollcraft.Skyblock.generator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

public class DeleteIsland {

    private static final IslandsController islandsController = Main.getIslandsController();
    private static final SkyblockPlayerController skyblockPlayerController = Main.getSkyblockPlayerController();

    /**
     * Deletes island
     * @param island Island to delete
     */
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

        for(Player player : Bukkit.getOnlinePlayers()){
            if( islandsController.getIslandByLocation( player.getLocation() ).equals(island) ){
                player.teleport(Storage.spawn);
                player.sendMessage(ChatUtils.fixColor("&aTeleportowano na spawna"));
            }
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            SkyblockPlayer skyblockPlayer = skyblockPlayerController.getPlayer(player.getName());
            if(player.getName().equalsIgnoreCase(owner)){
                skyblockPlayer.setIslandID(null);
            }
            else{
                skyblockPlayer.setCoopIslandID(null);
            }
        }

        islandsController.getIslands().remove( islandsController.getIslandIdByOwnerOrMember(owner) );

    }
}
