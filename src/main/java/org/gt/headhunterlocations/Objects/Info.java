package org.gt.headhunterlocations.Objects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.gt.headhunterlocations.API.ItemStackGenerator;
import org.gt.headhunterlocations.API.RunnableTimer;
import org.joml.Vector3f;

public class Info {
    AllMain main;


    Board board;
    Task task;


    Player player;
    TextDisplay scroll;
    TextDisplay text;
    ItemDisplay head;

    public Info(Board board, Task task, Player player, AllMain main){
        this.board = board;
        this.task = task;

        this.player = player;

        this.main = main;



        Location scrollL = board.getLoc().clone().add(3, 0.5, 0);
        TextDisplay scroll = scrollL.getWorld().spawn(scrollL, TextDisplay.class);

        scroll.setBackgroundColor(Color.fromARGB(0));

        scroll.setRotation(150, 5);

        scroll.setTransformation(new Transformation(new Vector3f(),
                scroll.getTransformation().getLeftRotation(),
                new Vector3f(0.7f, 0.7f, 0.7f),
                scroll.getTransformation().getRightRotation()));


        scroll.setText("⨑ \n\n\n\n\n\n\n\n\n");


        Location textL = board.getLoc().clone().add(2.9, 0.5, -0.1);
        TextDisplay text = textL.getWorld().spawn(textL, TextDisplay.class);

        text.setBackgroundColor(Color.fromARGB(0));
        text.setRotation(150, 5);

        text.setTransformation(new Transformation(new Vector3f(),
                text.getTransformation().getLeftRotation(),
                new Vector3f(0.7f, 0.7f, 0.7f),
                text.getTransformation().getRightRotation()));


        text.setText(
                "§0Игрок: " + task.getNickname() + "\n\n\n\n\n" +
                "§0Карма: " + task.getKarma() + "\n" +
                "§0Награда: " + task.getMoney()
        );






        Location playerImage = textL.clone().add(-0.1, 1, -0.3);
        ItemDisplay playerHead = playerImage.getWorld().spawn(playerImage, ItemDisplay.class);
        playerHead.setItemStack(ItemStackGenerator.createHead(task.getNickname()));

        playerHead.setRotation(-30, -5);

        playerHead.setTransformation(new Transformation(new Vector3f(),
                playerHead.getTransformation().getLeftRotation(),
                new Vector3f(0.8f, 0.8f, 0.1f),
                playerHead.getTransformation().getRightRotation()));


        this.scroll = scroll;
        this.text = text;
        this.head = playerHead;


        Info info = this;
        RunnableTimer timer = new RunnableTimer(main.getPlugin(), 1,1){
            @Override
            public void run() {
                for(Player another : player.getLocation().getNearbyEntitiesByType(Player.class, 100)){
                    if(another != player){
                        another.hideEntity(main.getPlugin(), playerHead);
                        another.hideEntity(main.getPlugin(), text);
                        another.hideEntity(main.getPlugin(), scroll);
                    }
                }


                if(getRepeated() == 0) {
                    main.getData().addPlayerTask(player, this);
                }
                if(getRepeated() == 100) {
                    removeInfo();

                    main.getData().removeInfos(info);
                    main.getData().removePlayerInfo(player);
                    main.getData().removePlayerTask(player);
                }
                addRepeated();
            }
        };

        main.getData().addInfos(this);
    }



    public void removeInfo(){
        head.remove();
        text.remove();
        scroll.remove();
    }

    public void update(Task task, Board board){
        this.task = task;


        scroll.setText("⨑ \n\n\n\n\n\n\n\n\n");

        text.setText(
                "§0Игрок: " + task.getNickname() + "\n\n\n\n\n" +
                        "§0Карма: " + task.getKarma() + "\n" +
                        "§0Награда: " + task.getMoney()
        );

        head.setItemStack(ItemStackGenerator.createHead(task.getNickname()));



        RunnableTimer timer = new RunnableTimer(main.getPlugin(), 1,1){
            @Override
            public void run() {
                for(Player another : player.getLocation().getNearbyEntitiesByType(Player.class, 100)){
                    if(another != player){
                        another.hideEntity(main.getPlugin(), head);
                        another.hideEntity(main.getPlugin(), text);
                        another.hideEntity(main.getPlugin(), scroll);
                    }
                }


                if(getRepeated() == 0) {
                    main.getData().addPlayerTask(player, this);
                }
                if(getRepeated() == 100) {
                    removeInfo();

                    main.getData().removePlayerInfo(player);
                    main.getData().removePlayerTask(player);
                }
                addRepeated();
            }
        };
    }
}
