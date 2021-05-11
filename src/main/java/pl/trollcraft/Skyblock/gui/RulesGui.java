package pl.trollcraft.Skyblock.gui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.BuildItem;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import scala.Int;

import java.util.List;

public class RulesGui {

    private static final ButtonController buttonController = Skyblock.getButtonController();

    private static PaginatedGui rulesGui;

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("rules.yml", "gui", Skyblock.getInstance());

        GuiItem fillItem = ItemBuilder.from(ConfigUtils.getItemstack(configuration, "fillItem")).asGuiItem();
        List<Integer> filledSlots = configuration.getIntegerList("filledSlots");

        Button nextButton = buttonController.loadButton(configuration, "nextPage");
        Button previousButton = buttonController.loadButton(configuration, "previousPage");
        Button returnButton = buttonController.loadButton(configuration, "buttons.back");

        String ruleFormat = configuration.getString("ruleFormat");
        List<String> rules = configuration.getStringList("rules");
        Material ruleMaterial = Material.valueOf(configuration.getString("ruleMaterial"));

        rulesGui = new PaginatedGui(configuration.getInt("rows"), configuration.getString("title"));

        rulesGui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        for(int slot : filledSlots){
            rulesGui.setItem(slot, fillItem);
        }

        rulesGui.setItem(returnButton.getSlot(), ItemBuilder.from(returnButton.getIcon()).asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        rulesGui.setItem(nextButton.getSlot(), ItemBuilder.from(nextButton.getIcon()).asGuiItem(event -> {
            rulesGui.next();
        }));

        rulesGui.setItem(previousButton.getSlot(), ItemBuilder.from(previousButton.getIcon()).asGuiItem(event -> {
            rulesGui.previous();
        }));


        int ruleNumber = 1;
        for(String rule : rules){
            String name = ruleFormat;
            name = name.replace("%number%", "" + ruleNumber);
            rulesGui.addItem(ItemBuilder.from(BuildItem.buildItem(name, ruleMaterial, 1, rule)).asGuiItem());
            ruleNumber++;
        }

    }

    public static void openGui(Player player){
        rulesGui.open(player);
    }

}
