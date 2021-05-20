package pl.trollcraft.Skyblock.kit;

import org.bouncycastle.jcajce.provider.digest.Skein;
import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.PermissionStorage;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class KitManager {

    private HashMap<String, KitSet> availableKits = new HashMap<>();
    private HashMap<String, HashMap<String, Long>> playerCooldowns = new HashMap<>();

    public void loadKits(){
        YamlConfiguration configuration = ConfigUtils.load("kits.yml", Skyblock.getInstance());

        Storage.kitsEnabled = configuration.getBoolean("enabled");

        for(String kitName : configuration.getConfigurationSection("kits").getKeys(false)){

            HashMap<Integer, Kit> kitSet = new HashMap<>();

            for(String kitLevel : configuration.getConfigurationSection("kits." + kitName + ".levels").getKeys(false)){

                ArrayList<ItemStack> items = new ArrayList<>();

                for(String itemName : configuration.getConfigurationSection("kits." + kitName + ".levels." + kitLevel + ".items").getKeys(false)){

                    items.add(ConfigUtils.getItemstack(configuration,"kits." + kitName + ".levels." + kitLevel + ".items." + itemName));

                }

                kitSet.put(Integer.parseInt(kitLevel), new Kit(items));
            }

            availableKits.put(kitName, new KitSet(kitSet, configuration.getLong("kits." + kitName + ".cooldown")));

        }

    }


    public void giveKit(String nickname, String kitName){

        Player player = Bukkit.getPlayer(nickname);

        if( !player.hasPermission("" + PermissionStorage.kitPermission + "." + kitName)){
            ChatUtils.sendMessage(player, "&cNie masz dostepu do tego zestawu!");
            return;
        }

        if(!availableKits.containsKey(kitName)){
            ChatUtils.sendMessage(player, "&cNie znaleziono takiego zestawu!");
            return;
        }

        if(playerCooldowns.containsKey(player.getName())){
            Debug.log("Player cooldowns contains player!");
            if(playerCooldowns.get(player.getName()).containsKey(kitName)){
                Debug.log("Player cooldowns contains player in specified kit!");
                long currentCooldown = playerCooldowns.get(player.getName()).get(kitName);

                Debug.log("Player cooldown: " + currentCooldown);

                long difference = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - currentCooldown);

                Debug.log("DIfference: " + difference);
                Debug.log("Kit cooldown: " + availableKits.get(kitName).getCooldown());

                if(difference < availableKits.get(kitName).getCooldown()){
                    Debug.log("Player cannot get kit!");
                    ChatUtils.sendMessage(player, "&cMozesz odebrac ten zestaw za " + (availableKits.get(kitName).getCooldown() - difference) + " sekund!");
                    return;
                }

            }
        }

        Debug.log("Player got a kit!");
        KitSet kit = availableKits.get(kitName);
        kit.getKit(
                (int) Skyblock.
                        getWorkerController().
                        getWorkerByName(player.getName()).
                        getAverageLevel()).
                giveItems(player);
        ChatUtils.sendMessage(player, "&aOtrzymano zestaw " + kitName + "!");

        saveTime(player, kitName);
    }

    public void saveTime(Player player, String kitName){
        if(!playerCooldowns.containsKey(player.getName())){
            playerCooldowns.put(player.getName(), new HashMap<String, Long>());
        }
        playerCooldowns.get(player.getName()).put(kitName, System.currentTimeMillis());
    }

    public void loadPlayer(Player player){
        YamlConfiguration configuration = ConfigUtils.load("kitsCooldowns.yml", Skyblock.getInstance());

        if(configuration.contains("cooldowns." + player.getName())){

            HashMap<String, Long> cooldowns = new HashMap<>();

            for(String kitName : configuration.getConfigurationSection("cooldowns." + player.getName()).getKeys(false)){
                cooldowns.put(kitName, configuration.getLong("cooldowns." + player.getName() + "." + kitName));
            }
        }
        else{
            playerCooldowns.put(player.getName(), new HashMap<>());
        }
    }

    public void savePlayer(Player player){

        if(playerCooldowns.containsKey(player.getName())){
            YamlConfiguration configuration = ConfigUtils.load("kitsCooldowns.yml", Skyblock.getInstance());
            HashMap<String, Long> cooldowns = playerCooldowns.get(player.getName());

            for(String kitName : cooldowns.keySet()){
                configuration.set("cooldowns." + player.getName() + "." + kitName, cooldowns.get(kitName));
            }

            ConfigUtils.save(configuration, "kitsCooldowns.yml");

        }

    }

    /*public void saveTimeToGlobal(Player player){
        if(playerCooldowns.containsKey(player.getName())){
            String kitJedis = Skyblock.getGson().toJson(playerCooldowns.get(player.getName()));
            String code = RedisSupport.getCode(player.getName());
            Skyblock.getJedis().hset(code, "kit", kitJedis);

            playerCooldowns.remove(player.getName());
        }
    }

    public void getFromGlobal(Player player){
        String code = RedisSupport.getCode(player.getName());
        String kitJedis = Skyblock.getJedis().hget(code, "kit");

        if(kitJedis == null){
            playerCooldowns.put(player.getName(), new HashMap<>());
        }
        else{
            HashMap<String, Long> kitCooldown = Skyblock.getGson().fromJson(kitJedis, HashMap.class);
            playerCooldowns.put(player.getName(), kitCooldown);
        }

    }*/

    /*public void giveKit(Player player, String kitName){

        if(!availableKits.containsKey(kitName)){
            ChatUtils.sendMessage(player, "&cNie znaleziono takiego zestawu!");
            return;
        }

        String nickname = player.getName();
        if(!playerCooldowns.containsKey(nickname)){
            String code = RedisSupport.getCode(nickname);
            HashMap<String, Long> cooldowns = Skyblock.getGson().fromJson(Skyblock.getJedis().hget(code, "kit"), HashMap.class);
            playerCooldowns.put(nickname, cooldowns);
        }

        if(playerCooldowns.get(nickname).containsKey(kitName)){
            Long currentCooldown = playerCooldowns.get(nickname).get(kitName);

            Debug.log("Current cooldown:" + currentCooldown);

            //long restTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - currentCooldown);
            long restTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - currentCooldown);

            Debug.log("Rest time: " + restTime);
            if(restTime < availableKits.get(kitName).getCooldown()){
                ChatUtils.sendMessage(player, "&cMozesz odebrac ten kit za " + (availableKits.get(kitName).getCooldown() - restTime));
                Debug.log("Rest time to player: " + (availableKits.get(kitName).getCooldown() - restTime));
                return;
            }
        }


        Kit kit = availableKits.get(kitName).getKit((int) Skyblock.getWorkerController().getWorkerByName(player.getName()).getAverageLevel());
        kit.giveItems(player);
        ChatUtils.sendMessage(player, "&aOtrzymano kit!");

        saveCooldown(nickname, kitName);
    }

    public void clearMap(String nickname){
        playerCooldowns.remove(nickname);
    }

    public void saveCooldown(String nickname, String kit){
        String code = RedisSupport.getCode(nickname);
        HashMap<String, Long> cooldowns = playerCooldowns.get(nickname);
        cooldowns.put(kit, System.currentTimeMillis());
        Skyblock.getJedis().hset(code, "kit", Skyblock.getGson().toJson(cooldowns));
    }*/

}
