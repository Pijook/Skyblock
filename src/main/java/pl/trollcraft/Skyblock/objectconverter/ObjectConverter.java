package pl.trollcraft.Skyblock.objectconverter;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.island.bungeeIsland.BungeeIsland;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.worker.Worker;
import scala.Int;

import java.util.*;

public class ObjectConverter {

    public String IslandToString(Island island){
        //Owner
        //Members
        //Center
        //Spawn
        //IslandLevel
        //Point1
        //Point2
        //Server
        //Points
        String a = "";
        a = a + island.getOwner();
        a = a + ";" + listToString(island.getMembers());
        a = a + ";" + locationToString(island.getCenter());
        a = a + ";" + locationToString(island.getHome());
        a = a + ";" + island.getIslandLevel();
        a = a + ";" + locationToString(island.getPoint1());
        a = a + ";" + locationToString(island.getPoint2());
        a = a + ";" + island.getServer();
        a = a + ";" + island.getPoints();
        return a;
    }

    public Island stringToIsland(String islandJSON){
        String[] a = islandJSON.split(";");

        return new Island(
                a[0],
                stringToList(a[1]),
                stringToLocation(a[2]),
                stringToLocation(a[3]),
                Integer.parseInt(a[4]),
                stringToLocation(a[5]),
                stringToLocation(a[6]),
                a[7],
                Integer.parseInt(a[8])
        );
    }

    public String playerToString(SkyblockPlayer skyblockPlayer){
        //Island id
        //CoopIslandID
        //Invites
        //PlacedBlocks
        //DropLevel
        String a = "";
        if(skyblockPlayer.getIslandID() == null){
            a = a + "";
        }
        else{
            a = a + "" + skyblockPlayer.getIslandID().toString();
        }
        if(skyblockPlayer.getCoopIslandID() == null){
            a = a + ";";
        }
        else{
            a = a + ";" + skyblockPlayer.getCoopIslandID().toString();
        }
        a = a + ";" + listToString(skyblockPlayer.getInvites());
        a = a + ";" + skyblockPlayer.getPlacedBlocks();
        a = a + ";" + skyblockPlayer.getBrokenBlocks();
        a = a + ";" + skyblockPlayer.getBrokenCobble();
        a = a + ";" + skyblockPlayer.getBrokenStone();
        a = a + ";" + skyblockPlayer.getKilledAnimals();
        a = a + ";" + skyblockPlayer.getKills();
        a = a + ";" + skyblockPlayer.getDeaths();
        a = a + ";" + skyblockPlayer.getDropLevel();
        return a;
    }

    public SkyblockPlayer stringToPlayer(String playerJSON){
        String[] a = playerJSON.split(";");
        UUID islandID;

        if(a[0] == null || a[0].equalsIgnoreCase("")){
            islandID = null;
        }
        else{
            islandID = UUID.fromString(a[0]);
        }

        UUID coopID;

        if(a[1] == null || a[1].equalsIgnoreCase("")){
            coopID = null;
        }
        else{
            coopID = UUID.fromString(a[1]);
        }

        ArrayList<String> invites = stringToList(a[2]);
        int placedBlocks = Integer.parseInt(a[3]);
        int brokenBlocks = Integer.parseInt(a[4]);
        int brokenCobble = Integer.parseInt(a[5]);
        int brokenStone = Integer.parseInt(a[6]);
        int killedAnimals = Integer.parseInt(a[7]);
        int kills = Integer.parseInt(a[8]);
        int deaths = Integer.parseInt(a[9]);
        int dropLevel = Integer.parseInt(a[10]);
        return new SkyblockPlayer(islandID, coopID, invites, placedBlocks, brokenBlocks, brokenCobble, brokenStone, killedAnimals, kills, deaths, dropLevel);
    }

