package pl.trollcraft.Skyblock.skyblockplayer;

import java.util.HashMap;

public class SkyblockPlayers {

    private static HashMap<String, SkyblockPlayer> skyblockPlayers = new HashMap<>();

    public static SkyblockPlayer getPlayer(String nickname){
        return skyblockPlayers.get(nickname);
    }

    public static void addPlayer(String nickname, SkyblockPlayer skyblockPlayer){
        skyblockPlayers.put(nickname, skyblockPlayer);
    }

    public static void removePlayer(String nickname){
        skyblockPlayers.remove(nickname);
    }
}
