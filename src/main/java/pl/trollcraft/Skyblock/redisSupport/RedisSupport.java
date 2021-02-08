package pl.trollcraft.Skyblock.redisSupport;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayers;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.Arrays;

public class RedisSupport {

    public static void loadPlayer(Player player){

        ChatUtils.sendMessage(player, "&f&lLoading stats...");

        new BukkitRunnable(){

            @Override
            public void run() {

                String nickname = player.getName();

                String code = Storage.redisCode;
                code = code.replace("%player%", nickname);

                String islandID = Main.getJedis().hget(code, "islandID");
                String coopIslandID = Main.getJedis().hget(code, "coopIslandID");
                String[] tempInvites = Main.getJedis().hget(code, "invites").split(":");

                ArrayList<String> invites = new ArrayList<>(Arrays.asList(tempInvites));

                SkyblockPlayers.addPlayer(nickname, new SkyblockPlayer(islandID, coopIslandID, invites));

                sendMessage(player, "&a&lLoaded stats!");

            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 20L);

    }

    public static void savePlayer(Player player){
        String nickname = player.getName();
        SkyblockPlayer skyblockPlayer = SkyblockPlayers.getPlayer(nickname);

        new BukkitRunnable(){

            @Override
            public void run() {

                String code = Storage.redisCode;
                code = code.replace("%player%", nickname);

                String islandID = skyblockPlayer.getIslandID();
                String coopIslandID = skyblockPlayer.getCoopIslandID();
                StringBuilder invites = new StringBuilder();

                int i = 0;
                for(String invite : skyblockPlayer.getInvites()){
                    if(i == 0){
                        invites.append(invite);
                    }
                    else{
                        invites.append(":").append(invite);
                    }
                    i++;
                }

                Pipeline pipeline = Main.getJedis().pipelined();
                pipeline.sadd(code, "islandID", islandID);
                pipeline.sadd(code, "coopIslandID", coopIslandID);
                pipeline.sadd(code, "invites", invites.toString());
                pipeline.sync();

            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    private static void sendMessage(Player player, String message){
        Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                ChatUtils.sendMessage(player, message);
            }
        });
    }

    public static String convertIslandToString(String owner, ArrayList<String> members){
        return null;
    }
}
