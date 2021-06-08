package pl.trollcraft.Skyblock.top;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TopController {

    private HashMap<String, Top> tops;

    public void load(){
        YamlConfiguration configuration = ConfigUtils.load("topConfig.yml", Skyblock.getInstance());

        tops = new HashMap<>();

        for(String topCategory : configuration.getConfigurationSection("top").getKeys(false)){

            tops.put(topCategory, new Top(
                    ChatUtils.fixColor(configuration.getString("top." + topCategory + ".header")),
                    ConfigUtils.getLocationFromConfig(configuration, "top." + topCategory),
                    null
            ));

        }

    }

    public void initTimer(){
        Skyblock.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {

                sync();

            }
        }, 60L, 72000L);
    }

    public void sync(){
        Debug.log("&aSyncing top...");
        for(String topCategory : tops.keySet()){
            Debug.log("Syncing top " + topCategory);
            String topJSON = Skyblock.getJedis().get("skyblock:" + topCategory);
            Debug.log(topJSON);
            LinkedHashMap<String, Integer> map = Skyblock.getGson().fromJson(topJSON, LinkedHashMap.class);

            Top top = tops.get(topCategory);

            if(top.getHologram() == null){
                top.setHologram(HologramsAPI.createHologram(Skyblock.getInstance(), top.getLocation()));
            }

            top.getHologram().clearLines();
            int i = 1;
            top.getHologram().insertTextLine(0, top.getHeader());
            for(String nickname : map.keySet()){
                String line = ChatUtils.fixColor("&e&l" + i + ".&e " + nickname + "&7: " + map.get(nickname));
                top.getHologram().insertTextLine(i, line);
                i++;
            }

        }

    }
}
