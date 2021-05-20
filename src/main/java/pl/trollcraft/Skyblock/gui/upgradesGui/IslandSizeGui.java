package pl.trollcraft.Skyblock.gui.upgradesGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.cost.Cost;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.Button;
import pl.trollcraft.Skyblock.gui.ButtonController;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.WorkerController;

import java.util.ArrayList;
import java.util.List;

public class IslandSizeGui {

    private static ButtonController buttonController = Skyblock.getButtonController();
    private static IslandsController islandsController = Skyblock.getIslandsController();
    private static WorkerController workerController = Skyblock.getWorkerController();
    private static SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

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

        gui.setItem(upgradeButton.getSlot(), ItemBuilder.from(createUpgradeButton(player)).asGuiItem(event -> {
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

    private static ItemStack createUpgradeButton(Player player){
        ItemStack upgradeIcon = upgradeButton.getIcon();

        Island island = islandsController.getIslandById(skyblockPlayerController.getPlayer(player.getName()).getIslandOrCoop());

        Cost cost = islandsController.getIslandUpgradeCost(island.getIslandLevel() + 1);

        List<String> lore = new ArrayList<>();

        if(cost != null){
            lore.add("");
            lore.add(ChatUtils.fixColor("&7Wymagany Poziom: &e" + cost.getPlayerLevel()));
            lore.add("");
            lore.add(ChatUtils.fixColor("&7Koszt: &e" + cost.getMoney()));
            lore.add("");
        }
        else{
            lore.add("");
            lore.add(ChatUtils.fixColor("&cPosiadasz maksymalny rozmiar!"));
            lore.add("");
        }

        ItemMeta meta = upgradeIcon.getItemMeta();
        meta.setLore(lore);
        upgradeIcon.setItemMeta(meta);
        return upgradeIcon;
    }
}
