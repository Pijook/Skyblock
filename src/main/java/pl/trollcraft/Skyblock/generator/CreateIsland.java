package pl.trollcraft.Skyblock.generator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.IslandsController;
import pl.trollcraft.Skyblock.listeners.customListeners.PlayerLoadListener;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayerController;

import javax.activation.MailcapCommandMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CreateIsland {

    private static final IslandsController islandsController = Skyblock.getIslandsController();
    private static final SkyblockPlayerController skyblockPlayerController = Skyblock.getSkyblockPlayerController();

    private static String schemFile = "island.schem";
    private static String world;
    private static double x;
    private static double y;
    private static double z;
    private static int distance;
    private static int startSize;
    private static int maxSize;
    private static int goNext;
    private static int last;
    private static int check;
    private static boolean isAv;
//////////////////////////////////

    private static ArrayList<String> members = new ArrayList<>();
    private static int level = 1;
/////////////////////////////////

    public static File schem = new File(Skyblock.getInstance().getDataFolder() + File.separator + schemFile);
    public static ClipboardFormat format = ClipboardFormats.findByFile(schem);


    /**
     * Loads position of next island from config nextisland.yml
     */
    public static void getNextIsland(){
        YamlConfiguration configuration = ConfigUtils.load("nextisland.yml", Skyblock.getInstance());

        assert configuration != null;

        schemFile = configuration.getString("nextIsland.schemat");


        world = Storage.world;
        x = configuration.getDouble("nextIsland.x");
        y = Storage.height;
        z = configuration.getDouble("nextIsland.z");

        distance = Storage.distance;
        startSize = Storage.startSize;
        maxSize = Storage.maxSize;

        goNext = configuration.getInt("nextIsland.goNext");
        last = configuration.getInt("nextIsland.last");
        check = configuration.getInt("nextIsland.check");

        isAv = configuration.getBoolean("nextIsland.isAv");
    }

    /**
     * Sets next island position to nextisland.yml config
     */
    private static void setNextIsland(){
        YamlConfiguration configuration = ConfigUtils.load("nextisland.yml", Skyblock.getInstance());

        assert configuration != null;


        Storage.world = world;
        configuration.set("nextIsland.x", x);
        Storage.height = y;
        configuration.set("nextIsland.z", z);

        Storage.distance = distance;
        Storage.startSize = startSize;
        Storage.maxSize = maxSize;

        configuration.set("nextIsland.goNext", goNext);
        configuration.set("nextIsland.last", last);
        configuration.set("nextIsland.check", check);

        configuration.set("nextIsland.isAv", isAv);

        ConfigUtils.save(configuration, "nextisland.yml");
    }

    /**
     * Creates new island
     * @param player Owner of island
     */
    public static void createNew(Player player) {
        String owner = player.getName();

        YamlConfiguration freePosistions = ConfigUtils.load("freeislands.yml", Skyblock.getInstance());

        boolean create = true;

        if(freePosistions.getConfigurationSection("free.1") != null ){

            int freeInt = 0;
            for(String free : freePosistions.getConfigurationSection("free").getKeys(false) ){
                freeInt = Integer.parseInt(free);
            }

            x =  freePosistions.getDouble("free." + freeInt + ".x");
            y = freePosistions.getDouble("free." + freeInt + ".y");
            z = freePosistions.getDouble("free." + freeInt + ".z");
            world = freePosistions.getString("free." + freeInt + ".world");

            Location islandLocation = new Location(Bukkit.getWorld(world), x, y, z);

            freePosistions.set("free." + freeInt, null);
            ConfigUtils.save(freePosistions, "freeislands.yml");
            Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    addIsland(owner, islandLocation);
                }
            },5L);
            create = false;
        }

        if(create) {
            getNextIsland();
            Location islandLocation = new Location(Bukkit.getWorld(world), x, y, z);
            if (last == 4) {
                if (check == goNext) {
                    last = 1;
                    check = 1;
                } else {
                    check++;
                }
                moveUp(owner, islandLocation);
            } else if (last == 1) {
                if (check == goNext) {
                    last = 2;
                    check = 1;
                    goNext++;
                } else {
                    check++;
                }
                moveLeft(owner, islandLocation);
            } else if (last == 2) {
                if (check == goNext) {
                    last = 3;
                    check = 1;
                } else {
                    check++;
                }
                moveDown(owner, islandLocation);
            } else if (last == 3) {
                if (check == goNext) {
                    last = 4;
                    check = 1;
                    goNext++;
                } else {
                    check++;
                }
                moveRight(owner, islandLocation);
            }
            setNextIsland();
        }
    }

    /**
     * ?
     * @param owner
     */
    public static void moveUp(String owner, Location location){
        addIsland(owner, location);
        z += maxSize + distance + 1;
    }

    /**
     * ?
     * @param owner
     */
    public static void moveLeft(String owner, Location location){
        addIsland(owner, location);
        x += maxSize + distance + 1;
    }

    /**
     * ?
     * @param owner
     */
    public static void moveDown(String owner, Location location){
        addIsland(owner, location);
        z -= maxSize + distance + 1;
    }

    /**
     * ?
     * @param owner
     */
    public static void moveRight(String owner, Location location){
        addIsland(owner, location);
        x -= maxSize + distance + 1;
    }


    /**
     * Adds new island to memory and sets owner of island
     * @param owner Nickname of owner
     */
    public static void addIsland(String owner, Location location){
        Debug.log("Setting island!");
        UUID islandID = UUID.randomUUID();

        //Wklejanie vvvvvvvvvvvvvvvv
        pasteIsland(format, schem, location);

        Location islandCenter = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        //Bukkit.getPlayer(owner).teleport(newLoc);
        /*
        new BukkitRunnable(){

            @Override
            public void run() {
                skyblockPlayerController.getPlayer(owner).setIslandID(islandID);

                Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(owner).teleport(islandCenter);
                    }
                });

                Debug.log("&aFinished creating island!");
            }
        }.runTaskLaterAsynchronously(Skyblock.getInstance(), 20L);*/

        Player test = Bukkit.getPlayer(owner);

        if(test == null || !test.isOnline()){
            Debug.sendError("&cGRACZ JEST OFFLINE");
        }

        skyblockPlayerController.getPlayer(owner).setIslandID(islandID);
        skyblockPlayerController.getPlayer(owner).setOnIsland(true);
        Debug.log(skyblockPlayerController.getPlayer(owner).getIslandOrCoop());

        /*Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getPlayer(owner).teleport(islandCenter);
            }
        });*/

        test.teleport(islandCenter);

        Debug.log("&aFinished creating island!");
        //RedisSupport.saveIsland(test, islandID);

        Location point1 = new Location(location.getWorld() , location.getX() - ((double)startSize/2), 0, location.getZ() - ((double)startSize/2));
        Location point2 = new Location(location.getWorld() , location.getX() + ((double)startSize/2), 255, location.getZ() + ((double)startSize/2));

        point1.getBlock().setType(Material.RED_WOOL);
        point2.getBlock().setType(Material.RED_WOOL);


        islandsController.addIsland(islandID, new Island(owner, members, islandCenter, islandCenter, level, point1, point2, Storage.serverName), Bukkit.getPlayer(owner));
        Skyblock.getLimitController().createNewLimiter(islandID);
    }

    /**
     * Pastes island
     * @param format Format
     * @param schemat Schematic file
     */
    public static void pasteIsland(ClipboardFormat format, File schemat, Location location){
        ClipboardReader reader;
        Clipboard clipboard;
        try {
            reader = format.getReader(new FileInputStream(schemat));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(location.getWorld());

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1);

            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY() -2, location.getZ())).ignoreAirBlocks(true).build();

            try {
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) {
                Debug.log(ChatUtils.fixColor("" + "Sth gone wrong"));
                e.printStackTrace();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
