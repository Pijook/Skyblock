package pl.trollcraft.Skyblock;

import com.google.gson.Gson;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeListener;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.cmdIslands.CommandManager;
import pl.trollcraft.Skyblock.cmdIslands.Commands;
import pl.trollcraft.Skyblock.commands.*;
import pl.trollcraft.Skyblock.configs.Persist;
import pl.trollcraft.Skyblock.dropManager.DropManager;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.generator.CreateIsland;
import pl.trollcraft.Skyblock.gui.ButtonController;
import pl.trollcraft.Skyblock.gui.KitGui;
import pl.trollcraft.Skyblock.gui.MainGui;
import pl.trollcraft.Skyblock.gui.WarpGui;
import pl.trollcraft.Skyblock.gui.islandGui.IslandGui;
import pl.trollcraft.Skyblock.gui.islandGui.MembersGui;
import pl.trollcraft.Skyblock.gui.upgradesGui.DropGui;
import pl.trollcraft.Skyblock.gui.upgradesGui.IslandSizeGui;
import pl.trollcraft.Skyblock.gui.upgradesGui.LimitsGui;
import pl.trollcraft.Skyblock.gui.upgradesGui.UpgradesGui;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.kit.KitManager;
import pl.trollcraft.Skyblock.limiter.LimitController;
import pl.trollcraft.Skyblock.listeners.*;
import pl.trollcraft.Skyblock.listeners.customListeners.*;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;
import pl.trollcraft.Skyblock.villagercontroller.VillagerController;
import pl.trollcraft.Skyblock.worker.WorkerController;
import redis.clients.jedis.Jedis;

public class Skyblock extends JavaPlugin {

    private static Skyblock instance;

    public static Plugin plugin;
    private static Jedis jedis;
    private static Gson gson;

    private static Economy econ = null;

    private static SkyblockPlayerController skyblockPlayerController;
    private static IslandsController islandsController;
    private static LimitController limitController;
    private static WorkerController workerController;
    private static DropManager dropManager;
    private static ButtonController buttonController;
    private static KitManager kitManager;
    private static VillagerController villagerController;

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
            Debug.sendError("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!setupEconomy() ) {
            Debug.log(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        skyblockPlayerController = new SkyblockPlayerController();
        islandsController = new IslandsController();
        limitController = new LimitController();
        workerController = new WorkerController();
        dropManager = new DropManager();
        buttonController = new ButtonController();
        kitManager = new KitManager();
        villagerController = new VillagerController();

        persist = new Persist(Persist.PersistType.YAML);

        //Events
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new IslandSaveListener(), this);
        getServer().getPluginManager().registerEvents(new VillagerChangeClassListener(), this);
        getServer().getPluginManager().registerEvents(new IslandSecurityListener(), this);
        //Custom Events
        getServer().getPluginManager().registerEvents(new PlayerLoadListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerSaveListener(), this);
        getServer().getPluginManager().registerEvents(new IslandLoadListener(), this);
        getServer().getPluginManager().registerEvents(new IslandSaveListener(), this);
        getServer().getPluginManager().registerEvents(new RemoveIslandFromMemoryListener(), this);
        //BungeeEvents
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
        //Placeholders
        PlaceholderAPI.registerPlaceholderHook(this, new Placeholders());

        //Commands
//        getCommand("island").setExecutor(new IslandCommand()); // OLD
        getCommand("debug").setExecutor(new DebugCommand());
//        getCommand("testisland").setExecutor(new TestIslandCommand());
        getCommand("tempspawn").setExecutor(new SpawnCommand());
        getCommand("worker").setExecutor(new WorkCommand());
        getCommand("aworker").setExecutor(new AdminWorkCommand());
        getCommand("menu").setExecutor(new GuiCommand());
        getCommand("vtrades").setExecutor(new TradesCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("checkblock").setExecutor(new CheckBlockCommand());


        loadStuff();
    }

    @Override
    public void onDisable() {

        limitController.saveAllLimiters();

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

        Debug.log("&aLoading island upgrades costs...");
        islandsController.loadLevelsCosts();
        Debug.log("&aDone!");

        Debug.log("&aLoading limits...");
        limitController.loadSettings();
        Debug.log("&aDone!");

        Debug.log("&aStarting island timer...");
        islandsController.initTimer();
        Debug.log("&aDone!");

        Debug.log("&aStarting skyblockPlayers timer...");
        skyblockPlayerController.initCheckingPlayers();
        Debug.log("&aDone!");

        Debug.log("&aLoading dropManager...");
        dropManager.setupGenerator();
        Debug.log("&aDone!");

        Debug.log("&aLoading guis..");
        loadGui();
        Debug.log("&aDone!");

        Debug.log("&aLoading kits...");
        kitManager.loadKits();
        Debug.log("&aDone!");

        Debug.log("&aLoading villager trades...");
        villagerController.load();
        Debug.log("&aDone!");

        Debug.log("&aFinished loading " + getDescription().getName() + " " + getDescription().getVersion() + "!");

    }

    public void loadGui(){

        Debug.log("&aLoading Main gui...");
        MainGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Island gui...");
        IslandGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Members gui...");
        MembersGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Kit gui...");
        KitGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Limits gui...");
        LimitsGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Drop gui...");
        DropGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Upgrades gui...");
        UpgradesGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Warps gui...");
        WarpGui.load();
        Debug.log("&aDone!");

        Debug.log("&aLoading Size gui...");
        IslandSizeGui.load();
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
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

    public static LimitController getLimitController(){
        return limitController;
    }

    public static WorkerController getWorkerController() {
        return workerController;
    }

    public static DropManager getDropManager(){
        return dropManager;
    }

    public static ButtonController getButtonController() {
        return buttonController;
    }

    public static KitManager getKitManager(){
        return kitManager;
    }

    public static Economy getEconomy(){
        return econ;
    }

    public static VillagerController getVillagerController() {
        return villagerController;
    }

}
