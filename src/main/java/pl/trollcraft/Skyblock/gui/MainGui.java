package pl.trollcraft.Skyblock.gui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.islandGui.IslandGui;

public class MainGui {

    private static Gui mainGui;

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("main.yml", "gui", Skyblock.getInstance());

        String title = configuration.getString("title");
        int rows = configuration.getInt("rows");

        mainGui = new Gui(rows, title);

        mainGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        ItemStack fillIcon = ConfigUtils.getItemstack(configuration, "fillItem");
        GuiItem filler = ItemBuilder.from(fillIcon).asGuiItem();

        // --- Loading buttons ---

        //Kit
        ItemStack kitIcon = ConfigUtils.getItemstack(configuration, "buttons.kit.icon");
        int kitSlot = configuration.getInt("buttons.kit.slot");

        GuiItem kitItem = ItemBuilder.from(kitIcon).asGuiItem(event -> {
            //Open kit gui
            KitGui.openGui((Player) event.getWhoClicked());
        });

        mainGui.setItem(kitSlot, kitItem);

        //Tutorial
        ItemStack tutorialIcon = ConfigUtils.getItemstack(configuration, "buttons.tutorial.icon");
        int tutorialSlot = configuration.getInt("buttons.tutorial.slot");

        GuiItem tutorialItem = ItemBuilder.from(tutorialIcon).asGuiItem(event -> {
            //Teleport on tutorial
            event.getWhoClicked().teleport(new Location(Bukkit.getWorld("Islands"), 0, 80, 0));
        });

        mainGui.setItem(tutorialSlot, tutorialItem);

        //Island
        ItemStack islandIcon = ConfigUtils.getItemstack(configuration, "buttons.island.icon");
        int islandSlot = configuration.getInt("buttons.island.slot");

        GuiItem islandItem = ItemBuilder.from(islandIcon).asGuiItem(event -> {
            //Open island gui
            IslandGui.openGui((Player) event.getWhoClicked());
        });

        mainGui.setItem(islandSlot, islandItem);

        //Filling gui
        mainGui.getFiller().fill(filler);

    }

    public static void openGui(Player player){
        mainGui.open(player);
    }
}
