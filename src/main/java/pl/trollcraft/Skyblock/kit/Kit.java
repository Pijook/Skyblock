package pl.trollcraft.Skyblock.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Kit {

    private ArrayList<ItemStack> items;

    public Kit(ArrayList<ItemStack> items){
        this.items = items;
    }

    public void addItem(ItemStack itemStack){
        items.add(itemStack);
    }

    public void removeItem(ItemStack itemStack){
        if(items.contains(itemStack)){
            items.remove(itemStack);
        }
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public void giveItems(Player player){
        for(ItemStack itemStack : items){
            player.getInventory().addItem(new ItemStack(itemStack));
        }
    }
}
