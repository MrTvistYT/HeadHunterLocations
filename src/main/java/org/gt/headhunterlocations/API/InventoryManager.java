package org.gt.headhunterlocations.API;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.gt.headhunterlocations.Objects.AllMain;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryManager {
    public static Inventory getPack(Player player, AllMain main){
        Inventory inv = Bukkit.createInventory(null, 27, "§fᐽ⪨ Кейс с головами");

        List<String> headsStr = main.getSQLgetter().getHeads(player);


        String result = headsStr.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(" "));

        player.sendMessage(result);

        List<ItemStack> headList = headsStr.stream()
                .filter(owner -> !owner.equals(""))
                .map(owner -> {
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    meta.setOwner(owner);
                    head.setItemMeta(meta);
                    return head;
                })
                .collect(Collectors.toList());

        headList.forEach(inv::addItem);

        return inv;
    }
}
