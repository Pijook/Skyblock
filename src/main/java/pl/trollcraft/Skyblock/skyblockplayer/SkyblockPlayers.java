package pl.trollcraft.Skyblock.skyblockplayer;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import sun.security.krb5.Config;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyblockPlayers {

    private static HashMap<String, SkyblockPlayer> skyblockPlayers = new HashMap<>();

    public static SkyblockPlayer getPlayer(String nickname){
        return skyblockPlayers.get(nickname);
    }

    public static void addPlayer(String nickname, SkyblockPlayer skyblockPlayer){
        skyblockPlayers.put(nickname, skyblockPlayer);
    }

    private static void removePlayer(String nickname){
        skyblockPlayers.remove(nickname);
    }

    /*public static void loadPlayer(String playerName){
        YamlConfiguration configuration = ConfigUtils.load("players.yml", Main.getInstance());

        SkyblockPlayer skyblockPlayer = new SkyblockPlayer(null, null, null);

        if(configuration.contains("players." + playerName + ".island")){
            skyblockPlayer.setIslandID(configuration.getString("players." + playerName + ".island"));
        }
        if(configuration.contains("players." + playerName + ".coopIsland")){
            skyblockPlayer.setCoopIslandID(configuration.getString("players." + playerName + ".coopIsland"));
        }

        addPlayer(playerName, skyblockPlayer);
//        skyblockPlayers.put(playerName, skyblockPlayer);
    }*/

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

    public static boolean hasInvite(String owner, String member){
        return skyblockPlayers.get(member).getInvites().contains(owner);
    }

    public static void addInvite(String owner, String member){
        ArrayList<String> invites = skyblockPlayers.get(member).getInvites();
        invites.add(owner);
        skyblockPlayers.get(member).setInvites(invites);
    }

    public static void remInvite(String owner, String member){
        ArrayList<String> invites = skyblockPlayers.get(member).getInvites();
        invites.remove(owner);
        skyblockPlayers.get(member).setInvites(invites);
    }

    public static void clearInvites(String member){
        skyblockPlayers.get(member).setInvites(null);
    }

    public static void debugPlayers(){
        Debug.log("Debugging players...");
        Debug.log("Players to show: " + skyblockPlayers.size());
        for(String nickname : skyblockPlayers.keySet()){
            SkyblockPlayer skyblockPlayer = getPlayer(nickname);
            Debug.log("Nickname: " + nickname);
            Debug.log("Has island: " + skyblockPlayer.hasIslandOrCoop());
        }
    }
}
