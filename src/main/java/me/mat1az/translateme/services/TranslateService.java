package me.mat1az.translateme.services;

import me.mat1az.translateme.models.Color;
import me.mat1az.translateme.models.UserColor;
import me.mat1az.translateme.utils.DBHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Color> getColors() {
        List<Color> colors = new ArrayList<>();
        String sql = "SELECT * FROM color";
        JSONArray array = dbHelper.queryDDL(sql);
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            Color color = new Color();
            color.setID(Integer.parseInt(String.valueOf(object.get("id"))));
            color.setValue(String.valueOf(object.get("value")));
            color.setLevel(Integer.parseInt(String.valueOf(object.get("level"))));
            colors.add(color);
        }
        return colors;
    }

    @Override
    public Color getColor(int id) {
        String sql = "SELECT * from color WHERE id = " + id;
        JSONObject o = dbHelper.singleDDL(sql);
        Color color = new Color();
        color.setID(Integer.parseInt(String.valueOf(o.get("id"))));
        color.setValue(String.valueOf(o.get("value")));
        color.setLevel(Integer.parseInt(String.valueOf(o.get("level"))));
        return color;
    }

    @Override
    public UserColor getUserColor(UUID player) {
        String sql = "SELECT * FROM users_colors WHERE user = '" + player + "'";
        JSONObject o = dbHelper.singleDDL(sql);
        if (!o.isEmpty()) {
            return new UserColor(player, (int) o.get("a"), (int) o.get("b"), (int) o.get("c"));
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
        String sql;
        UserColor uc;
        if (player != null) {
            sql = "SELECT value FROM message WHERE id = " + id + " AND language = (SELECT language FROM user WHERE uuid = '" + player + "')";
            uc = getUserColor(player);
        } else {
            sql = "SELECT value FROM message WHERE id = " + id + " AND language = " + plugin.getConfig().getInt("DEFAULT_LANG");
            Integer[] dColors = Objects.requireNonNull(plugin.getConfig().getList("DEFAULT_COLOR_SET")).toArray(new Integer[0]);
            uc = new UserColor(dColors[0], dColors[1], dColors[2]);
        }
        String message = String.valueOf(dbHelper.singleDDL(sql).get("value"));
        if (!message.equals("null")) {
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
            /*
            if (message.contains("!!!")) {
                message = message.replace("!!!", "");
                message = message.replace(message, uc.getB() + ">" + uc.getA() + " " + message);
                message = message.replace(message, message + " " + uc.getB() + "<");
            }
            //"**" means special item, this gives a prefix after & before the item name.
            if (message.contains("**")) {
                message = message.replace("**", "");
                message = message.replace(message, uc.getA() + "☠" + uc.getB() + " " + message);
                message = message.replace(message, message + " " + uc.getA() + "☠");
            }
            message = message
                    .replace("<", uc.getC() + "<" + uc.getB())
                    .replace(">", uc.getC() + ">" + uc.getA())
                    .replace("!!", uc.getB() + "▪ " + uc.getA())
                    .replace("¡¡", uc.getB() + "> ")
                    .replace("%.3", uc.getC()).replace("%.2", uc.getB()).replace("%.1", uc.getA());
             */
            return translate(message);
        }
        return "No message '" + id + "' in database.";
    }
}
