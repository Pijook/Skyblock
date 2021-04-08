package pl.trollcraft.Skyblock.gui.upgradesGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.Button;
import pl.trollcraft.Skyblock.gui.ButtonController;

public class UpgradesGui {

    private static Gui upgradesGui;
    private static ButtonController buttonController = Skyblock.getButtonController();

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("upgrades.yml", "gui", Skyblock.getInstance());

        upgradesGui = new Gui(configuration.getInt("rows"), configuration.getString("title"));
        upgradesGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        //Limits
        Button limitsButton = buttonController.loadButton(configuration, "buttons.limits");
        upgradesGui.setItem(limitsButton.getSlot(), ItemBuilder.from(limitsButton.getIcon()).asGuiItem(event -> {
            LimitsGui.open((Player) event.getWhoClicked());
        }));

        //Island border
        Button borderButton = buttonController.loadButton(configuration, "buttons.border");
        upgradesGui.setItem(borderButton.getSlot(), ItemBuilder.from(borderButton.getIcon()).asGuiItem(event -> {
            IslandSizeGui.openGui((Player) event.getWhoClicked());
        }));

        //Drop from stone
        Button dropButtton = buttonController.loadButton(configuration, "buttons.drop");
        upgradesGui.setItem(dropButtton.getSlot(), ItemBuilder.from(dropButtton.getIcon()).asGuiItem(event -> {
            DropGui.openGui((Player) event.getWhoClicked());
        }));
    }

    public static void openGui(Player player){
        upgradesGui.open(player);
    }
}
