package org.gt.headhunterlocations.Objects;

import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.plugin.java.JavaPlugin;
import org.gt.headhunterlocations.API.ConfigManager;
import org.gt.headhunterlocations.API.DataManager;
import org.gt.headhunterlocations.MySQL.SQLgetter;

public class AllMain {
    JavaPlugin plugin;
    DataManager dataManager;
    ConfigManager configManager;
    SQLgetter sqLgetter;
    XConomyAPI api;
    public AllMain(JavaPlugin javaPlugin, DataManager dataManager, ConfigManager configManager, SQLgetter sqLgetter, XConomyAPI api){
        this.plugin = javaPlugin;
        this.dataManager = dataManager;
        this.configManager = configManager;
        this.sqLgetter = sqLgetter;
        this.api = api;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DataManager getData() {
        return dataManager;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public SQLgetter getSQLgetter() {
        return sqLgetter;
    }

    public XConomyAPI getApi(){
        return api;
    }
}
