package pl.trollcraft.Skyblock.skyblockplayer;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.IslandsController;

import java.util.ArrayList;
import java.util.HashMap;

public class SkyblockPlayerController {

    private final ArrayList<Player> uncheckedPlayers = new ArrayList<>();
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

    public void savePlayer(String playerName){
        SkyblockPlayer skyblockPlayer = getPlayer(playerName);

        YamlConfiguration configuration = ConfigUtils.load("players.yml", Skyblock.getInstance());

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
        if(skyblockPlayers.get(member).getInvites() == null){
            return false;
        }
        return skyblockPlayers.get(member).getInvites().contains(owner);
    }

    public void addInvite(String owner, String member){
        SkyblockPlayer skyblockPlayer = getPlayer(member);
        skyblockPlayer.addInvite(owner);
    }

    public void remInvite(String owner, String member){
        SkyblockPlayer skyblockPlayer = getPlayer(member);
        skyblockPlayer.removeInvite(owner);
    }

    public void clearInvites(String member){
        skyblockPlayers.get(member).clearInvites();
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

    public void addUncheckedPlayer(Player player){
        uncheckedPlayers.add(player);
    }

    public void removeUncheckedPlayer(Player player){
        uncheckedPlayers.remove(player);
    }

    public boolean isCurrentlyChecked(Player player){
        return uncheckedPlayers.contains(player);
    }

    public void initCheckingPlayers(){
        final IslandsController islandsController = Skyblock.getIslandsController();
        ArrayList<Player> toRemove = new ArrayList<>();
        Skyblock.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(uncheckedPlayers.size() == 0){
                    return;
                }

                for(Player player : uncheckedPlayers){
                    if(islandsController.isPlayerOnHisIsland(player)) {
                        getPlayer(player.getName()).setOnIsland(true);
                    }
                    toRemove.add(player);
                }

                for(Player player : toRemove){
                    uncheckedPlayers.remove(player);
                }

                toRemove.clear();
            }
        }, 20L, 60L);
    }
}
