package pl.trollcraft.Skyblock;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeListener;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.cmdIslands.CommandManager;
import pl.trollcraft.Skyblock.cmdIslands.Commands;
import pl.trollcraft.Skyblock.commands.DebugCommand;
import pl.trollcraft.Skyblock.commands.IslandCommand;
import pl.trollcraft.Skyblock.configs.Persist;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.listeners.BlockPlaceListener;
import pl.trollcraft.Skyblock.listeners.JoinListener;
import pl.trollcraft.Skyblock.listeners.QuitListener;
import redis.clients.jedis.Jedis;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Jedis jedis;
    private static Gson gson;

    //Commands
    private Commands commands;
    private CommandManager commandManager;
    private Persist persist;
    //Commands

    @Override
    public void onEnable() {
        instance = this;
        jedis = new Jedis();
        gson = new Gson();

        persist = new Persist(Persist.PersistType.YAML);

        //Events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        //BungeeEvents
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());

        //Commands
        getCommand("island").setExecutor(new IslandCommand());
        getCommand("debug").setExecutor(new DebugCommand());

        //Functions
        CreateIsland.getNextIsland();
        BungeeSupport.loadConfiguration();

        loadCommands();
        saveCommands();

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

    //Command Load/save

    public void loadCommands(){

        commands = persist.load(Commands.class);

        commandManager = new CommandManager("test");
        commandManager.registerCommands();
    }
    public void saveCommands() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            if (commands != null) persist.save(commands);
        });
    }

    //Command Load/save





    public static Gson getGson() {
        return gson;
    }

    public static Main getInstance() {
        return instance;
    }

    public static Jedis getJedis() {
        return jedis;
    }

    public Commands getCommands() {
        return commands;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

}
