package org.gt.headhunterlocations.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.gt.headhunterlocations.Objects.AllMain;

public class InventoryEvent implements Listener {
    AllMain main;
    public InventoryEvent(AllMain main){
        this.main = main;
    }
    @EventHandler
    public void onPlayerInteract(InventoryClickEvent event){
        if(event.getView().getTitle().equalsIgnoreCase("§fᐽ⪨ Кейс с головами")){
            event.setCancelled(true);
        }
    }
}
