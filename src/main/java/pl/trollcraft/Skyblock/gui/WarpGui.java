package pl.trollcraft.Skyblock.gui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class WarpGui {

    private static Gui warpGui;

    private static ButtonController buttonController = Skyblock.getButtonController();

    public static void load(){

        YamlConfiguration configuration = ConfigUtils.load("warps.yml","gui", Skyblock.getInstance());

        warpGui = new Gui(configuration.getInt("rows"), configuration.getString("title"));

        warpGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        for(String key : configuration.getConfigurationSection("warps").getKeys(false)){

            Button button = buttonController.loadButton(configuration, "warps." + key);
            String command = configuration.getString("warps." + key + ".command");

            GuiItem item = ItemBuilder.from(button.getIcon()).asGuiItem(event -> {
               Player player = (Player) event.getWhoClicked();
               player.performCommand(command);
            });

            warpGui.setItem(button.getSlot(), item);

        }

        warpGui.getFiller().fill(ItemBuilder.from(ConfigUtils.getItemstack(configuration, "fillItem")).asGuiItem());
    }

    public static void openGui(Player player){
        warpGui.open(player);
    }
}
