package me.verosity.knightfall.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItem {
    static int modelData = 1;
    static String name;
    static List<String> lore;
    static Material material = Material.AIR;


    public CustomItem(String _name, List<String> _lore, Material _material, int _modelData){
        name = _name;
        lore = _lore;
        material = _material;
        modelData = _modelData;
    }

    public ItemStack getItem(){
        ItemStack returnVal = new ItemStack(material);

        ItemMeta meta = returnVal.getItemMeta();

        assert meta != null;
        meta.setCustomModelData(modelData);
        meta.setDisplayName("§r" + name);
        meta.setLore(lore);

        return returnVal;
    }

}
