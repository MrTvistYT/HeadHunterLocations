package org.gt.headhunterlocations.Events;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.gt.headhunterlocations.API.GenerateMessage;
import org.gt.headhunterlocations.Objects.AllMain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

public class EventHunter implements Listener {
    AllMain main;
    public EventHunter(AllMain main){
        this.main = main;
    }

    @EventHandler
    public void onPlayerClick(NPCRightClickEvent event){
        Player player = event.getClicker();

        if(!event.getNPC().getName().equals("Охотник")){
            return;
        }

        if(!main.getSQLgetter().getBoolean(player, "headHunter", "license")){
            player.sendMessage("Приветствую, путник! Не желаешь ли ты купить лицензию и опробовать себя в качестве охотника на людей!");
            player.sendMessage("По секрету скажу, за эту работу получают неплохую сумму");
            player.sendMessage("Согласен?");

            player.sendMessage(GenerateMessage.generateAcceptDecline());
        }
        else{
            player.sendMessage("Приветствую, охотник! Неужели ты хочешь сделать заказ на игрока?!");
            player.sendMessage(GenerateMessage.generateYesNo());
        }
    }



    @EventHandler
    public void onPlayerSuccess(NPCLeftClickEvent event){
        Player player = event.getClicker();

        if(!event.getNPC().getName().equals("Охотник")){
            return;
        }

        if(!main.getSQLgetter().getBoolean(player, "headHunter", "license")){
            return;
        }

        if(!main.getSQLgetter().getString(player.getName(), "storedHeads").equals("")) {
            if (!main.getSQLgetter().hasCooldown(player)) {
                List<String> nicks = main.getSQLgetter().getTasksNicks();

                for (String str : main.getSQLgetter().getHeads(player)) {
                    if (nicks.contains(str)) {
                        player.sendMessage("Вы успешно выполнили задание!");
                        int money = main.getData().getTaskByNick(str).getMoney();

                        main.getApi().changePlayerBalance(player.getUniqueId(),
                                player.getName(),
                                BigDecimal.valueOf(money),
                                true);

                        main.getSQLgetter().removeTask(str);
                        main.getSQLgetter().setValue(str, "tasked", 0);

                        main.getSQLgetter().removeHeads(str);

                        main.getSQLgetter().startCooldown(player);
                        main.getData().getBoard().updateBoard();
                        return;
                    }
                }
            }
            Timestamp time = main.getSQLgetter().getTimeStamp(player, "taskCoolDown");
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());

            Duration duration = Duration.between(nowTime.toLocalDateTime(), time.toLocalDateTime());

            String text = duration.toHoursPart() + " часа " + duration.toMinutesPart() + " минут.";
            player.sendMessage("У вас откат! Доступно будет через " + text);
            return;
        }
        player.sendMessage("У вас нет нужной головы в кейсе для голов!");
    }
}
