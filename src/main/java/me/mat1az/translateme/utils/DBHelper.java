package me.mat1az.translateme.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.sql.*;
import java.util.Objects;

public class DBHelper {

    protected static DBHelper instance;

    private final JavaPlugin plugin;

    public DBHelper(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static DBHelper getInstance(JavaPlugin plugin) {
        if (instance == null) {
            instance = new DBHelper(plugin);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private Connection connection;
    public static final String DB_NAME = "database.db";
    private String dbPath;

    public void initDBInstance() {
        try {
            this.dbPath = plugin.getDataFolder() + File.separator + DB_NAME;
            if (new File(plugin.getDataFolder(), DB_NAME).exists()) {
                String url = "jdbc:sqlite::memory:";
                connection = DriverManager.getConnection(url);
                connection.createStatement().executeUpdate("restore from " + dbPath);
                connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON");
                plugin.getLogger().info("Restored the database from " + dbPath + " to memory.");
            } else {
                String url = "jdbc:sqlite:" + dbPath;
                connection = DriverManager.getConnection(url);
                connection.createStatement().executeUpdate(new String(Objects.requireNonNull(plugin.getResource("database.sql")).readAllBytes()));
                plugin.getLogger().info("Created " + dbPath + " file.");
                connection.close();
                initDBInstance();
            }
        } catch (SQLException | IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    /**
     * Backup DB instance to a file, closes DB instance, then re-setup.
     */
    public boolean backup() {
        try {
            if (connection != null) {
                connection.createStatement().executeUpdate("backup to " + dbPath);
                plugin.getLogger().info("Database saved.");
                return true;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        return false;
    }

    /**
     * DDL based queries with multiple rows.
     *
     * @param sql query to execute.
     * @return returns a JSONArray that contains JSONObject.
     */
    public JSONArray queryDDL(String sql) {
        //Bukkit.getServer().broadcastMessage("consulta bd "+new Exception().getStackTrace()[1].getClassName().split("\\.")[3]);
        JSONArray jsonArray = new JSONArray();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                JSONObject row = new JSONObject();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                jsonArray.add(row);
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage() + ", " + e.getSQLState());
        }
        return jsonArray;
    }

    /**
     * DDL based queries with one single row.
     *
     * @param sql query to execute.
     * @return returns a JSONObject.
     */
    public JSONObject singleDDL(String sql) {
        JSONObject object = new JSONObject();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                JSONObject row = new JSONObject();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                return row;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage() + ", " + e.getSQLState());
        }
        return object;
    }

    /**
     * DML based queries.
     *
     * @param sql query to execute.
     * @return returns a Boolean if it's success.
     */
    public int queryDML(PreparedStatement sql) {
        try {
            return sql.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        return 0;
    }

}
