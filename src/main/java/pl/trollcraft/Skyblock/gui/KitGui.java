package pl.trollcraft.Skyblock.gui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.kit.KitManager;

public class KitGui {

    private static Gui kitGui;
    private static ButtonController buttonController = Skyblock.getButtonController();
    private static KitManager kitManager = Skyblock.getKitManager();

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("kits.yml", "gui", Skyblock.getInstance());
        kitGui = new Gui(configuration.getInt("rows"), configuration.getString("title"));

        kitGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        //Kit Wyspiarza
        Button islandButton = buttonController.loadButton(configuration, "buttons.island");
        kitGui.setItem(islandButton.getSlot(), ItemBuilder.from(islandButton.getIcon()).asGuiItem(event -> {
            kitManager.giveKit(event.getWhoClicked().getName(), "wyspiarz");

        }));

        //Kit Vip'a
        Button vipButton = buttonController.loadButton(configuration, "buttons.vip");
        kitGui.setItem(vipButton.getSlot(), ItemBuilder.from(vipButton.getIcon()).asGuiItem(event -> {
            kitManager.giveKit(event.getWhoClicked().getName(), "vip");

        }));

        //Kit Svip'a
        Button svipButton = buttonController.loadButton(configuration, "buttons.svip");
        kitGui.setItem(svipButton.getSlot(), ItemBuilder.from(svipButton.getIcon()).asGuiItem(event -> {
            kitManager.giveKit(event.getWhoClicked().getName(), "svip");

        }));

        //Kit gracza
        Button playerButton = buttonController.loadButton(configuration, "buttons.player");
        kitGui.setItem(playerButton.getSlot(), ItemBuilder.from(playerButton.getIcon()).asGuiItem(event -> {
            kitManager.giveKit(event.getWhoClicked().getName(), "gracz");

        }));

        //Powrot
        Button returnButton = buttonController.loadButton(configuration, "buttons.back");
        kitGui.setItem(returnButton.getSlot(), ItemBuilder.from(returnButton.getIcon()).asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        kitGui.getFiller().fill(ItemBuilder.from(ConfigUtils.getItemstack(configuration, "fillItem")).asGuiItem());
    }

    public static void openGui(Player player){
        kitGui.open(player);
    }
}
