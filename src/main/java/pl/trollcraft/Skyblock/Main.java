package pl.trollcraft.Skyblock;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeListener;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.cmdIslands.CommandManager;
import pl.trollcraft.Skyblock.cmdIslands.Commands;
import pl.trollcraft.Skyblock.commands.DebugCommand;
import pl.trollcraft.Skyblock.configs.Persist;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.limiter.IslandLimiter;
import pl.trollcraft.Skyblock.listeners.*;
import pl.trollcraft.Skyblock.listeners.customListeners.IslandLoadListener;
import pl.trollcraft.Skyblock.listeners.customListeners.IslandSaveListener;
import pl.trollcraft.Skyblock.listeners.customListeners.PlayerLoadListener;
import pl.trollcraft.Skyblock.listeners.customListeners.PlayerSaveListener;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import redis.clients.jedis.Jedis;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Jedis jedis;
    private static Gson gson;

    private static SkyblockPlayerController skyblockPlayerController;
    private static IslandsController islandsController;
    private static IslandLimiter islandLimiter;

    //Commands
    private Commands commands;
    private CommandManager commandManager;
    private Persist persist;

    @Override
    public void onEnable() {
        instance = this;
        jedis = new Jedis();
        gson = new Gson();

        skyblockPlayerController = new SkyblockPlayerController();
        islandsController = new IslandsController();
        islandLimiter = new IslandLimiter();

        persist = new Persist(Persist.PersistType.YAML);

        //Events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        //Custom Events
        getServer().getPluginManager().registerEvents(new PlayerLoadListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSaveListener(), this);
        getServer().getPluginManager().registerEvents(new IslandLoadListener(), this);
        getServer().getPluginManager().registerEvents(new IslandSaveListener(), this);
        //BungeeEvents
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());

        //Commands
//        getCommand("island").setExecutor(new IslandCommand()); // OLD
        getCommand("debug").setExecutor(new DebugCommand());

        loadStuff();

        islandsController.initTimer();
        islandLimiter.loadDefault();
    }

    @Override
    public void onDisable() {

    }

    /**
     * Loads every component of plugin
     */
    public void loadStuff(){

        Debug.log("&aLoading settings...");
        Settings.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading generator settings...");
        CreateIsland.getNextIsland();
        Debug.log("&aDone!");

        Debug.log("&aLoading bungee configuration...");
        BungeeSupport.loadConfiguration();
        Debug.log("&aDone!");

        Debug.log("&aLoading commands...");
        loadCommands();
        saveCommands();
        Debug.log("&aDone!");
    }


    /**
     * Loads commands
     */
    public void loadCommands(){

        commands = persist.load(Commands.class);

        commandManager = new CommandManager("island");
        commandManager.registerCommands();
    }

    /**
     * Saves commands
     */
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

    public static SkyblockPlayerController getSkyblockPlayerController() {
        return skyblockPlayerController;
    }

    public static IslandsController getIslandsController() {
        return islandsController;
    }

    public static IslandLimiter getIslandLimiter() {
        return islandLimiter;
    }

}
