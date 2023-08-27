package org.gt.headhunterlocations.API;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.gt.headhunterlocations.Objects.Board;
import org.gt.headhunterlocations.Objects.Info;
import org.gt.headhunterlocations.Objects.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataManager {

    public Board board;

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }



    private final HashMap<Player, Info> showedInfo = new HashMap<>();

    public void addPlayerInfo(Player player, Info info){
        if(!showedInfo.containsKey(player)){
            showedInfo.put(player, info);
        }
        else{
            showedInfo.replace(player, info);
        }
    }
    public void removePlayerInfo(Player player){
        showedInfo.remove(player);
    }

    public HashMap<Player, Info> getShowedInfo() {
        return showedInfo;
    }

















    private final HashMap<Player, RunnableTimer> playerTask = new HashMap<>();
    public void addPlayerTask(Player player, RunnableTimer runnableTimer){
        if(!playerTask.containsKey(player)){
            playerTask.put(player, runnableTimer);
        }
        else{
            playerTask.replace(player, runnableTimer);
        }
    }
    public void removePlayerTask(Player player){
        playerTask.remove(player);
    }
    public RunnableTimer getPlayerTask(Player player){
        return playerTask.get(player);
    }












    private final HashMap<Interaction, ItemDisplay> interactionItem = new HashMap<>();

    public void addInteractionItem(Interaction interaction,ItemDisplay itemDisplay){
        if(!interactionItem.containsKey(interaction)){
            interactionItem.put(interaction, itemDisplay);
        }
        else{
            interactionItem.replace(interaction, itemDisplay);
        }
    }
    public ItemDisplay getItemByInteraction(Interaction interaction){
        return interactionItem.get(interaction);
    }

    public void removeInteractionItem(Interaction interaction){
        interactionItem.remove(interaction);
    }



























    private final HashMap<ItemDisplay, Task> itemTask = new HashMap<>();

    public void addItemTask(ItemDisplay itemDisplay, Task task){
        if(!itemTask.containsKey(itemDisplay)){
            itemTask.put(itemDisplay, task);
        }
        else{
            itemTask.replace(itemDisplay, task);
        }
    }
    public Task getItemTask(ItemDisplay itemDisplay){
        return itemTask.get(itemDisplay);
    }

    public void removeItemTask(ItemDisplay itemDisplay){
        itemTask.remove(itemDisplay);
    }




    private final HashMap<ItemDisplay, Board> itemDisplayBoard = new HashMap<>();
    public void addItemDisplayBoard(ItemDisplay itemDisplay, Board board){
        if(!itemDisplayBoard.containsKey(itemDisplay)){
            itemDisplayBoard.put(itemDisplay, board);
        }
        else{
            itemDisplayBoard.replace(itemDisplay, board);
        }
    }
    public Board getItemDisplayBoard(ItemDisplay itemDisplay){
        return itemDisplayBoard.get(itemDisplay);
    }

    public void removeItemDisplayBoard(ItemDisplay itemDisplay){
        itemDisplayBoard.remove(itemDisplay);
    }












    private final HashMap<TextDisplay, ItemDisplay> textItem = new HashMap<>();
    public void addTextHead(TextDisplay textDisplay, ItemDisplay itemDisplay){
        if(!textItem.containsKey(textDisplay)){
            textItem.put(textDisplay, itemDisplay);
        }
        else{
            textItem.replace(textDisplay, itemDisplay);
        }
    }
    public ItemDisplay getTextHead(TextDisplay textDisplay){
        return textItem.get(textDisplay);
    }

    public void removeTextItem(TextDisplay textDisplay){
        textItem.remove(textDisplay);
    }










    private final List<Player> waiting = new ArrayList<>();
    public void addWaiting(Player player){
        waiting.add(player);
    }
    public boolean getWaiting(Player player){
        return waiting.contains(player);
    }

    public void removeWaiting(Player player){
        waiting.remove(player);
    }







    private final HashMap<Integer, List<Task>> tasksPages = new HashMap<>();


    public void addTask(Integer integer, Task task){
        if(tasksPages.containsKey(integer)) {
            tasksPages.get(integer).add(task);
        }
        else{
            List<Task> t = new ArrayList<>();
            t.add(task);
            tasksPages.put(integer, t);
        }
    }
    public List<Task> getTasks(int page) {
        return tasksPages.get(page);
    }
    public void removeTask(Task task){
        for(List<Task> list : tasksPages.values()){
            if(list.contains(task)){
                list.remove(task);
                return;
            }
        }
    }
    public int findFirstPage(){
        for(List<Task> list : tasksPages.values()){
            if(list.size() <= 10){
                return HashMapUtils.getKeyByValue(tasksPages, list);
            }
        }
        return -1;
    }

    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();

        for(Integer page : tasksPages.keySet()){
            taskList.addAll(tasksPages.get(page));
        }

        return taskList;
    }

    public int getLastPage(){
        if(!tasksPages.isEmpty()) {
            return tasksPages.keySet().stream()
                    .max(Integer::compareTo)
                    .orElse(Integer.MIN_VALUE);
        }

        return -1;
    }

    public HashMap<Integer, List<Task>> getTasksPages() {
        return tasksPages;
    }

    private final List<Info> infos = new ArrayList<>();

    public void addInfos(Info info){
        infos.add(info);
    }
    public List<Info> getInfos() {
        return infos;
    }
    public void removeInfos(Info info){
        infos.remove(info);
    }












    private final HashMap<String, Task> nickTask = new HashMap<>();

    public void addNickTask(String nick, Task task){
        nickTask.put(nick, task);
    }

    public Task getTaskByNick(String nick){
        return nickTask.get(nick);
    }
    public void removeNickTask(String nick){
        nickTask.remove(nick);
    }

    public void clearNickTask(){
        nickTask.clear();
    }
}
