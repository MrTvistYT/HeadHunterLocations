package org.gt.headhunterlocations.Events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.gt.headhunterlocations.API.ItemStackGenerator;
import org.gt.headhunterlocations.Objects.AllMain;
import org.gt.headhunterlocations.Objects.Task;

public class BoardEvent implements Listener {
    AllMain main;
    public BoardEvent(AllMain main){
        this.main = main;
    }

    @EventHandler
    public void clickList(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();

        if(event.getRightClicked().getType() != EntityType.INTERACTION){
            return;
        }
        ItemDisplay itemDisplay = main.getData().getItemByInteraction((Interaction) event.getRightClicked());

        if(itemDisplay == null){
            return;
        }

        Task task = main.getData().getItemTask(itemDisplay);
        if(task == null){
            return;
        }
        task.showInfoForPlayer(player, main.getData().getItemDisplayBoard(itemDisplay));
    }










    @EventHandler
    public void clickArrow(PlayerInteractAtEntityEvent event){
        Player player = event.getPlayer();

        if(event.getRightClicked().getType() != EntityType.INTERACTION){
            return;
        }
        ItemDisplay itemDisplay = main.getData().getItemByInteraction((Interaction) event.getRightClicked());

        if(itemDisplay == null){
            return;
        }

        if(itemDisplay.getItemStack().isSimilar(ItemStackGenerator.generateItem(Material.PAPER, 4))){
            main.getData().getBoard().previousPage();
        }
        else if(itemDisplay.getItemStack().isSimilar(ItemStackGenerator.generateItem(Material.PAPER, 5))){
            main.getData().getBoard().nextPage();
        }
    }
}
