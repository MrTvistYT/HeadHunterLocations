package org.gt.headhunterlocations.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gt.headhunterlocations.Objects.AllMain;
import org.gt.headhunterlocations.Objects.Task;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class GlobalEvent implements Listener {
    AllMain main;
    public GlobalEvent(AllMain main){
        this.main = main;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();


        main.getSQLgetter().createPlayer(player);

        main.getApi().createPlayerData(player.getUniqueId(), player.getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        int tasks = main.getSQLgetter().getTasks().size();

        main.getSQLgetter().setValue(player, "lastSeen", tasks);
    }

    @EventHandler
    public void chatEvent(PlayerChatEvent event){
        Player player = event.getPlayer();

        if (!main.getData().getWaiting(player)) {
            return;
        }


        String str = event.getMessage();
        String[] args = str.split(" ");

        if(args.length != 2){
            main.getData().removeWaiting(player);
            player.sendMessage("Не хватает аргументов");
            return;
        }



        try {
            Integer.parseInt(args[1]);
        }
        catch (NumberFormatException er){
            main.getData().removeWaiting(player);
            player.sendMessage("Второй аргумент не число!");
            return;
        }



        if(!main.getSQLgetter().exists("headHunter", args[0])){
            player.sendMessage("Данного игрока не существует!");
            return;
        }


        event.setCancelled(true);

        if(main.getSQLgetter().exists("headHunterTasks", args[0])){
            player.sendMessage("Данный игрок уже висит на таблице!");
            return;
        }

        if(main.getApi().getPlayerData(player.getName()).getBalance().intValue() >= Integer.parseInt(args[1])) {
            player.sendMessage("Вы успешно заказали этого человека!");

            Task task = new Task(args[0], Integer.parseInt(args[1]), main);

            main.getSQLgetter().createTask(args[0], Integer.parseInt(args[1]), true);


            main.getData().getBoard().updateBoard();

            main.getApi().changePlayerBalance(player.getUniqueId(),
                    player.getName(),
                    BigDecimal.valueOf(1000),
                    false);

            main.getSQLgetter().createTask(args[0], Integer.parseInt(args[1]), true);

            main.getData().addNickTask(args[0], task);

            main.getSQLgetter().setValue(args[0], "tasked", 1);

            player.sendPluginMessage(main.getPlugin(), "headhunter:hhboard", "sendTasks".getBytes(StandardCharsets.UTF_8));
        }
        else{
            player.sendMessage("У вас не хватает денег!");
        }
        main.getData().removeWaiting(player);
    }
}
