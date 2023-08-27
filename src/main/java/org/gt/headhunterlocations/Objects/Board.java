package org.gt.headhunterlocations.Objects;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.*;
import org.bukkit.util.Transformation;
import org.gt.headhunterlocations.API.ItemStackGenerator;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.*;

public class Board implements ConfigurationSerializable {
    AllMain main;


    private int page;
    private List<List<Task>> pages;
    private int currentPage;



    List<Interaction> showedTasks = new ArrayList<>();

    ItemDisplay left;
    ItemDisplay right;

    Interaction leftI;
    Interaction rightI;

    TextDisplay pageT;

    Location loc;

    public Board(Location location, AllMain main){
        this.main = main;

        this.loc = location;

        location.getNearbyEntities(2,2,2).stream()
                .filter(entity ->
                entity instanceof ItemDisplay ||
                entity instanceof TextDisplay ||
                entity instanceof BlockDisplay ||
                entity instanceof Interaction)
                .forEach(Entity::remove);

        ItemDisplay itemDisplay = location.getWorld().spawn(location, ItemDisplay.class);
        itemDisplay.setItemStack(ItemStackGenerator.generateItem(Material.PAPER, 1000));
        itemDisplay.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);
        itemDisplay.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(1.3f), new AxisAngle4f()));

        itemDisplay.setRotation(180, 0);

        this.pages = new ArrayList<>();

        this.currentPage = 0;
        this.page = 1;

        createButtons();
    }
    public void updateBoard(){
        removeShowedTasks();

        List<Task> taskList = main.getSQLgetter().getTasks();

        List<List<Task>> p = new ArrayList<>();
        p.add(new ArrayList<>());



        int num = 0;
        int listInd = 0;

        for(Task task : taskList){
            if(num == 9){
                num = 0;

                listInd++;
                p.add(new ArrayList<>());

                continue;
            }

            p.get(listInd).add(task);
            num++;
        }

        main.getData().clearNickTask();

        for(Task task : taskList){
            main.getData().addNickTask(task.getNickname(), task);
        }


        this.pages = p;

        page = 1;
        pageT.setText(page + "");

        updatePage();
    }



    public void nextPage() {
        if (page < pages.size()) {
            page++;
            removeShowedTasks();
            updatePage();
        }

    }

    public void previousPage() {
        if (page > 1) {
            page--;
            removeShowedTasks();
            updatePage();
        }
    }




    public void updatePage(){
        for(Task task : pages.get(page-1)){

            double y;
            if(showedTasks.size() >= 5){
                y = 1;
            }
            else{
                y = 1.5;
            }


            int column = showedTasks.size();

            if(column >= 5) {
                column -= 5;
            }

            double x = -1 * (column * (0.5));

            Location taskLoc = loc.clone().add(1, 0, 0);
            taskLoc.add(x, y, 0);










            ItemDisplay itemDisplay = taskLoc.getWorld().spawn(taskLoc, ItemDisplay.class);

            itemDisplay.setTransformation(new Transformation(new Vector3f(),
                    itemDisplay.getTransformation().getRightRotation(),
                    new Vector3f(0.4f, 0.4f, 0.4f),
                    itemDisplay.getTransformation().getLeftRotation()));

            itemDisplay.setItemStack(ItemStackGenerator.generateItem(Material.PAPER, 1));

            task.setItem(itemDisplay);





            taskLoc.add(0, -0.3, 0);

            Interaction interaction = taskLoc.getWorld().spawn(taskLoc, Interaction.class);
            interaction.setInteractionHeight(0.4f);
            interaction.setInteractionWidth(0.4f);

            task.setInteraction(interaction);


            main.getData().addItemTask(itemDisplay, task);
            main.getData().addItemDisplayBoard(itemDisplay, this);
            main.getData().addInteractionItem(interaction, itemDisplay);

            showedTasks.add(interaction);
        }

        pageT.setText(page + "");
    }



    public void removeShowedTasks(){
        if(showedTasks != null) {
            for (Interaction interaction : showedTasks) {
                main.getData().getItemByInteraction(interaction).remove();
                interaction.remove();
            }
        }
        showedTasks = new ArrayList<>();
    }

    public void createButtons(){
        Location locat = loc.clone().add(-0.4, 0.5, 0);
        ItemDisplay leftArrow = locat.getWorld().spawn(locat, ItemDisplay.class);

        leftArrow.setItemStack(ItemStackGenerator.generateItem(Material.PAPER, 4));

        leftArrow.setTransformation(new Transformation(new Vector3f(),
                leftArrow.getTransformation().getRightRotation(),
                new Vector3f(0.3f, 0.3f, 0.3f),
                leftArrow.getTransformation().getLeftRotation()));


        locat.add(0, -0.3, 0);
        Interaction leftInter = locat.getWorld().spawn(locat, Interaction.class);


        leftInter.setInteractionHeight(0.4f);
        leftInter.setInteractionWidth(0.4f);

        this.left = leftArrow;
        this.leftI = leftInter;



        Location scrollL = loc.clone().add(0, 0.3, 0);
        TextDisplay scroll = scrollL.getWorld().spawn(scrollL, TextDisplay.class);

        scroll.setBackgroundColor(Color.fromARGB(0));

        scroll.setTransformation(new Transformation(new Vector3f(),
                new Quaternionf(0, Math.toRadians(180), 0,0),
                new Vector3f(0.1f, 0.1f, 0.1f),
                scroll.getTransformation().getLeftRotation()));


        scroll.setText(page + "");

        this.pageT = scroll;


        Location locat2 = loc.clone().add(0.4, 0.5, 0);
        ItemDisplay rightArrow = locat2.getWorld().spawn(locat2, ItemDisplay.class);

        rightArrow.setItemStack(ItemStackGenerator.generateItem(Material.PAPER, 5));

        rightArrow.setTransformation(new Transformation(new Vector3f(),
                rightArrow.getTransformation().getRightRotation(),
                new Vector3f(0.3f, 0.3f, 0.3f),
                rightArrow.getTransformation().getLeftRotation()));



        locat2.add(0, -0.3, 0);
        Interaction rightInter = locat2.getWorld().spawn(locat2, Interaction.class);

        rightInter.setInteractionHeight(0.4f);
        rightInter.setInteractionWidth(0.4f);

        this.right = rightArrow;
        this.rightI = rightInter;

        main.getData().addInteractionItem(leftInter, leftArrow);
        main.getData().addInteractionItem(rightInter, rightArrow);
    }

    public void remove(){
        Optional<ItemDisplay> itm = getLoc().getNearbyEntitiesByType(ItemDisplay.class, 0.3).stream().findFirst();

        if(itm != null){
            itm.get().remove();
        }


        rightI.remove();
        right.remove();

        left.remove();
        leftI.remove();

        pageT.remove();
    }

    public Location getLoc() {
        return loc;
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("location", loc);

        return map;
    }


    public List<List<Task>> getPages() {
        return pages;
    }
    public int getPage(){
        return page;
    }
}

