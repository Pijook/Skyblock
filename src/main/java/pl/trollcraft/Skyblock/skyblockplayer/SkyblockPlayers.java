package pl.trollcraft.Skyblock.skyblockplayer;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import sun.security.krb5.Config;

import java.util.HashMap;

public class SkyblockPlayers {

    private static HashMap<String, SkyblockPlayer> skyblockPlayers = new HashMap<>();

    public static SkyblockPlayer getPlayer(String nickname){
        return skyblockPlayers.get(nickname);
    }

    private static void addPlayer(String nickname, SkyblockPlayer skyblockPlayer){
        skyblockPlayers.put(nickname, skyblockPlayer);
    }

    private static void removePlayer(String nickname){
        skyblockPlayers.remove(nickname);
    }

    public static void loadPlayer(String playerName){
        YamlConfiguration configuration = ConfigUtils.load("players.yml", Main.getInstance());

        SkyblockPlayer skyblockPlayer = new SkyblockPlayer(null, null);

        if(configuration.contains("players." + playerName + ".island")){
            skyblockPlayer.setIslandID(configuration.getString("players." + playerName + ".island"));
        }
        if(configuration.contains("players." + playerName + ".coopIsland")){
            skyblockPlayer.setCoopIslandID(configuration.getString("players." + playerName + ".coopIsland"));
        }

        skyblockPlayers.put(playerName, skyblockPlayer);
    }

    public static void savePlayer(String playerName){
        SkyblockPlayer skyblockPlayer = getPlayer(playerName);

        YamlConfiguration configuration = ConfigUtils.load("players.yml", Main.getInstance());

        if(skyblockPlayer.getIslandID() != null){
            configuration.set("players." + playerName + ".island", skyblockPlayer.getIslandID());
        }
        if(skyblockPlayer.getCoopIslandID() != null){
            configuration.set("players." + playerName + ".coopIsland", skyblockPlayer.getCoopIslandID());
        }

        removePlayer(playerName);

        ConfigUtils.save(configuration, "players.yml");
    }
}
