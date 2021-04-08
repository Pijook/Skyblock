package pl.trollcraft.Skyblock.kit;

import org.bouncycastle.jcajce.provider.digest.Skein;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
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

        for(String kitName : configuration.getConfigurationSection("kits").getKeys(false)){

            HashMap<Integer, Kit> kitSet = new HashMap<>();

            for(String kitLevel : configuration.getConfigurationSection("kits." + kitName).getKeys(false)){

                ArrayList<ItemStack> items = new ArrayList<>();

                for(String itemName : configuration.getConfigurationSection("kits." + kitName + "." + kitLevel + ".items").getKeys(false)){

                    items.add(ConfigUtils.getItemstack(configuration,"kits." + kitName + "." + kitLevel + ".items." + itemName));

                }

                kitSet.put(Integer.parseInt(kitLevel), new Kit(items));
            }

            availableKits.put(kitName, new KitSet(kitSet, configuration.getLong("kits.")));

        }
    }


    public void giveKit(Player player, String kitName){

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
            System.out.println(playerCooldowns.get(nickname).get(kitName));
            Long currentCooldown = playerCooldowns.get(nickname).get(kitName);

            long restTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - currentCooldown);

            if(restTime > availableKits.get(kitName).getCooldown()){
                ChatUtils.sendMessage(player, "&cMozesz odebrac ten kit za " + (availableKits.get(kitName).getCooldown() - restTime));
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
    }

}
