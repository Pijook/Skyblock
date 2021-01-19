package pl.trollcraft.Skyblock;

import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.Skyblock.listeners.JoinListener;
import pl.trollcraft.Skyblock.listeners.QuitListener;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        //Events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
//Komentarz od Diabla
        //Commands


    }

    @Override
    public void onDisable() {

    }


    /**
     * Loads every component of plugin
     */
    public void loadStuff(){
        //Loads here
    }

    public static Main getInstance() {
        return instance;
    }
}
