package pl.trollcraft.Skyblock.gui;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;

public class ButtonController {

    public Button loadButton(YamlConfiguration configuration, String path){
        int slot = configuration.getInt(path + ".slot");
        ItemStack icon = ConfigUtils.getItemstack(configuration, path + ".icon");

        return new Button(slot, icon);
    }
}
