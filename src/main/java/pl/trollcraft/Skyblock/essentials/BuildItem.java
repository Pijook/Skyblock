package pl.trollcraft.Skyblock.essentials;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BuildItem {

    /**
     * Builds item with specified details
     * @param name Item name
     * @param material Item Material
     * @param amount Amount of an item
     * @param lore Lore of an item
     * @return Returns ready item
     */
    public static ItemStack buildItem(String name, Material material, int amount, String lore){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        ArrayList<String> lore_list = new ArrayList<String>();
        lore_list.add(ChatColor.translateAlternateColorCodes('&', lore));
        meta.setLore(lore_list);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    /**
     * Builds item with specified details
     * @param name Item name
     * @param material Item Material
     * @param amount Amount of an item
     * @param lore Lore of an item
     * @return Returns ready item
     */
    public static ItemStack buildItem(String name, Material material, int amount, List<String> lore){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> lore_list = new ArrayList<String>();
        for(String line : lore){
            line = ChatUtils.fixColor(line);
            lore_list.add(line);
        }
        meta.setLore(lore_list);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    /**
     * Builds item with specified details
     * @param name Item name
     * @param material Item Material
     * @param amount Amount of an item
     * @return Returns ready item
     */
    public static ItemStack buildItem(String name, Material material, int amount){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

}
