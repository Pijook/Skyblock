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
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.islandGui.IslandGui;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;

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

         /*
            ----- ----- -----
                Kits
            ----- ----- -----
        */

        ItemStack kitIcon = ConfigUtils.getItemstack(configuration, "buttons.kit.icon");
        int kitSlot = configuration.getInt("buttons.kit.slot");

        GuiItem kitItem = ItemBuilder.from(kitIcon).asGuiItem(event -> {
            //Open kit gui
            KitGui.openGui((Player) event.getWhoClicked());
        });

        mainGui.setItem(kitSlot, kitItem);

         /*
            ----- ----- -----
                Tutorial
            ----- ----- -----
        */

        ItemStack tutorialIcon = ConfigUtils.getItemstack(configuration, "buttons.tutorial.icon");
        int tutorialSlot = configuration.getInt("buttons.tutorial.slot");

        GuiItem tutorialItem = ItemBuilder.from(tutorialIcon).asGuiItem(event -> {
            //Teleport on tutorial
            event.getWhoClicked().teleport(new Location(Bukkit.getWorld("Islands"), 0, 80, 0));
        });

        mainGui.setItem(tutorialSlot, tutorialItem);

        /*
            ----- ----- -----
                Island
            ----- ----- -----
        */

        ItemStack islandIcon = ConfigUtils.getItemstack(configuration, "buttons.island.icon");
        int islandSlot = configuration.getInt("buttons.island.slot");

        GuiItem islandItem = ItemBuilder.from(islandIcon).asGuiItem(event -> {
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

        mainGui.setItem(islandSlot, islandItem);

        /*
            ----- ----- -----
                Jobs
            ----- ----- -----
        */

        ItemStack jobsIcon = ConfigUtils.getItemstack(configuration, "buttons.jobs.icon");
        int jobsSlot = configuration.getInt("buttons.jobs.slot");

        GuiItem jobsItem = ItemBuilder.from(jobsIcon).asGuiItem(event -> {
            Skyblock.getWorkerController().showGUIToPlayer((Player) event.getWhoClicked());
        });

        mainGui.setItem(jobsSlot, jobsItem);

        /*
            ----- ----- -----
                Statue
            ----- ----- -----
        */

        ItemStack statueIcon = ConfigUtils.getItemstack(configuration, "buttons.statue.icon");
        int statueSlot = configuration.getInt("buttons.statue.slot");

        GuiItem statueItem = ItemBuilder.from(statueIcon).asGuiItem(event -> {
            //Send on chat or teleport to warp?
            ChatUtils.sendMessage((Player) event.getWhoClicked(), "&c&lFinish code!");
        });

        mainGui.setItem(statueSlot, statueItem);

        /*
            ----- ----- -----
              Communicators
            ----- ----- -----
        */

        ItemStack communicatorsIcon = ConfigUtils.getItemstack(configuration, "buttons.communicators.icon");
        int communicatorsSlot = configuration.getInt("buttons.communicators.slot");

        GuiItem communicatorsItem = ItemBuilder.from(communicatorsIcon).asGuiItem(event -> {
            //Send on chat?
            ChatUtils.sendMessage((Player) event.getWhoClicked(), "&c&lFinish code!");
        });

        mainGui.setItem(communicatorsSlot, communicatorsItem);

        /*
            ----- ----- -----
                Warps
            ----- ----- -----
        */

        //Filling gui
        mainGui.getFiller().fill(filler);

    }

    public static void openGui(Player player){
        mainGui.open(player);
    }
}
