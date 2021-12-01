package pl.trollcraft.Skyblock.villagercontroller;

import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Skyblock.essentials.Debug;
import pl.trollcraft.Skyblock.essentials.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Trade {

    private List<Element> ingredients;
    private Element result;

    public Trade(List<Element> ingredients, Element result){
        this.ingredients = ingredients;
        this.result = result;
    }

    public List<Element> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Element> ingredients) {
        this.ingredients = ingredients;
    }

    public Element getResult() {
        return result;
    }

    public void setResult(Element result) {
        this.result = result;
    }

    public List<ItemStack> constructIngredients(Random random){
        List<ItemStack> finalIngredients = new ArrayList<>();
        for(Element element : ingredients){
            ItemStack itemStack = new ItemStack(element.getItemStack());
            if(element.getMin() == element.getMax()){
                itemStack.setAmount(element.getMin());
            }
            else{
                itemStack.setAmount(Utils.getRandomNumberInRange(element.getMin(), element.getMax()));
            }

            finalIngredients.add(itemStack);
        }
        return finalIngredients;
    }

    public ItemStack constructResult(Random random){
        ItemStack resultItem = result.getItemStack();
        //resultItem.setAmount(random.nextInt(result.getMax() - result.getMin()) + result.getMin());
        if(result.getMin() == result.getMax() || result.getMin() > result.getMax() ){
            resultItem.setAmount(result.getMin());
        }
        else{
            int r = Utils.getRandomNumberInRange(result.getMin(), result.getMax());
            resultItem.setAmount(r);
        }

        return resultItem;
    }
}
