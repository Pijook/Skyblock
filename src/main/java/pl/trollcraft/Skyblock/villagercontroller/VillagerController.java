package pl.trollcraft.Skyblock.villagercontroller;

import javafx.scene.input.InputMethodTextRun;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.Skyblock.Skyblock;
import pl.trollcraft.Skyblock.essentials.ConfigUtils;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class VillagerController {

    private HashMap<Villager.Profession, HashMap<Integer, ArrayList<Trade>>> possibleTrades = new HashMap<>();

    public void load(){
        possibleTrades.clear();
        YamlConfiguration configuration = ConfigUtils.load("trades.yml", Skyblock.getInstance());

        for(String professionName : configuration.getConfigurationSection("trades").getKeys(false)){

            if(!Utils.isVillagerProfession(professionName)){
                Debug.sendError("&cWrong profession name! (trades." + professionName + ")");
                continue;
            }

            HashMap<Integer, ArrayList<Trade>> levelTrades = new HashMap<>();

            for(String tradeLevel : configuration.getConfigurationSection("trades." + professionName).getKeys(false)){

                ArrayList<Trade> trades = new ArrayList<>();

                for(String tradeName : configuration.getConfigurationSection("trades." + professionName + "." + tradeLevel).getKeys(false)){

                    List<Element> ingredients = new ArrayList<>();

                    for(String ingredientName : configuration.getConfigurationSection("trades." + professionName + "." + tradeLevel + "." + tradeName + ".ingredients").getKeys(false)){
                        ingredients.add(buildElement(configuration, "trades." + professionName + "." + tradeLevel + "." + tradeName + ".ingredients" + "." + ingredientName));
                    }

                    Element result = buildElement(configuration, "trades." + professionName + "." + tradeLevel + "." + tradeName + ".result");

                    trades.add(new Trade(ingredients, result));

                }

                levelTrades.put(Integer.parseInt(tradeLevel), trades);
            }

            possibleTrades.put(Villager.Profession.valueOf(professionName), levelTrades);
        }

    }

    public void customizeVillager(Villager villager){

        if(!possibleTrades.containsKey(villager.getProfession())){
            return;
        }

        HashMap<Integer, ArrayList<Trade>> tempTrades = possibleTrades.get(villager.getProfession());

        Random random = new Random();

        List<MerchantRecipe> trades = new ArrayList<>();

        for(int tradeLevel : tempTrades.keySet()){
            //Trade trade = tempTrades.get(tradeLevel).get(tempTrades.get(tradeLevel).size());
            Trade trade = tempTrades.get(tradeLevel).get(random.nextInt(tempTrades.get(tradeLevel).size()));
            MerchantRecipe recipe = new MerchantRecipe(trade.constructResult(random), 0, 999, false);
            for(ItemStack itemStack : trade.constructIngredients(random)){
                recipe.addIngredient(itemStack);
            }
            trades.add(recipe);
        }

        villager.setRecipes(trades);

    }

    public Element buildElement(YamlConfiguration configuration, String path){
        String material = configuration.getString(path + ".material");
        int min = configuration.getInt(path + ".minAmount");
        int max = configuration.getInt(path + ".maxAmount");

        ItemStack itemStack = new ItemStack(Material.valueOf(material), 1);
        return new Element(itemStack, min, max);
    }
}
