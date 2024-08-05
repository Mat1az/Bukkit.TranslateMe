package me.mat1az.translateme.services;

import me.mat1az.translateme.models.Color;
import me.mat1az.translateme.models.UserColor;
import me.mat1az.translateme.utils.DBHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO PreparedStatement on SQL Queries (SQL injection)

public class TranslateService implements TranslateInterface {

    private final DBHelper dbHelper;
    public static TranslateService instance;
    private final JavaPlugin plugin;

    public TranslateService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dbHelper = DBHelper.getInstance(plugin);
    }

    public static TranslateService getInstance() {
        return instance;
    }

    public boolean backup() {
        return dbHelper.backup();
    }

    @Override
    public int setLanguage(UUID player, int language) {
        try {
            String sql = "INSERT OR REPLACE INTO user (uuid,language) VALUES (?,?)";
            PreparedStatement ps = dbHelper.getConnection().prepareStatement(sql);
            ps.setString(1, String.valueOf(player));
            ps.setInt(2, language);
            return dbHelper.queryDML(ps);
        } catch (SQLException ignored) {}
        return 0;
    }


    @Override
    public int setUserColor(UUID player, UserColor uc) {
        try {
            String sql = "INSERT OR REPLACE INTO users_colors (user,a,b,c) VALUES (?,?,?,?)";
            PreparedStatement ps = dbHelper.getConnection().prepareStatement(sql);
            ps.setString(1, String.valueOf(player));
            ps.setInt(2, uc.getA());
            ps.setInt(3, uc.getB());
            ps.setInt(4, uc.getC());
            return dbHelper.queryDML(ps);
        } catch (SQLException ignored) {}
        return 0;
    }

    @Override
    public List<Color> getColors() {
        try {
            List<Color> colors = new ArrayList<>();
            PreparedStatement ps = dbHelper.getConnection().prepareStatement("SELECT * FROM color");
            ResultSet rs = dbHelper.queryDDL(ps);
            while (rs.next()) {
                Color color = new Color(rs.getInt("id"), rs.getInt("level"), rs.getString("value"));
                colors.add(color);
            }
            return colors;
        } catch (Exception ignored) {

        }
        return Collections.emptyList();
    }

    @Override
    public Color getColor(int id) {
        try {
            PreparedStatement ps = dbHelper.getConnection().prepareStatement("SELECT * from color WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = dbHelper.queryDDL(ps);
            if (rs.next()) {
                return new Color(rs.getInt("id"), rs.getInt("level"), rs.getString("value"));
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    @Override
    public UserColor getUserColor(UUID player) {
        try {
            Integer[] dColors = Objects.requireNonNull(plugin.getConfig().getList("DEFAULT_COLOR_SET")).toArray(new Integer[0]);
            PreparedStatement ps = dbHelper.getConnection().prepareStatement("SELECT * FROM users_colors WHERE user = ? UNION ALL SELECT ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM users_colors WHERE user = ?)");
            ps.setString(1, String.valueOf(player));
            ps.setString(2, String.valueOf(player));
            ps.setInt(3, dColors[0]);
            ps.setInt(4, dColors[1]);
            ps.setInt(5, dColors[2]);
            ps.setString(6, String.valueOf(player));
            ResultSet rs = dbHelper.queryDDL(ps);
            if (rs.next()) {
                return new UserColor(player, rs.getInt("a"), rs.getInt("b"), rs.getInt("c"));
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    @Override
    public String translate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Override
    public String getMessage(int id, UUID player) {
        try {
            String sql = "SELECT value FROM message WHERE id = ? AND language = (SELECT IFNULL((SELECT language FROM user WHERE uuid = ?), ?))";
            PreparedStatement ps = dbHelper.getConnection().prepareStatement(sql);
            UserColor uc = getUserColor(player);
            ps.setInt(1, id);
            ps.setString(2, String.valueOf(player));
            ps.setInt(3, plugin.getConfig().getInt("DEFAULT_LANG"));
            String message = dbHelper.singleDDL(ps);
            //This find %variables in message then gives a color.
            Matcher m = Pattern.compile("%[A-z]+").matcher(message);
            int i = 0;
            while (m.find()) {
                String s = m.group(i);
                //If variables contains %! it ignores to give color.
                if (!s.contains("%!")) {
                    message = message.replace(s, getColor(uc.getB()).getValue() + s + getColor(uc.getA()).getValue());
                    if (m.end() < i) {
                        ++i;
                    }
                }
            }
            return translate(message);
        } catch (SQLException ignored) {}
        return "";
    }
}
