package com.awexer.agradientname.utils;

import com.awexer.agradientname.AGradientName;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    private static final String SQLITE_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS player_colors (" +
                    "player_name VARCHAR(16) PRIMARY KEY, " +
                    "first_color VARCHAR(7), " +
                    "second_color VARCHAR(7)" +
                    ")";

    private static final String MYSQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS player_colors (" +
                    "player_name VARCHAR(16) PRIMARY KEY, " +
                    "first_color VARCHAR(7), " +
                    "second_color VARCHAR(7)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

    public static Connection getSqliteConnection() { return sqliteConnection; }

    public static Connection getMysqlConnection() { return mysqlConnection; }

    private static Connection sqliteConnection;
    private static Connection mysqlConnection;

    public static void initDatabaseConnections() {
        try {
            AGradientName plugin = AGradientName.getInstance();
            String dataType = plugin.getDataType().name().toUpperCase();

            switch (dataType) {
                case "SQLITE":
                    sqliteConnection = DriverManager.getConnection(
                            "jdbc:sqlite:" + new File(plugin.getDataFolder(), "data.db")
                    );
                    sqliteConnection.createStatement().execute(SQLITE_CREATE_TABLE);
                    break;
                case "MYSQL":
                    String mysqlUrl = "jdbc:mysql://" +
                            plugin.getConfig().getString("mysql.host") + ":" +
                            plugin.getConfig().getInt("mysql.port") + "/" +
                            plugin.getConfig().getString("mysql.database");

                    mysqlConnection = DriverManager.getConnection(
                            mysqlUrl,
                            plugin.getConfig().getString("mysql.user"),
                            plugin.getConfig().getString("mysql.password")
                    );
                    mysqlConnection.createStatement().execute(MYSQL_CREATE_TABLE);
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            AGradientName.getInstance().getLogger().severe("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeConnections() {
        try {
            if (sqliteConnection != null) sqliteConnection.close();
            if (mysqlConnection != null) mysqlConnection.close();
        } catch (SQLException e) {
            AGradientName.getInstance().getLogger().severe("Error closing database connections: " + e.getMessage());
        }
    }
}
