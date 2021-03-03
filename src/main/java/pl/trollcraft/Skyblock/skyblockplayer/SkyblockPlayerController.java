package pl.trollcraft.Skyblock.skyblockplayer;

import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyblockPlayerController {

    private final HashMap<String, SkyblockPlayer> skyblockPlayers = new HashMap<>();

    public SkyblockPlayer getPlayer(String nickname){
        return skyblockPlayers.get(nickname);
    }

    public void addPlayer(String nickname, SkyblockPlayer skyblockPlayer){
        skyblockPlayers.put(nickname, skyblockPlayer);
    }

    public void removePlayer(String nickname){
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

    public void savePlayer(String playerName){
        SkyblockPlayer skyblockPlayer = getPlayer(playerName);

        YamlConfiguration configuration = ConfigUtils.load("players.yml", Main.getInstance());

        if(skyblockPlayer.getIslandID() != null){
            configuration.set("players." + playerName + ".island", skyblockPlayer.getIslandID().toString());
        }
        if(skyblockPlayer.getCoopIslandID() != null){
            configuration.set("players." + playerName + ".coopIsland", skyblockPlayer.getCoopIslandID().toString());
        }

        removePlayer(playerName);

        ConfigUtils.save(configuration, "players.yml");
    }

    public boolean hasInvite(String owner, String member){
        return skyblockPlayers.get(member).getInvites().contains(owner);
    }

    public void addInvite(String owner, String member){
        ArrayList<String> invites = skyblockPlayers.get(member).getInvites();
        invites.add(owner);
        skyblockPlayers.get(member).setInvites(invites);
    }

    public void remInvite(String owner, String member){
        ArrayList<String> invites = skyblockPlayers.get(member).getInvites();
        invites.remove(owner);
        skyblockPlayers.get(member).setInvites(invites);
    }

    public void clearInvites(String member){
        skyblockPlayers.get(member).setInvites(null);
    }

    public void debugPlayers(){
        Debug.log("Debugging players...");
        Debug.log("Players to show: " + skyblockPlayers.size());
        for(String nickname : skyblockPlayers.keySet()){
            SkyblockPlayer skyblockPlayer = getPlayer(nickname);
            Debug.log("Nickname: " + nickname);
            Debug.log("Has island: " + skyblockPlayer.hasIslandOrCoop());
        }
    }

    public boolean isPlayerLoaded(String nickname){
        return skyblockPlayers.containsKey(nickname);
    }
}
