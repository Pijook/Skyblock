package pl.trollcraft.Skyblock.gui.upgradesGui;


import me.mattstudios.mfgui.gui.guis.Gui;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class DropGui {

    private static String title;
    private static int rows;
    private static ItemStack fillItem;

    public static void load(){
        YamlConfiguration configuration = ConfigUtils.load("dropGui.yml", "gui", Skyblock.getInstance());

        title = configuration.getString("title");
        rows = configuration.getInt("rows");
        fillItem = ConfigUtils.getItemstack(configuration, "fillItem");

    }

    public static void openGui(Player player){
        Gui gui = new Gui(rows, title);


    }
}
