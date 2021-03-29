package pl.trollcraft.Skyblock.gui.islandGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class MembersGui {

    private static Gui membersGui;

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("members.yml", Skyblock.getInstance());

        membersGui = new Gui(configuration.getInt("rows"), configuration.getString("title"));
        membersGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        

        membersGui.getFiller().fill(ItemBuilder.from(ConfigUtils.getItemstack(configuration, "fillItem")).asGuiItem());
    }

    public static void openGui(Player player){
        membersGui.open(player);
    }
}
