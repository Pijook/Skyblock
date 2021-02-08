package pl.trollcraft.Skyblock;

import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeListener;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.commands.HelpCommand;
import pl.trollcraft.Skyblock.commands.IslandCommand;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.listeners.JoinListener;
import pl.trollcraft.Skyblock.listeners.QuitListener;
import redis.clients.jedis.Jedis;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Jedis jedis;

    @Override
    public void onEnable() {
        instance = this;
        jedis = new Jedis();

        //Events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        //BungeeEvents
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());

        //Commands
        getCommand("island").setExecutor(new IslandCommand());

        //Functions
        CreateIsland.getNextIsland();
        BungeeSupport.loadConfiguration();

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

    public static Jedis getJedis() {
        return jedis;
    }
}
