package pl.trollcraft.Skyblock.gui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KitGui {

    private static Gui kitGui;

    public static void load(){

        kitGui = new Gui(5, "Kity");
        kitGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        //Kit Wyspiarza
        kitGui.setItem(13, ItemBuilder.from(Material.TURTLE_HELMET).setName("&7Kit &dWyspiarza").asGuiItem(event -> {

            //TODO give kit

        }));

        //Kit Vip'a
        kitGui.setItem(21, ItemBuilder.from(Material.TURTLE_HELMET).setName("&7Kit &eVIP'a").asGuiItem(event -> {

            //TODO give kit

        }));

        //Kit Svip'a
        kitGui.setItem(23, ItemBuilder.from(Material.TURTLE_HELMET).setName("&7Kit &6SVIP'a").asGuiItem(event -> {

            //TODO give kit

        }));

        //Kit gracza
        kitGui.setItem(31, ItemBuilder.from(Material.TURTLE_HELMET).setName("&7Kit gracza").asGuiItem(event -> {

            //TODO give kit

        }));

        //Powrot
        kitGui.setItem(40, ItemBuilder.from(Material.REDSTONE).setName("&c&lPowrot").asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        kitGui.getFiller().fill(ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem());
    }

    public static void openGui(Player player){
        kitGui.open(player);
    }
}
