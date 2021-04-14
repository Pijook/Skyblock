package pl.trollcraft.Skyblock.villagercontroller;

import org.bukkit.inventory.ItemStack;

public class Element {

    private ItemStack itemStack;
    private int min;
    private int max;

    public Element(ItemStack itemStack, int min, int max){
        this.itemStack = itemStack;
        this.min = min;
        this.max = max;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
