package pl.trollcraft.Skyblock.objectconverter;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.trollcraft.Skyblock.island.Island;
import pl.trollcraft.Skyblock.skyblockplayer.SkyblockPlayer;
import pl.trollcraft.Skyblock.worker.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        a = a + "" + skyblockPlayer.getIslandID().toString();
        a = a + ";" + skyblockPlayer.getCoopIslandID().toString();
        a = a + ";" + listToString(skyblockPlayer.getInvites());
        a = a + ";" + skyblockPlayer.getPlacedBlocks();
        a = a + ";" + skyblockPlayer.getDropLevel();
        return a;
    }

    public SkyblockPlayer stringToPlayer(String playerJSON){
        String[] a = playerJSON.split(";");
        return new SkyblockPlayer(
                UUID.fromString(a[0]),
                UUID.fromString(a[1]),
                stringToList(a[2]),
                Integer.parseInt(a[3]),
                Integer.parseInt(a[4])
        );
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

    private String locationToString(Location location){
        String a = "";
        a = a + location.getWorld().getName();
        a = a + ";" + location.getX();
        a = a + ";" + location.getY();
        a = a + ";" + location.getZ();
        return a;
    }

    private Location stringToLocation(String location){
        String[] a = location.split(";");
        return new Location(Bukkit.getWorld(a[0]), Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
    }

    private String listToString(List<String> list){
        String a = "";
        if(list.size() == 0){
            return a;
        }
        else if(list.size() == 1){
            a = list.get(0);
            return a;
        }
        else{
            a = a + list.get(0);
            for(int i = 1; i < list.size(); i++){
                a = a + ";" + list.get(i);
            }
            return a;
        }
    }

    private ArrayList<String> stringToList(String list){
        if(list == null || list.equalsIgnoreCase("")){
            return new ArrayList<>();
        }
        String[] a = list.split(";");
        ArrayList<String> returnList = new ArrayList<>();
        returnList.addAll(Arrays.asList(a));
        return returnList;
    }
}
