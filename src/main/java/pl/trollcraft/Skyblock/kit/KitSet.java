package pl.trollcraft.Skyblock.kit;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class KitSet {

    private HashMap<Integer, Kit> kits;
    private long cooldown;

    public KitSet(HashMap<Integer, Kit> kits, long cooldown){
        this.kits = kits;
        this.cooldown = cooldown;
    }

    public Kit getKit(int level){
        return kits.getOrDefault(level, null);
    }

    public void addKit(int level, Kit kit){
        kits.put(level, kit);
    }

    public boolean addToKit(int level, ItemStack itemStack){
        try{
            kits.get(level).addItem(itemStack);
            return true;
        }
        catch (NullPointerException e){
            return false;
        }
    }

    public HashMap<Integer, Kit> getKits() {
        return kits;
    }

    public void setKits(HashMap<Integer, Kit> kits) {
        this.kits = kits;
    }
}
