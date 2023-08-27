package org.gt.headhunterlocations.MySQL;

import org.bukkit.entity.Player;
import org.gt.headhunterlocations.Objects.AllMain;
import org.gt.headhunterlocations.Objects.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLgetter {
    SQLManager manager;
    AllMain main;
    public SQLgetter(SQLManager manager) {
        this.manager = manager;
    }


    public void createTable() {
        PreparedStatement ps;
        try {
            ps = manager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS headHunter "
                    + "(nickName VARCHAR(100), "
                    + "karma INT(10) DEFAULT 0, license BOOLEAN DEFAULT false, "
                    + "tasked BOOLEAN DEFAULT false, "
                    + "storedHeads TEXT, "
                    + "taskCoolDown DATETIME, "
                    + "lastSeen INT DEFAULT 0, "
                    + "PRIMARY KEY (nickName))");

            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }

        try {
                ps = manager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS headHunterTasks "
                    + "(nickName VARCHAR(100), "
                    + "money INT(10) DEFAULT 0, byPlayer BOOLEAN DEFAULT false, "
                    + "PRIMARY KEY (nickName))");

            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
        try {
            ps = manager.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS headHunterExecutor "
                    + "(command TEXT, "
                    + "server INT)");
            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }


    public void createPlayer(Player player) {
        try {
            if (!exists("headHunter", player)) {
                PreparedStatement ps2 = manager.getConnection().prepareStatement("INSERT IGNORE INTO headHunter (nickName, karma, license, tasked, storedHeads) VALUES (?, ?, ?, ?, ?)");
                ps2.setString(1, player.getName());
                ps2.setInt(2, 0);
                ps2.setBoolean(3, false);
                ps2.setBoolean(4, false);
                ps2.setString(5, "");

                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String table, Player player) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE nickName=?");
            ps.setString(1, player.getName());

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                //player is found
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String table, String player) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE nickName=?");
            ps.setString(1, player);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                //player is found
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addValue(Player player, String kind, int value){
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE headHunter SET " + kind + "= " + kind + " + ? WHERE nickName=?");
            ps.setInt(1, value);
            ps.setString(2, player.getName());
            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }
    public void addValue(String player, String kind, int value){
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE headHunter SET " + kind + "= " + kind + " + ? WHERE nickName=?");
            ps.setInt(1, value);
            ps.setString(2, player);
            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }
    public void setValue(Player player, String kind, int value) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE headHunter SET " + kind + "= ? WHERE nickName=?");
            ps.setInt(1, value);
            ps.setString(2, player.getName());
            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }
    public void setValue(Player player, String kind, Timestamp time) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE headHunter SET " + kind + "= ? WHERE nickName=?");
            ps.setTimestamp(1, time);
            ps.setString(2, player.getName());
            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }
    public void setValue(String player, String kind, int value) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE headHunter SET " + kind + "= ? WHERE nickName=?");
            ps.setInt(1, value);
            ps.setString(2, player);

            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }
    public void setValue(String player, String kind, String value) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE headHunter SET " + kind + "= ? WHERE nickName=?");
            ps.setString(1, value);
            ps.setString(2, player);

            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }

    public void setValue(String player, String table, String kind, int value) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("UPDATE " + table + " SET " + kind + "= ? WHERE nickName=?");
            ps.setInt(1, value);
            ps.setString(2, player);

            ps.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
    }

    public int getValue(Player player, String kind) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM headHunter WHERE nickName=?");
            ps.setString(1, player.getName());
            ResultSet rs = ps.executeQuery();
            int val;
            if (rs.next()) {
                val = rs.getInt(kind);
                return val;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return 0;
    }
    public int getValue(String player, String table, String kind) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM " + table + " WHERE nickName=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            int val;
            if (rs.next()) {
                val = rs.getInt(kind);
                return val;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return 0;
    }
    public int getValue(String player, String kind) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM headHunter WHERE nickName=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            int val;
            if (rs.next()) {
                val = rs.getInt(kind);
                return val;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return 0;
    }
    public String getString(String player, String kind) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM headHunter WHERE nickName=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            String str;
            if (rs.next()) {
                str = rs.getString(kind);
                return str;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return "";
    }


    public boolean getBoolean(Player player, String table, String kind) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM " + table + " WHERE nickName=?");
            ps.setString(1, player.getName());
            ResultSet rs = ps.executeQuery();
            boolean val;
            if (rs.next()) {
                val = rs.getBoolean(kind);
                return val;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return false;
    }
    public boolean getBoolean(String player, String table, String kind) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM " + table + " WHERE nickName=?");
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            boolean val;
            if (rs.next()) {
                val = rs.getBoolean(kind);
                return val;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return false;
    }
    public Timestamp getTimeStamp(Player player, String kind){
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT " + kind + " FROM headHunter WHERE nickName=?");
            ps.setString(1, player.getName());
            ResultSet rs = ps.executeQuery();
            Timestamp val;
            if (rs.next()) {
                val = rs.getTimestamp(kind);
                return val;
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return new Timestamp(0);
    }

    public void createTask(String player, int money, boolean byPlayer) {
        try {
            if (!exists("headHunterTasks", player)) {
                PreparedStatement ps2 = manager.getConnection().prepareStatement("INSERT IGNORE INTO headHunterTasks (nickName, money, byPlayer) VALUES (?, ?, ?)");
                ps2.setString(1, player);
                ps2.setInt(2, money);
                ps2.setBoolean(3, byPlayer);

                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void sortTasksByMoney(){
        try {
            manager.getConnection().prepareStatement("SELECT * FROM headHunterTasks ORDER BY money DESC");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMain(AllMain main) {
        this.main = main;
    }

    public List<Task> getTasks(){
        sortTasksByMoney();

        List<Task> tasks = new ArrayList<>();

        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT * FROM headHunterTasks");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nick = rs.getString(1);
                int money = rs.getInt(2);

                Task task = new Task(nick, money, main);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    public List<String> getTasksNicks(){
        List<String> nicks = new ArrayList<>();

        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT * FROM headHunterTasks");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nick = rs.getString(1);

                nicks.add(nick);
            }
            return nicks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void removeTask(String nickname){
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("DELETE FROM headHunterTasks WHERE nickName = ?");
            ps.setString(1, nickname);

            ps.executeUpdate();

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getHeads(Player player){
        String[] val;
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT storedHeads FROM headHunter WHERE nickName=?");
            ps.setString(1, player.getName());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                val = rs.getString("storedHeads")
                        .split(" ");

                List<String> res = new ArrayList<>();

                Collections.addAll(res, val);

                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String getCommands(){
        String cmds = "";
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT * FROM headHunterExecutor WHERE server = ?");
            ps.setInt(1, 1);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cmds;
    }
    public void removeCommands(){
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("DELETE FROM headHunterExecutor WHERE server = ?");
            ps.setInt(1, 1);

            ps.executeUpdate();

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void createCommand(String command, int server) {
        try {
            if (!exists(command)) {
                PreparedStatement ps2 = manager.getConnection().prepareStatement("INSERT IGNORE INTO headHunterExecutor (command, server) VALUES (?, ?)");
                ps2.setString(1, command);
                ps2.setInt(2, server);

                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String command) {
        try {
            PreparedStatement ps = manager.getConnection().prepareStatement("SELECT * FROM headHunterExecutor WHERE command=?");
            ps.setString(1, command);

            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                //player is found
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void removeHeads(String head){
        String command = "UPDATE headHunter SET storedHeads = " +
                "CASE " +
                "WHEN storedHeads LIKE ? THEN REPLACE(storedHeads, ?, '') " +
                "ELSE REPLACE(storedHeads, ?, '') " +
                "END " +
                "WHERE storedHeads LIKE ?";

        try {
            PreparedStatement ps = manager.getConnection().prepareStatement(command);

            ps.setString(1, "% " + head + "%");
            ps.setString(2, " " + head);
            ps.setString(3, head);
            ps.setString(4, "%" + head + "%");

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHead(Player player, String head) {
        String command =
                "UPDATE headHunter " +
                "SET storedHeads = " +
                "CASE " +
                "WHEN storedHeads IS NULL OR storedHeads = '' THEN ? " +
                "WHEN storedHeads LIKE '% %' THEN CONCAT(storedHeads, ?) " +
                "WHEN storedHeads LIKE ? THEN storedHeads " +
                "ELSE CONCAT(storedHeads, ?) " +
                "END " +
                "WHERE nickName = ?";

        try {
            PreparedStatement statement = manager.getConnection().prepareStatement(command);

            statement.setString(1, head);
            statement.setString(2, " " + head);
            statement.setString(3, "%" + head + "%");
            statement.setString(4," " +  head);
            statement.setString(5, player.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean hasCooldown(Player player){
        String command = "SELECT * FROM headHunter WHERE nickName = ?";

        try {
            PreparedStatement ps = manager.getConnection().prepareStatement(command);
            ps.setString(1, player.getName());

            ResultSet rs = ps.executeQuery();
            Timestamp val;
            if (rs.next()) {
                val = rs.getTimestamp("taskCoolDown");
                if(val == null){
                    return false;
                }

                if(val.getTime() < System.currentTimeMillis()){
                    setValue(player, "taskCoolDown", new Timestamp(0));
                }

                return val.getTime() > System.currentTimeMillis();
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        return false;
    }
    public void startCooldown(Player player){
        String command = "UPDATE headHunter SET taskCoolDown = DATE_ADD(NOW(), INTERVAL 24 HOUR) WHERE nickName = ?";
        try {
            PreparedStatement statement = manager.getConnection().prepareStatement(command);
            statement.setString(1, player.getName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
