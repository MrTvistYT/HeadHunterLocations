package org.gt.headhunterlocations.Commands;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.gt.headhunterlocations.Objects.AllMain;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class HeadHunterCommand implements CommandExecutor {
    AllMain main;

    public HeadHunterCommand(AllMain main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) commandSender;
        if(args.length != 1){
            return true;
        }

        switch (args[0]){
            case "accept":
                ItemStack item = player.getInventory().getItemInMainHand();
                if(CustomStack.byItemStack(item) != null && CustomStack.byItemStack(item).getNamespacedID().equals(main.getConfigManager().getItemNamespaceId())){
                    CustomStack customStack = CustomStack.byItemStack(item);
                    if(item.getAmount() >= main.getConfigManager().getAmount()){
                        main.getSQLgetter().setValue(player, "license", 1);

                        player.sendMessage("Вы успешно приобрели лицензию!");

                        Bukkit.getServer().sendPluginMessage(main.getPlugin(), "headhunter:hhboard", ("givePermission " + player.getName()).getBytes(StandardCharsets.UTF_8));

                        item.setAmount(item.getAmount() - main.getConfigManager().getAmount());
                    }
                }
                break;

            case "cancel":
                player.sendMessage("Мне жаль, что вы отказались :(");
                break;


            case "createTask":
                if(!main.getData().getWaiting(player)) {
                    player.sendMessage("Введите информацию в виде \"Ник цена\"");
                    main.getData().addWaiting(player);

                }
                break;


            case "cancelCreating":
                player.sendMessage("Мне жаль, что вы отказались :(");
                break;
        }

        return false;
    }




    public static void removeItems(PlayerInventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }
}
