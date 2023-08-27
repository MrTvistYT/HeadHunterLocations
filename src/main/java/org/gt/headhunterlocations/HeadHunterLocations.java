package org.gt.headhunterlocations;

import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.gt.headhunterlocations.API.ConfigManager;
import org.gt.headhunterlocations.API.DataManager;
import org.gt.headhunterlocations.API.ExpressionConverter;
import org.gt.headhunterlocations.Commands.*;
import org.gt.headhunterlocations.Events.*;
import org.gt.headhunterlocations.MySQL.SQLManager;
import org.gt.headhunterlocations.MySQL.SQLgetter;
import org.gt.headhunterlocations.Objects.AllMain;
import org.gt.headhunterlocations.Objects.Board;
import org.gt.headhunterlocations.Objects.Info;
import org.gt.headhunterlocations.Objects.Task;
import org.gt.headhunterlocations.PlaceHolders.KarmaPlaceHolder;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public final class HeadHunterLocations extends JavaPlugin implements PluginMessageListener {

    DataManager data;
    SQLgetter sqLgetter;

    static AllMain main;

    private File dataFile;
    private FileConfiguration dataConf;
    public static HeadHunterLocations instance;

    public static AllMain getMain() {
        return main;
    }

    @Override
    public void onEnable() {
        instance = this;

        createCustomConfig();
        saveDefaultConfig();

        ConfigManager configManager = new ConfigManager(getConfig());
        data = new DataManager();

        SQLManager sqlManager = new SQLManager();
        sqLgetter = new SQLgetter(sqlManager);

        XConomyAPI xcapi = new XConomyAPI();

        try {
            sqlManager.setConnection(configManager);
        } catch (SQLException | ClassNotFoundException er) {
            Bukkit.getLogger().info("0------------------------0");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("Database is not connected");
            Bukkit.getLogger().info("");
            Bukkit.getLogger().info("0------------------------0");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (sqlManager.isConnected()) {
            Bukkit.getLogger().info("Database is connected");
            sqLgetter.createTable();
        }

        main = new AllMain(this, data, configManager, sqLgetter, xcapi);

        registerPluginChannel("headhunter:hhboard");

        loadData();

        getCommand("spawnHunter").setExecutor(new SpawnNPC(main));
        getCommand("spawnBoard").setExecutor(new SpawnBoard(main));
        getCommand("headHunter").setExecutor(new HeadHunterCommand(main));
        getCommand("karma").setExecutor(new KarmaCommand(main));
        getCommand("karma").setTabCompleter(new KarmaCommand(main));
        getCommand("headBag").setExecutor(new HeadBagCommand(main));

        Bukkit.getPluginManager().registerEvents(new GlobalEvent(main), this);
        Bukkit.getPluginManager().registerEvents(new EventHunter(main), this);
        Bukkit.getPluginManager().registerEvents(new BoardEvent(main), this);
        Bukkit.getPluginManager().registerEvents(new KarmaEvents(main), this);
        Bukkit.getPluginManager().registerEvents(new InventoryEvent(main), this);

        new KarmaPlaceHolder(main).register();

        messageReader();

    }

    @Override
    public void onDisable() {
        for (Info info : data.getInfos()) {
            info.removeInfo();
        }

        for (Task task : data.getAllTasks()) {
            if (task.getInteraction() != null) {
                task.getInteraction().remove();
            }
            if (task.getItem() != null) {
                task.getItem().remove();
            }
        }
        data.getBoard().removeShowedTasks();
        data.getBoard().remove();

        saveData();
    }

    private void createCustomConfig() {
        dataFile = new File(getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }

        dataConf = new YamlConfiguration();
        try {
            dataConf.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    private void saveData() {
        dataConf.set("board", null);


        if (data.getBoard() != null) {
            dataConf.set("board", data.getBoard().serialize());
        }

        try {
            dataConf.save(dataFile);
        } catch (IOException e) {
            Bukkit.getLogger().info("Can't save");
        }
    }


    private void loadData() {
        if (dataFile != null && dataFile.exists()) {
            try {
                Location loc = dataConf.getLocation("board.location");

                Board board = new Board(loc, main);
                data.setBoard(board);

                sqLgetter.setMain(main);
                board.updateBoard();

            } catch (NullPointerException er) {
                Bukkit.getLogger().warning("[HeadHunter] PROBLEM IN LOADING DATA");
            }
        }
    }

    public void registerPluginChannel(String channel) {
        Bukkit.getMessenger().registerIncomingPluginChannel(this, channel, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if (channel.equals("headhunter:hhboard")) {
            String cmd = new String(bytes, StandardCharsets.UTF_8);

            Bukkit.getLogger().info(cmd);

            String[] args = cmd.split(" ");
            if (cmd.contains("sendLastSeen")) {
                Player playerNick = Bukkit.getPlayer(args[1]);

                if (main.getSQLgetter().getBoolean(playerNick, "headHunter", "license")) {
                    int seen = main.getSQLgetter().getValue(playerNick, "lastSeen");
                    int tasks = main.getSQLgetter().getTasks().size();

                    if (seen < tasks) {
                        playerNick.sendMessage("На доске доступно " + (tasks - seen) + " новых задания");
                    }
                }
            }
        }
    }

    private void messageReader() {
        Bukkit.getScheduler().runTaskTimer(this, reader -> {
            List<String> cmd = main.getSQLgetter().getCommands();

            cmd.forEach(command -> {
                String[] args = command.split(" ");

                if (cmd.contains("update")) {
                    updateTasks(args[1]);
                    main.getSQLgetter().removeCommand(command);
                }
            });

        }, 5, 5);
    }

    public static boolean isTasked(Player player) {
        return main.getSQLgetter().getBoolean(player, "headHunter", "tasked");
    }

    public static int getKarma(Player player) {
        return main.getSQLgetter().getValue(player, "karma");
    }

    public static void setKarma(String player, int amount){
        main.getSQLgetter().setValue(player, "karma", amount);

        updateTasks(player);

        main.getData().getBoard().updateBoard();
    }

    public static void addKarma(String player, int amount) {
        main.getSQLgetter().addValue(player, "karma", amount);

        updateTasks(player);

        main.getData().getBoard().updateBoard();
    }



    private static void updateTasks(String player){
        if (main.getSQLgetter().exists("headHunterTasks", player)) {
            int karma = main.getSQLgetter().getValue(player, "karma");

            if (karma > main.getConfigManager().getBoardKarma()) {
                main.getSQLgetter().removeTask(player);
            }

            if (!main.getSQLgetter().getBoolean(player, "headHunterTasks", "byPlayer")) {
                String eq = main.getConfigManager().getFormula();
                int money = ExpressionConverter.evaluateExpression(eq, karma);


                main.getSQLgetter().setValue(player, "headHunterTasks", "money", money);
            }

            main.getData().getBoard().updateBoard();
        } else {
            if (main.getSQLgetter().getValue(player, "karma") <= main.getConfigManager().getBoardKarma()) {
                if (!main.getSQLgetter().exists("headHunterTasks", player)) {
                    main.getSQLgetter().createTask(player, 0, false);

                    int karma = main.getSQLgetter().getValue(player, "karma");

                    String eq = main.getConfigManager().getFormula();
                    int money = ExpressionConverter.evaluateExpression(eq, karma);

                    main.getSQLgetter().setValue(player, "headHunterTasks", "money", money);

                    main.getSQLgetter().setValue(player, "tasked", 1);

                    main.getData().getBoard().updateBoard();

                    Bukkit.getServer().sendPluginMessage(main.getPlugin(), "headhunter:hhboard", "sendTasks".getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }
}