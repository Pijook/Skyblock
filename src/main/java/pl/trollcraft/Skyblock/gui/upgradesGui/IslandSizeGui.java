package pl.trollcraft.Skyblock.gui.upgradesGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.Button;
import pl.trollcraft.Skyblock.gui.ButtonController;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;

public class IslandSizeGui {

    private static ButtonController buttonController = Skyblock.getButtonController();
    private static IslandsController islandsController = Skyblock.getIslandsController();

    private static Button upgradeButton;
    private static Button returnButton;

    private static ItemStack fillItem;

    private static String title;
    private static int rows;

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("islandsize.yml", "gui", Skyblock.getInstance());

        fillItem = ConfigUtils.getItemstack(configuration, "fillItem");
        title = configuration.getString("title");
        rows = configuration.getInt("rows");

        upgradeButton = buttonController.loadButton(configuration, "buttons.upgrade");
        returnButton = buttonController.loadButton(configuration, "buttons.return");
    }

    public static void openGui(Player player){
        Gui gui = new Gui(rows, title);


        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        gui.setItem(upgradeButton.getSlot(), ItemBuilder.from(upgradeButton.getIcon()).asGuiItem(event -> {
            Player target = (Player) event.getWhoClicked();
            target.performCommand("is upgrade");
            gui.close(target);
        }));

        gui.setItem(returnButton.getSlot(), ItemBuilder.from(returnButton.getIcon()).asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        gui.getFiller().fill(ItemBuilder.from(fillItem).asGuiItem());

        gui.open(player);
    }
}
