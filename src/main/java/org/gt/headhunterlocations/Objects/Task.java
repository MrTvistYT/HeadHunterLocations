package org.gt.headhunterlocations.Objects;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.gt.headhunterlocations.API.ExpressionConverter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class Task implements ConfigurationSerializable {
    String nickname;
    int karma;
    int money;

    boolean byPlayer;

    transient Interaction interaction;
    transient ItemDisplay item;

    transient AllMain main;

    public Task(String nickname, int money, AllMain main){
        this.nickname = nickname;
        this.money = money;
        this.karma = main.getSQLgetter().getValue(nickname, "karma");

        byPlayer = true;

        this.main = main;
    }


    public Task(String nickname, AllMain main){
        this.nickname = nickname;
        this.karma = main.getSQLgetter().getValue(nickname, "karma");

        String eq = main.getConfigManager().getFormula();
        this.money = ExpressionConverter.evaluateExpression(eq, karma);

        byPlayer = false;

        this.main = main;
    }


    public void showInfoForPlayer(Player player, Board board){

        if(!main.getData().getShowedInfo().containsKey(player)) {
            Info info = new Info(board, this, player, main);

            main.getData().addPlayerInfo(player, info);
            main.getData().addInfos(info);
        }
        else{
            Info info = main.getData().getShowedInfo().get(player);

            main.getData().getPlayerTask(player).canncel();

            info.update(this, board);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public int getMoney() {
        return money;
    }

    public int getKarma() {
        return karma;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("player", nickname);
        map.put("money", money);

        return map;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public void setItem(ItemDisplay item) {
        this.item = item;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public ItemDisplay getItem() {
        return item;
    }

}
