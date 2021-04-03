package pl.trollcraft.Skyblock.gui.islandGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.gui.Button;
import pl.trollcraft.Skyblock.gui.ButtonController;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import java.util.ArrayList;
import java.util.List;

public class MembersGui {


    //Gui Settings
    private static String title;
    private static int rows;
    private static ItemStack fillItem;
    private static List<Integer> slots;
    private static Button backButton;

    private static ButtonController buttonController = Skyblock.getButtonController();

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("members.yml", "gui", Skyblock.getInstance());

        rows = configuration.getInt("rows");
        title = configuration.getString("title");
        fillItem = ConfigUtils.getItemstack(configuration, "fillItem");
        slots = configuration.getIntegerList("slots");
        backButton = buttonController.loadButton(configuration, "buttons.back");


    }

    public static void openGui(Player player){
        Gui membersGui = new Gui(rows, title);

        membersGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        SkyblockPlayer skyblockPlayer = Skyblock.getSkyblockPlayerController().getPlayer(player.getName());
        List<String> members = Skyblock.getIslandsController().getIslandById(skyblockPlayer.getIslandOrCoop()).getMembers();

        int index = 0;
        for(String nickname : members){
            membersGui.setItem(slots.get(index), ItemBuilder.from(Material.PLAYER_HEAD).setName(ChatUtils.fixColor("&e&l"+ nickname)).asGuiItem());
            index++;
        }

        membersGui.setItem(backButton.getSlot(), ItemBuilder.from(backButton.getIcon()).asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        membersGui.getFiller().fill(ItemBuilder.from(fillItem).asGuiItem());

        membersGui.open(player);
    }
}
