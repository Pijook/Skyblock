package pl.trollcraft.Skyblock.island;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

import java.util.HashMap;
import java.util.UUID;

public class PointsController {

    private HashMap<String, Integer> blockValues = new HashMap<>();

    private final IslandsController islandsController = Skyblock.getIslandsController();

    public void load(){
        YamlConfiguration configuration = ConfigUtils.load("blocksvalues.yml", Skyblock.getInstance());

        for(String blockType : configuration.getConfigurationSection("blocks").getKeys(false)){

            if(!Utils.isMaterial(blockType)){
                Debug.sendError("&c&lWrong material! (" + blockType + ")");
                continue;
            }

            blockValues.put(blockType, configuration.getInt("blocks." + blockType));

        }
    }

    public void addPoints(String blockType, UUID islandID){
        if(!blockValues.containsKey(blockType)){
            return;
        }

        Island island = islandsController.getIslandById(islandID);
        island.setPoints(island.getPoints() + blockValues.get(blockType));
    }

    public void removePoints(String blockType, UUID islandID){
        if(!blockValues.containsKey(blockType)){
            return;
        }

        Island island = islandsController.getIslandById(islandID);

        if(island.getPoints() - blockValues.get(blockType) > 0){
            island.setPoints(island.getPoints() - blockValues.get(blockType));
        }
    }
}
