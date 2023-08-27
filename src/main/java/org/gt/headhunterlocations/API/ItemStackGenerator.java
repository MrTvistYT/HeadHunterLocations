package org.gt.headhunterlocations.API;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;



public class ItemStackGenerator {
    public static ItemStack generateItem(Material material, int cmd){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(cmd);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createHead(String nickname){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwner(nickname);

        head.setItemMeta(headMeta);
        return head;
    }
}
