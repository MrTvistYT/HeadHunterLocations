package org.gt.headhunterlocations.API;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RunnableTimer implements Runnable {
    private int taskId;
    private long repeated;

    public RunnableTimer(JavaPlugin plugin, int arg1, int arg2) {
        taskId = Bukkit.getScheduler().runTaskTimer(plugin, this, arg1, arg2).getTaskId();
    }

    public void canncel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public int getNumber(){
        return taskId;
    }
    public long getRepeated(){
        return repeated;
    }
    public void addRepeated() {
        repeated++;
    }
}
