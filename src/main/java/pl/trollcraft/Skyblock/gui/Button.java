package pl.trollcraft.Skyblock.gui;

import org.bukkit.inventory.ItemStack;

public class Button {

    private int slot;
    private ItemStack icon;

    public Button(int slot, ItemStack icon){
        this.slot = slot;
        this.icon = icon;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }
}
