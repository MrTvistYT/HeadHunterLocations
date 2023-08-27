package org.gt.headhunterlocations.MySQL;

import org.gt.headhunterlocations.API.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {
    static Connection connection;

    public boolean isConnected(){
        return (connection != null);
    }
    public void setConnection(ConfigManager manager) throws SQLException, ClassNotFoundException{
        if(!isConnected()){
            connection = DriverManager.getConnection(manager.getUrl(), manager.getUserName(), manager.getPassword());
        }
    }
    public void removeConnection() throws SQLException, ClassNotFoundException{
        if(isConnected()){
            try {
                connection.close();
            }
            catch (SQLException er){
                er.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
