package pl.trollcraft.Skyblock.gui.islandGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;

public class IslandGui {

    private static Gui islandGui;

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("island.yml", "gui", Skyblock.getInstance());

        islandGui = new Gui(configuration.getInt("rows"), configuration.getString("title"));
        islandGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        //Teleportowanie na wyspe
        islandGui.setItem(13, ItemBuilder.from(Material.WHITE_BED).setName(ChatUtils.fixColor("&b&lTeleportacja na wyspe")).asGuiItem(event -> {
            SkyblockPlayer skyblockPlayer = Skyblock.getSkyblockPlayerController().getPlayer(event.getWhoClicked().getName());
            event.getWhoClicked().teleport(Skyblock.getIslandsController().getIslandById(skyblockPlayer.getIslandOrCoop()).getHome());
            ChatUtils.sendMessage((Player) event.getWhoClicked(), "&aTeleportowanie na wyspe...");
        }));


        //Biomy
        islandGui.setItem(configuration.getInt("buttons.bioms.slot"), ItemBuilder.from(ConfigUtils.getItemstack(configuration, "buttons.bioms.icon")).asGuiItem(event -> {
            //TODO
        }));

        //Granica
        islandGui.setItem(configuration.getInt("buttons.border.slot"), ItemBuilder.from(ConfigUtils.getItemstack(configuration, "buttons.border.icon")).asGuiItem(event -> {
            //TODO
        }));

        //Ulepszenia
        islandGui.setItem(configuration.getInt("buttons.upgrades.slot"), ItemBuilder.from(ConfigUtils.getItemstack(configuration, "buttons.upgrades.icon")).asGuiItem(event -> {
            //TODO
        }));

        //Pogoda
        islandGui.setItem(configuration.getInt("buttons.weather.slot"), ItemBuilder.from(ConfigUtils.getItemstack(configuration, "buttons.weather.icon")).asGuiItem(event -> {
            //TODO
        }));

        //Czlonkowie
        islandGui.setItem(configuration.getInt("buttons.members.slot"), ItemBuilder.from(ConfigUtils.getItemstack(configuration, "buttons.members.icon")).asGuiItem(event -> {
            MembersGui.openGui((Player) event.getWhoClicked());
        }));

        //Powrot
        islandGui.setItem(configuration.getInt("buttons.back.slot"), ItemBuilder.from(ConfigUtils.getItemstack(configuration, "buttons.back.icon")).asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        islandGui.getFiller().fill(ItemBuilder.from(ConfigUtils.getItemstack(configuration, "fillItem")).asGuiItem());

    }

    public static void openGui(Player player){
        islandGui.open(player);
    }
}
