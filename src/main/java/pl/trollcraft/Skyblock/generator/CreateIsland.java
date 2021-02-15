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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Skyblock.Main;
import pl.trollcraft.Skyblock.essentials.ChatUtils;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.island.Islands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CreateIsland {

    private static String schemFile = "island.schem";
    private static int id;
    private static String world;
    private static double x;
    private static double y;
    private static double z;
    private static int distance;
    private static int maxSize;
    private static int goNext;
    private static int last;
    private static int check;
    private static boolean isAv;
//////////////////////////////////

    private static String owner = "";
    private static ArrayList<String> members = new ArrayList<>();
    private static int level = 1;
/////////////////////////////////

    public static File schem = new File(Main.getInstance().getDataFolder() + File.separator + schemFile);
    public static ClipboardFormat format = ClipboardFormats.findByFile(schem);


    public static void getNextIsland(){
        YamlConfiguration configuration = ConfigUtils.load("nextisland.yml", Main.getInstance());

        assert configuration != null;

        schemFile = configuration.getString("nextIsland.schemat");

        id = configuration.getInt("nextIsland.id");

        world = configuration.getString("nextIsland.world");
        x = configuration.getDouble("nextIsland.x");
        y = configuration.getDouble("nextIsland.y");
        z = configuration.getDouble("nextIsland.z");

        distance = configuration.getInt("nextIsland.distance");
        maxSize = configuration.getInt("nextIsland.maxSize");

        goNext = configuration.getInt("nextIsland.goNext");
        last = configuration.getInt("nextIsland.last");
        check = configuration.getInt("nextIsland.check");

        isAv = configuration.getBoolean("nextIsland.isAv");
    }

    private static void setNextIsland(){
        YamlConfiguration configuration = ConfigUtils.load("nextisland.yml", Main.getInstance());

        assert configuration != null;

        configuration.set("nextIsland.id", id);

        configuration.set("nextIsland.world", world);
        configuration.set("nextIsland.x", x);
        configuration.set("nextIsland.y", y);
        configuration.set("nextIsland.z", z);

        configuration.set("nextIsland.distance", distance);
        configuration.set("nextIsland.maxSize", maxSize);

        configuration.set("nextIsland.goNext", goNext);
        configuration.set("nextIsland.last", last);
        configuration.set("nextIsland.check", check);

        configuration.set("nextIsland.isAv", isAv);

        ConfigUtils.save(configuration, "nextisland.yml");
    }



    public static void createNew(Player player) {
        getNextIsland();
        owner = player.getName();

        YamlConfiguration freePosistions = ConfigUtils.load("freeislands.yml", Main.getInstance());

        boolean create = true;

        if( freePosistions.getConfigurationSection("free.1") != null ){

            int freeInt = 0;
            for( String free : freePosistions.getConfigurationSection("free").getKeys(false) ){
                freeInt = Integer.parseInt(free);
            }

            x =  freePosistions.getDouble("free." + freeInt + ".x");
            y = freePosistions.getDouble("free." + freeInt + ".y");
            z = freePosistions.getDouble("free." + freeInt + ".z");
            world = freePosistions.getString("free." + freeInt + ".world");

            freePosistions.set("free." + freeInt, null);
            ConfigUtils.save(freePosistions, "freeislands.yml");
            id++;
            addIsland();
            create = false;
        }

        if( create) {
            if (last == 4) {
                if (check == goNext) {
                    last = 1;
                    check = 1;
                } else {
                    check++;
                }
                moveUp();
            } else if (last == 1) {
                if (check == goNext) {
                    last = 2;
                    check = 1;
                    goNext++;
                } else {
                    check++;
                }
                moveLeft();
            } else if (last == 2) {
                if (check == goNext) {
                    last = 3;
                    check = 1;
                } else {
                    check++;
                }
                moveDown();
            } else if (last == 3) {
                if (check == goNext) {
                    last = 4;
                    check = 1;
                    goNext++;
                } else {
                    check++;
                }
                moveRight();
            }
            setNextIsland();
        }
    }

    public static void moveUp(){
        addIsland();
        id++;
        z += maxSize + distance + 1;
    }
    public static void moveLeft(){
        addIsland();
        id++;
        x += maxSize + distance + 1;
    }
    public static void moveDown(){
        addIsland();
        id++;
        z -= maxSize + distance + 1;
    }
    public static void moveRight(){
        addIsland();
        id++;
        x -= maxSize + distance + 1;
    }


    public static void addIsland(){

        YamlConfiguration configuration = ConfigUtils.load("islands.yml", Main.getInstance());

        //Wklejanie vvvvvvvvvvvvvvvv
        pasteIsland(format, schem);
        //Wklejanie ^^^^^^^^^^^^^^^^

//        Bukkit.getWorld("" + world).getPlayers().forEach(p -> p.sendMessage(ChatUtils.fixColor("&a&lStworzylem wyspe na koordach: " + x + ", " +  y + ", " +  z)));
//
        Location tpNew = new Location(Bukkit.getWorld("" + world), x, y, z);
        Bukkit.getPlayer(owner).teleport(tpNew);
        configuration.set("islands." + owner + ".owner", owner);
        configuration.set("islands." + owner + ".members", members);
        configuration.set("islands." + owner + ".level", level);
        configuration.set("islands." + owner + ".spawn.x", x);
        configuration.set("islands." + owner + ".spawn.y", y);
        configuration.set("islands." + owner + ".spawn.z", z);
        configuration.set("islands." + owner + ".spawn.world", world);
        configuration.set("islands." + owner + ".center.x", x);
        configuration.set("islands." + owner + ".center.y", y);
        configuration.set("islands." + owner + ".center.z", z);
        configuration.set("islands." + owner + ".center.world", world);

        ConfigUtils.save(configuration, "islands.yml");
    }

    public static void pasteIsland(ClipboardFormat format, File schemat){
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
        try { //Pasting Operation
            // We need to adapt our world into a format that worldedit accepts. This looks like this:
            // Ensure it is using com.sk89q... otherwise we'll just be adapting a world into the same world.
                com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(Bukkit.getWorld("" + world));

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld,
                    -1);

// Saves our operation and builds the paste - ready to be completed.
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(x, y, z)).ignoreAirBlocks(true).build();

            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushSession();

            } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                Debug.log(ChatUtils.fixColor("" + "Sth gone wrong"));
                e.printStackTrace();
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
//Testowe dodawanie memberow
//    public static void addMember(String ownr, String mbr){
//        YamlConfiguration configuration = ConfigUtils.load("islands.yml", Main.getInstance());
//        ArrayList<String> membr = (ArrayList<String>) configuration.getStringList("islands." + ownr + ".members");
//        membr.add(mbr);
//        configuration.set("islands." + ownr + ".members", membr);
//        ConfigUtils.save(configuration, "islands.yml");
//    }
}
