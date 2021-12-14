package pl.trollcraft.Skyblock.worker;

import it.unimi.dsi.fastutil.Hash;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.Storage;
import pl.trollcraft.Skyblock.bungeeSupport.BungeeSupport;
import pl.trollcraft.Skyblock.essentials.*;
import pl.trollcraft.Skyblock.redisSupport.RedisSupport;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkerController {

    private ArrayList<Material> blocksToMine = new ArrayList<>();
    private ArrayList<EntityType> entitiesToHunt = new ArrayList<>();
    private ArrayList<Material> woodToChop = new ArrayList<>();
    //private ArrayList<Material> cropsToHarvest = new ArrayList<>();
    private HashMap<Material, Byte> cropsToHarvest = new HashMap<>();

    private HashMap<String, HashMap<Integer, Integer>> worksLevels = new HashMap<>();

    private HashMap<String, Worker> workers = new HashMap<>();

    private HashMap<String, Integer> loadTries = new HashMap<>();

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

        for(String blockName : configuration.getStringList("cropsToHarvest")){
            String[] fragments = blockName.split(":");
            if(Utils.isMaterial(fragments[0])){
                cropsToHarvest.put(Material.valueOf(fragments[0]), Byte.parseByte(fragments[1]));
                //cropsToHarvest.add(Material.valueOf(blockName));
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

        HashMap<Integer, Integer> farmerLevels = new HashMap<>();
        for(String levelString : configuration.getConfigurationSection("farmer").getKeys(false)){
            score = configuration.getInt("farmer." + levelString + ".score");
            farmerLevels.put(Integer.parseInt(levelString), score);
        }

        HashMap<Integer, Integer> breederLevels = new HashMap<>();
        for(String levelString : configuration.getConfigurationSection("breeder").getKeys(false)){
            score = configuration.getInt("breeder." + levelString + ".score");
            breederLevels.put(Integer.parseInt(levelString), score);
        }

        worksLevels.put("hunter", hunterLevels);
        worksLevels.put("miner", minerLevels);
        worksLevels.put("lumberjack", lumberJackLevels);
        worksLevels.put("farmer", farmerLevels);
        worksLevels.put("breeder", breederLevels);

    }

    //Getting players and adding
    public Worker getWorkerByName(String nickname){
        if(workers.containsKey(nickname)){
            return workers.get(nickname);
        }

        Debug.sendError("Worker doesn't exist!");
        return null;
    }

    public boolean canLevelUp(Worker worker, String workName){
        int currentLevel = worker.getJobLevel(workName);

        if(!worksLevels.get(workName).containsKey(currentLevel + 1)){
            return false;
        }

        int currentScore = worker.getJobScore(workName);

        if(currentScore >= worksLevels.get(workName).get(currentLevel + 1)){
            return true;
        }

        return false;
    }

    public void levelUpJob(Worker worker, String workName){
        worker.increaseJobLevel(workName, 1);
    }

    public void addWorker(String nickname, Worker worker){
        workers.put(nickname, worker);
    }

    public void removeWorker(String nickname){
        Debug.log("&4Removing worker!");
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

        String nickname = player.getName();
        String code = Storage.redisWorkerCode;
        code = code.replace("%player%", nickname);

        String workerJSON = Skyblock.getJedis().hget(code, "worker");

        if(workerJSON == null){
            BungeeSupport.sendReloadWorkerCommand(player);
            Skyblock.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Skyblock.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if(!loadTries.containsKey(player.getName()) || loadTries.get(player.getName()) < 3){
                        int amount = loadTries.getOrDefault(player.getName(), 0);
                        amount++;
                        loadTries.put(player.getName(), amount);
                        loadPlayer(player);
                    }
                    else{
                        Debug.log("&cGiving up from working " + player.getName() + " worker!");
                    }
                }
            }, 20L);
            return;
        }

        Debug.log("Worker JSON: " + workerJSON);

        Worker worker = RedisSupport.stringToWorker(workerJSON);

        addWorker(player.getName(), worker);

        ChatUtils.sendSyncMessage(player, "&aLoaded work!");

        Debug.log("Worker " + player.getName());
        debugWorker(player.getName());

    }

    /**
     * Saves player worker stats to redis
     * @param player Player to save
     */
    public void savePlayer(Player player){

        String nickname = player.getName();
        String code = Storage.redisWorkerCode;
        code = code.replace("%player%", nickname);

        Worker worker = getWorkerByName(nickname);

        if(worker == null){
            return;
        }

        String workerJSON = RedisSupport.workerToString(worker);
        debugWorker(player.getName());
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

    public boolean isCropsToHarvest(Material material){
        return cropsToHarvest.containsKey(material);
    }

    public boolean isCropFullyGrown(Block block){
        if(!cropsToHarvest.containsKey(block.getType())){
            return false;
        }
        if(block.getData() == cropsToHarvest.get(block.getType())){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean canLevelUp(String nickname, String job){
        Worker worker = getWorkerByName(nickname);

        int currentLevel = worker.getJobLevel(job);

        if(!worksLevels.containsKey(job)){
            return false;
        }
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

        if(!worksLevels.containsKey(job)){
            return -1;
        }
        if(!worksLevels.get(job).containsKey(currentLevel + 1)){
            return -1;
        }

        return worksLevels.get(job).get(currentLevel + 1);
    }

    public void showGUIToPlayer(Player player){
        Worker worker = getWorkerByName(player.getName());

        Gui gui = new Gui(3, "Prace");

        gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
        });

        ArrayList<String> itemLore = new ArrayList<>();
        int toNextLevel;

        itemLore = buildWorkerGuiLore(player, worker, "miner");
        ItemStack minerIcon = BuildItem.buildItem("&7&lGornik", Material.IRON_PICKAXE, 1, itemLore);

        itemLore = buildWorkerGuiLore(player, worker, "lumberjack");
        ItemStack lumberjackIcon = BuildItem.buildItem("&e&lDrwal", Material.STONE_AXE, 1, itemLore);

        itemLore = buildWorkerGuiLore(player, worker, "farmer");
        ItemStack farmerIcon = BuildItem.buildItem("&d&lFarmer", Material.WHEAT, 1, itemLore);

        itemLore = buildWorkerGuiLore(player, worker, "hunter");
        ItemStack hunterIcon = BuildItem.buildItem("&c&lLowca", Material.BOW, 1, itemLore);

        itemLore = buildWorkerGuiLore(player, worker, "breeder");
        ItemStack breederIcon = BuildItem.buildItem("&d&lHodowca", Material.PIG_SPAWN_EGG, 1, itemLore);

        double averageLevel = worker.getAverageLevel();
        ItemStack averageIcon = BuildItem.buildItem("&6&lSrednia: &7" + averageLevel, Material.NETHER_STAR, 1);

        GuiItem miner = ItemBuilder.from(minerIcon).asGuiItem();
        GuiItem lumberJack = ItemBuilder.from(lumberjackIcon).asGuiItem();
        GuiItem farmer = ItemBuilder.from(farmerIcon).asGuiItem();
        GuiItem hunter = ItemBuilder.from(hunterIcon).asGuiItem();
        GuiItem breeder = ItemBuilder.from(breederIcon).asGuiItem();
        GuiItem average = ItemBuilder.from(averageIcon).asGuiItem();


        GuiItem filler = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).asGuiItem();

        gui.getFiller().fill(filler);
        gui.setItem(11, miner);
        gui.setItem(12, lumberJack);
        gui.setItem(13, breeder);
        gui.setItem(14, farmer);
        gui.setItem(15, hunter);
        gui.setItem(4, average);

        gui.open(player);
    }

    private ArrayList<String> buildWorkerGuiLore(Player player, Worker worker, String workName){
        ArrayList<String> itemLore = new ArrayList<>();
        itemLore.add("&7Poziom: " + worker.getJobLevel(workName));
        itemLore.add("&7Wynik: " + worker.getJobScore(workName));

        int toNextLevel;
        toNextLevel = getNextLevelRequirement(player, workName);
        if(toNextLevel != -1){
            toNextLevel =  toNextLevel - worker.getJobScore(workName);
            itemLore.add("&7Do nastepnego poziomu: " + toNextLevel);
        }
        return itemLore;
    }

    public void debugWorker(String nickname){
        Worker worker = getWorkerByName(nickname);
        //Debug.log(Skyblock.getGson().toJson(worker));
        Debug.log(Skyblock.getObjectConverter().workerToString(worker));
    }
}
