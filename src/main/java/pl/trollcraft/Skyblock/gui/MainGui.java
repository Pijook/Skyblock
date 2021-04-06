package pl.trollcraft.Skyblock.gui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.islandGui.IslandGui;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;

public class MainGui {

    private static Gui mainGui;

    private static final ButtonController buttonController = Skyblock.getButtonController();

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

         /*
            ----- ----- -----
                Kits
            ----- ----- -----
        */

        Button kitButton = buttonController.loadButton(configuration, "buttons.kit");

        GuiItem kitItem = ItemBuilder.from(kitButton.getIcon()).asGuiItem(event -> {
            //Open kit gui
            KitGui.openGui((Player) event.getWhoClicked());
        });

        mainGui.setItem(kitButton.getSlot(), kitItem);

         /*
            ----- ----- -----
                Tutorial
            ----- ----- -----
        */

        Button tutorialButton = buttonController.loadButton(configuration, "buttons.tutorial");

        GuiItem tutorialItem = ItemBuilder.from(tutorialButton.getIcon()).asGuiItem(event -> {
            //Teleport on tutorial
            event.getWhoClicked().teleport(new Location(Bukkit.getWorld("Islands"), 0, 80, 0));
        });

        mainGui.setItem(tutorialButton.getSlot(), tutorialItem);

        /*
            ----- ----- -----
                Island
            ----- ----- -----
        */

        Button islandButton = buttonController.loadButton(configuration, "buttons.island");

        GuiItem islandItem = ItemBuilder.from(islandButton.getIcon()).asGuiItem(event -> {
            //Open island gui
            Player player = (Player) event.getWhoClicked();
            SkyblockPlayer skyblockPlayer = Skyblock.getSkyblockPlayerController().getPlayer(player.getName());

            if(!skyblockPlayer.hasIslandOrCoop()){
                mainGui.close(player);
                ChatUtils.sendMessage(player, "&cMusisz miec wyspe aby otworzyc to menu!");
                return;
            }

            IslandGui.openGui((Player) event.getWhoClicked());
        });

        mainGui.setItem(islandButton.getSlot(), islandItem);

        /*
            ----- ----- -----
                Jobs
            ----- ----- -----
        */

        Button jobsButton = buttonController.loadButton(configuration, "buttons.jobs");

        GuiItem jobsItem = ItemBuilder.from(jobsButton.getIcon()).asGuiItem(event -> {
            Skyblock.getWorkerController().showGUIToPlayer((Player) event.getWhoClicked());
        });

        mainGui.setItem(jobsButton.getSlot(), jobsItem);

        /*
            ----- ----- -----
                Statue
            ----- ----- -----
        */

        Button statueButton = buttonController.loadButton(configuration, "buttons.statue");

        GuiItem statueItem = ItemBuilder.from(statueButton.getIcon()).asGuiItem(event -> {
            //Send on chat or teleport to warp?
            ChatUtils.sendMessage((Player) event.getWhoClicked(), "&c&lFinish code!");
        });

        mainGui.setItem(statueButton.getSlot(), statueItem);

        /*
            ----- ----- -----
              Communicators
            ----- ----- -----
        */

        Button communicatorsButton = buttonController.loadButton(configuration, "buttons.communicators");

        GuiItem communicatorsItem = ItemBuilder.from(communicatorsButton.getIcon()).asGuiItem(event -> {
            //Send on chat?
            ChatUtils.sendMessage((Player) event.getWhoClicked(), "&c&lFinish code!");
        });

        mainGui.setItem(communicatorsButton.getSlot(), communicatorsItem);

        /*
            ----- ----- -----
                Warps
            ----- ----- -----
        */

        Button warpsButton = buttonController.loadButton(configuration, "buttons.warps");
        GuiItem warpsItem = ItemBuilder.from(warpsButton.getIcon()).asGuiItem(event -> {
            WarpGui.openGui((Player) event.getWhoClicked());
        });

        mainGui.setItem(warpsButton.getSlot(), warpsItem);
        /*
            ----- ----- -----
                Shop
            ----- ----- -----
        */

        Button shopButton = buttonController.loadButton(configuration, "buttons.shop");
        GuiItem shopItem = ItemBuilder.from(shopButton.getIcon()).asGuiItem(event -> {
           Player player = (Player) event.getWhoClicked();

           player.performCommand("shop");
        });

        mainGui.setItem(shopButton.getSlot(), shopItem);

        /*
            ----- ----- -----
                Close
            ----- ----- -----
        */

        Button closeButton = buttonController.loadButton(configuration, "buttons.close");
        GuiItem closeItem = ItemBuilder.from(closeButton.getIcon()).asGuiItem(event -> {
           mainGui.close(event.getWhoClicked());
        });

        mainGui.setItem(closeButton.getSlot(), closeItem);

        //Filling gui
        mainGui.getFiller().fill(filler);

    }

    public static void openGui(Player player){
        mainGui.open(player);
    }
}
