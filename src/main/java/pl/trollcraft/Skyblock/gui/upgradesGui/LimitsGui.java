package pl.trollcraft.Skyblock.gui.upgradesGui;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.cost.Cost;
import pl.trollcraft.Skyblock.essentials.BuildItem;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Utils;
import pl.trollcraft.Skyblock.gui.Button;
import pl.trollcraft.Skyblock.gui.ButtonController;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.limiter.Limiter;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.worker.WorkerController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LimitsGui {

    private static String title;
    private static int rows;
    private static ItemStack fillItem;
    private static List<Integer> slots;
    private static Button backButton;

    private static ButtonController buttonController = Skyblock.getButtonController();

    private static SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();
    private static IslandsController islandsController = Skyblock.getIslandsController();
    private static WorkerController workerController = Skyblock.getWorkerController();
    private static LimitController limitController = Skyblock.getLimitController();

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("limits.yml", "gui", Skyblock.getInstance());

        title = configuration.getString("title");
        rows = configuration.getInt("rows");
        fillItem = ConfigUtils.getItemstack(configuration, "fillItem");

        slots = configuration.getIntegerList("slots");

        backButton = buttonController.loadButton(configuration, "buttons.back");

    }

    public static void open(Player player){

        Gui gui = new Gui(rows, title);

        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        IslandLimiter islandLimiter = Skyblock.getLimitController().getLimiter(Skyblock.getSkyblockPlayerController().getPlayer(player.getName()).getIslandOrCoop());

        int index = 0;
        for(String type : islandLimiter.getIslandLimiters().keySet()){
            GuiItem guiItem = ItemBuilder.from(createIcon(type, islandLimiter.getLimiter(type))).asGuiItem(event -> {

                Player target = (Player) event.getWhoClicked();

                if(!islandsController.isPlayerOnHisIsland(target)){
                    gui.close(target);
                    ChatUtils.sendMessage(player, "&cMusisz byc na wyspie aby to zrobic!");
                    return;
                }

                UUID islandID = skyblockPlayerController.getPlayer(target.getName()).getIslandOrCoop();

                if(!limitController.canUpgrade(islandID, type, target)){
                    ChatUtils.sendMessage(player, "&cNie spelniasz wymagan na to ulepszenie!");
                    return;
                }

                limitController.upgradeLimiter(islandID, type, target);
                ChatUtils.sendMessage(player, "&aUlepszono!");

            });

            gui.setItem(slots.get(index), guiItem);
            index++;
        }

        gui.setItem(backButton.getSlot(), ItemBuilder.from(backButton.getIcon()).asGuiItem(event -> {
            MainGui.openGui((Player) event.getWhoClicked());
        }));

        gui.getFiller().fill(ItemBuilder.from(fillItem).asGuiItem());
        gui.open(player);
    }

    private static ItemStack createIcon(String type, Limiter limiter){
        ItemStack itemStack;
        if(Utils.isMaterial(type)){
            itemStack = BuildItem.buildItem("", Material.valueOf(type), 1);
        }
        else{
            itemStack = BuildItem.buildItem(type, Material.SKELETON_SKULL, 1);
        }

        Limiter defaultValues = Skyblock.getLimitController().getDefaultValues(type, limiter.getLevel());

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&7Obecny poziom: &e" + limiter.getLevel());
        lore.add("&7Obecna ilosc: &e" + limiter.getCurrentAmount());
        lore.add("");
        lore.add("&7Maksymalna ilosc: &e" + defaultValues.getCurrentAmount());

        Cost cost = limitController.getLimiterCost(limiter.getLevel() + 1, type);
        if(cost != null){
            lore.add("");
            lore.add("&7Level do ulepszenia: &e" + cost.getPlayerLevel());
        }

        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);;
        itemStack.setItemMeta(meta);
        return itemStack;

    }
}
