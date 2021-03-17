package pl.trollcraft.Skyblock;

import com.google.gson.Gson;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeListener;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.cmdIslands.CommandManager;
import pl.trollcraft.Skyblock.cmdIslands.Commands;
import pl.trollcraft.Skyblock.commands.DebugCommand;
import pl.trollcraft.Skyblock.commands.SpawnCommand;
import pl.trollcraft.Skyblock.commands.TestIslandCommand;
import pl.trollcraft.Skyblock.commands.WorkCommand;
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
import pl.trollcraft.Skyblock.worker.WorkerController;
import redis.clients.jedis.Jedis;

public class Skyblock extends JavaPlugin {

    private static Skyblock instance;

    public static Plugin plugin;
    private static Jedis jedis;
    private static Gson gson;

    private static SkyblockPlayerController skyblockPlayerController;
    private static IslandsController islandsController;
    private static IslandLimiter islandLimiter;
    private static WorkerController workerController;

    //Commands
    private Commands commands;
    private CommandManager commandManager;
    private Persist persist;

    @Override
    public void onEnable() {

        plugin = this;
        instance = this;
        jedis = new Jedis();
        gson = new Gson();


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            //getLogger().warn("Could not find PlaceholderAPI! This plugin is required.");
            Debug.sendError("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        skyblockPlayerController = new SkyblockPlayerController();
        islandsController = new IslandsController();
        islandLimiter = new IslandLimiter();
        workerController = new WorkerController();

        persist = new Persist(Persist.PersistType.YAML);

        //Events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        //Custom Events
        getServer().getPluginManager().registerEvents(new PlayerLoadListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSaveListener(), this);
        getServer().getPluginManager().registerEvents(new IslandLoadListener(), this);
        getServer().getPluginManager().registerEvents(new IslandSaveListener(), this);
        //BungeeEvents
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
        //Placeholders
        PlaceholderAPI.registerPlaceholderHook(this, new Placeholders());

        //Commands
//        getCommand("island").setExecutor(new IslandCommand()); // OLD
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("testisland").setExecutor(new TestIslandCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("worker").setExecutor(new WorkCommand());

        loadStuff();

        islandsController.initTimer();
        //islandLimiter.loadDefault();
        skyblockPlayerController.initCheckingPlayers();
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

        Debug.log("&aLoading worker settings...");
        workerController.loadSettings();
        Debug.log("&aDone!");

        Debug.log("&aFinished loading Skyblock v1.0!");
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

    public static Skyblock getInstance() {
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

    public static WorkerController getWorkerController() {
        return workerController;
    }

}
