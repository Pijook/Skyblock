package pl.trollcraft.Skyblock.islandUpgrades;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

import java.util.HashMap;

public class IslandSizes {

    private static HashMap<Integer, IslandSize> islandSizes;

    public static IslandSize getIslandSize(int level){

        if( islandSizes.containsKey(level)){
            return islandSizes.get(level);
        }
        return null;
    }

    public static boolean loadSizes(){

        YamlConfiguration sizeConfig = ConfigUtils.load("upgrades.yml", Skyblock.getInstance());

        int checkLevel = 0;

        for( String sLevel : sizeConfig.getConfigurationSection("sizeUpgrade").getKeys(false) ){
            int level;
            if(Utils.isInteger(sLevel)){
                level = Integer.parseInt(sLevel);
            }
            else{
                Debug.sendError("[Size] Podany poziom (" + sLevel + ") nie jest liczba" );
                return false;
            }
            if( level != ( checkLevel+1 ) ){
                Debug.sendError("[Size] Brakuje poziomu " + (checkLevel-1) );
                return false;
            }
            int size;
            if( Utils.isInteger( sizeConfig.getString("sizeUpgrade." + sLevel + ".size") )){
                size = Integer.parseInt( sizeConfig.getString("sizeUpgrade." + sLevel + ".size") );



            }

            checkLevel++;
        }
        return true;
    }




}