    public String workerToString(Worker worker){
        String a = "";
        a = a + worker.getJobLevel("miner");
        a = a + ";" + worker.getJobScore("miner");
        a = a + ";" + worker.getJobLevel("lumberjack");
        a = a + ";" + worker.getJobScore("lumberjack");
        a = a + ";" + worker.getJobLevel("farmer");
        a = a + ";" + worker.getJobScore("farmer");
        a = a + ";" + worker.getJobLevel("breeder");
        a = a + ";" + worker.getJobScore("breeder");
        a = a + ";" + worker.getJobLevel("hunter");
        a = a + ";" + worker.getJobScore("hunter");
        return a;
    }

    public Worker stringToWorker(String workerJSON){
        String[] a = workerJSON.split(";");
        Integer[] values = new Integer[a.length];
        for(int i = 0; i < a.length; i++){
            values[i] = Integer.parseInt(a[i]);
        }
        return new Worker(
                values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                values[5],
                values[6],
                values[7],
                values[8],
                values[9]
        );
    }

    public String bungeeIslandToString(BungeeIsland island){
        String a = "";
        a = a + island.getOwner();
        a = a + ";" + listToString(island.getMembers());
        a = a + ";" + locationToString(island.getCenter());
        a = a + ";" + locationToString(island.getHome());
        a = a + ";" + island.getIslandLevel();
        a = a + ";" + locationToString(island.getPoint1());
        a = a + ";" + locationToString(island.getPoint2());
        a = a + ";" + island.getServer();
        a = a + ";" + island.getPoints();
        return a;
    }

    public BungeeIsland stringToBungeeIsland(String json){
        String[] a = json.split(";");
        return new BungeeIsland(
                a[0],
                stringToList(a[1]),
                stringToBungeeLocation(a[2]),
                stringToBungeeLocation(a[3]),
                Integer.parseInt(a[4]),
                stringToBungeeLocation(a[5]),
                stringToBungeeLocation(a[6]),
                a[7],
                Integer.parseInt(a[8])
        );
    }

    private String locationToString(Location location){
        String a = "";
        a = a + location.getWorld().getName();
        a = a + "!" + location.getX();
        a = a + "!" + location.getY();
        a = a + "!" + location.getZ();
        return a;
    }

    private String locationToString(pl.trollcraft.Skyblock.island.bungeeIsland.Location location){
        String a = "";
        a = a + location.getWorld();
        a = a + "!" + location.getX();
        a = a + "!" + location.getY();
        a = a + "!" + location.getZ();
        return a;
    }

    private Location stringToLocation(String location){
        String[] a = location.split("!");
        return new Location(Bukkit.getWorld(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
    }

    private pl.trollcraft.Skyblock.island.bungeeIsland.Location stringToBungeeLocation(String location){
        String[] a = location.split("!");
        return new pl.trollcraft.Skyblock.island.bungeeIsland.Location(a[0], Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
    }

    private String listToString(List<String> list){
        String a = "";
        if(list == null){
            return a;
        }
        else if(list.size() == 0){
            return a;
        }
        else if(list.size() == 1){
            a = list.get(0);
            return a;
        }
        else{
            a = a + list.get(0);
            for(int i = 1; i < list.size(); i++){
                a = a + ":" + list.get(i);
            }
            return a;
        }
    }

    private ArrayList<String> stringToList(String list){
        if(list == null || list.equalsIgnoreCase("")){
            return new ArrayList<>();
        }
        String[] a = list.split(":");
        ArrayList<String> returnList = new ArrayList<>();
        returnList.addAll(Arrays.asList(a));
        return returnList;
    }

    public String mapToString(Map<String, Integer> map){
        String a = "";

        if(map.size() > 0){
            int i = 0;
            for(String key : map.keySet()){
                if(i != 0){
                    a = a + ";";
                }
                a = a + key + ":" + map.get(key);
                i++;
            }
        }

        return a;
    }

    public Map<String, Integer> stringToMap(String a){
        Map<String, Integer> map = new HashMap<>();

        String[] fragments = a.split(";");

        if(fragments.length > 0){
            for(String fragment : fragments){
                String[] values = fragment.split(":");
                map.put(values[0], Integer.parseInt(values[1]));
            }
        }

        return map;
    }

}
