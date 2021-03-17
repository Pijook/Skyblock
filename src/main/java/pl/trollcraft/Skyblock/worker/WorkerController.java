package pl.trollcraft.Skyblock.worker;

import it.unimi.dsi.fastutil.Hash;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.essentials.*;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkerController {

    private ArrayList<Material> blocksToMine = new ArrayList<>();
    private ArrayList<EntityType> entitiesToHunt = new ArrayList<>();
    private ArrayList<Material> woodToChop = new ArrayList<>();

    private HashMap<String, HashMap<Integer, Integer>> worksLevels = new HashMap<>();

    private HashMap<String, Worker> workers = new HashMap<>();

    public void loadSettings(){
        YamlConfiguration configuration = ConfigUtils.load("jobsconfig.yml", Skyblock.getInstance());

        for(String mobName : configuration.getStringList("animalsToHunt")){
            if(!Utils.isMob(mobName)){
                entitiesToHunt.add(EntityType.valueOf(mobName));
            }
            else{
                Debug.sendError("&cWrong entity name: " + mobName);
            }
        }

        for(String blockName : configuration.getStringList("blocksToMine")){
            if(Utils.isMaterial(blockName)){
                blocksToMine.add(Material.valueOf(blockName));
            }
            else{
                Debug.sendError("&cWrong block name: " + blockName);
            }
        }

        for(String blockName : configuration.getStringList("woodToChop")){
            if(Utils.isMaterial(blockName)){
                woodToChop.add(Material.valueOf(blockName));
            }
            else{
                Debug.sendError("&cWrong block name: " + blockName);
            }
        }

        HashMap<Integer, Integer> hunterLevels = new HashMap<>();
        int score = 0;
        for(String levelString : configuration.getConfigurationSection("hunter").getKeys(false)){
            score = configuration.getInt("hunter." + levelString + ".score");
            hunterLevels.put(Integer.parseInt(levelString), score);
        }

        HashMap<Integer, Integer> minerLevels = new HashMap<>();
        for(String levelString : configuration.getConfigurationSection("miner").getKeys(false)){
            score = configuration.getInt("miner." + levelString + ".score");
            minerLevels.put(Integer.parseInt(levelString), score);
        }

        HashMap<Integer, Integer> lumberJackLevels = new HashMap<>();
        for(String levelString : configuration.getConfigurationSection("lumberjack").getKeys(false)){
            score = configuration.getInt("lumberjack." + levelString + ".score");
            lumberJackLevels.put(Integer.parseInt(levelString), score);
        }

        worksLevels.put("hunter", hunterLevels);
        worksLevels.put("miner", minerLevels);
        worksLevels.put("lumberjack", lumberJackLevels);

    }

    //Getting players and adding
    public Worker getWorkerByName(String nickname){
        if(workers.containsKey(nickname)){
            return workers.get(nickname);
        }

        return null;
    }

    public void addWorker(String nickname, Worker worker){
        workers.put(nickname, worker);
    }

    public void removeWorker(String nickname){
        if(workers.containsKey(nickname)){
            workers.remove(nickname);
        }
    }


    /**
     * Loads player worker stats from redis
     * @param player Player to load
     */
    public void loadPlayer(Player player){
        ChatUtils.sendMessage(player, "&aLoading work...");

        new BukkitRunnable(){
            @Override
            public void run() {

                String nickname = player.getName();
                String code = Storage.redisWorkerCode;
                code = code.replace("%player%", nickname);

                String workerJSON = Skyblock.getJedis().hget(code, "worker");

                Debug.log("Worker JSON: " + workerJSON);

                Worker worker = RedisSupport.stringToWorker(workerJSON);

                addWorker(player.getName(), worker);

                ChatUtils.sendSyncMessage(player, "&aLoaded work!");

            }
        }.runTaskLaterAsynchronously(Skyblock.getInstance(), 3L);
    }

    /**
     * Saves player worker stats to redis
     * @param player Player to save
     */
    public void savePlayer(Player player){

        String nickname = player.getName();
        String code = Storage.redisWorkerCode;
        code = code.replace("%placer%", nickname);

        String workerJSON = RedisSupport.workerToString(getWorkerByName(nickname));

        Skyblock.getJedis().hset(code, "worker", workerJSON);

        removeWorker(nickname);
    }

    public boolean isMobToHunt(EntityType entityType){
        return entitiesToHunt.contains(entityType);
    }

    public boolean isWoodToChop(Material material){
        return woodToChop.contains(material);
    }

    public boolean isBlockToMine(Material material){
        return blocksToMine.contains(material);
    }

    public boolean canLevelUp(String nickname, String job){
        Worker worker = getWorkerByName(nickname);

        int currentLevel = worker.getJobLevel(job);

        if(!worksLevels.get(job).containsKey(currentLevel + 1)){
            return false;
        }

        int toHave = worksLevels.get(job).get(currentLevel + 1);

        return worker.getJobScore(job) >= toHave;
    }

    public void levelUpJob(String nickname, String job){
        getWorkerByName(nickname).increaseJobLevel(job, 1);
    }

    public int getNextLevelRequirement(Player player, String job){
        Worker worker = getWorkerByName(player.getName());
        int currentLevel = worker.getJobLevel(job);
        return worksLevels.get(job).get(currentLevel + 1);
    }

    public void showGUIToPlayer(Player player){
        Worker worker = getWorkerByName(player.getName());

        Gui gui = new Gui(3, "Prace");

        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("&7Poziom: " + worker.getJobLevel("miner"));
        itemLore.add("&7Wynik: " + worker.getJobScore("miner"));
        ItemStack minerIcon = BuildItem.buildItem("&7Gornik", Material.IRON_PICKAXE, 1, itemLore);

        itemLore = new ArrayList<>();
        itemLore.add("&7Poziom: " + worker.getJobScore("lumberjack"));
        itemLore.add("&7Wynik: " + worker.getJobScore("lumberjack"));
        ItemStack lumberjackIcon = BuildItem.buildItem("&eDrwal", Material.STONE_AXE, 1, itemLore);

        itemLore = new ArrayList<>();
        itemLore.add("&7Poziom: " + worker.getJobScore("farmer"));
        itemLore.add("&7Wynik: " + worker.getJobScore("farmer"));
        ItemStack farmerIcon = BuildItem.buildItem("&dFarmer", Material.PIG_SPAWN_EGG, 1, itemLore);

        itemLore = new ArrayList<>();
        itemLore.add("&7Poziom: " + worker.getJobScore("hunter"));
        itemLore.add("&7Wynik: " + worker.getJobScore("hunter"));
        ItemStack hunterIcon = BuildItem.buildItem("&cLowca", Material.BOW, 1, itemLore);

        GuiItem miner = ItemBuilder.from(minerIcon).asGuiItem();
        GuiItem lumberJack = ItemBuilder.from(lumberjackIcon).asGuiItem();
        GuiItem farmer = ItemBuilder.from(farmerIcon).asGuiItem();
        GuiItem hunter = ItemBuilder.from(hunterIcon).asGuiItem();

        GuiItem filler = ItemBuilder.from(Material.GRAY_STAINED_GLASS).asGuiItem();
        /*GuiItem miner = ItemBuilder.from(Material.IRON_PICKAXE).
                setName(ChatUtils.fixColor("&7Gornik")).
                setLore("&7Poziom: " + worker.getJobLevel("miner"), ).asGuiItem();
        GuiItem lumberJack = ItemBuilder.from(Material.STONE_AXE).
                setName(ChatUtils.fixColor("&eDrwal")).
                setLore("&7Poziom: " + worker.getJobScore("lumberjack"), "&7Wynik: " + worker.getJobScore("lumberjack")).asGuiItem();
        GuiItem farmer = ItemBuilder.from(Material.PIG_SPAWN_EGG).
                setName(ChatUtils.fixColor("&dFarmer")).
                setLore("&7Poziom: " + worker.getJobScore("farmer"), "&7Wynik: " + worker.getJobScore("farmer")).asGuiItem();
        GuiItem hunter = ItemBuilder.from(Material.BOW).
                setName(ChatUtils.fixColor("&cLowca")).
                setLore("&7Poziom: " + worker.getJobScore("hunter"), "&7Wynik: " + worker.getJobScore("hunter")).asGuiItem();*/

        gui.getFiller().fill(filler);
        gui.setItem(11, miner);
        gui.setItem(12, lumberJack);
        gui.setItem(14, farmer);
        gui.setItem(15, hunter);

        gui.open(player);
    }

    public void debugWorker(String nickname){
        Worker worker = getWorkerByName(nickname);
        Debug.log(Skyblock.getGson().toJson(worker));
    }
}
