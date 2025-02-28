package com.awexer.agradientname.utils;

import com.awexer.agradientname.AGradientName;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    public static void save(@NotNull Player p, @NotNull String firstCol, @NotNull String secondCol) {
        String dataType = AGradientName.getInstance().getDataType().name();
        try {
            switch (dataType.toUpperCase()) {
                case ("SQLITE"):
                    try (PreparedStatement ps = DatabaseUtils.getSqliteConnection().prepareStatement(
                            "INSERT OR REPLACE INTO player_colors (player_name, first_color, second_color) VALUES (?, ?, ?)")) {
                        ps.setString(1, p.getName());
                        ps.setString(2, firstCol);
                        ps.setString(3, secondCol);
                        ps.executeUpdate();
                    }
                    break;
                case ("MYSQL"):
                    try (PreparedStatement ps = DatabaseUtils.getMysqlConnection().prepareStatement(
                            "INSERT INTO player_colors (player_name, first_color, second_color) " +
                                    "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE first_color = ?, second_color = ?")) {
                        ps.setString(1, p.getName());
                        ps.setString(2, firstCol);
                        ps.setString(3, secondCol);
                        ps.setString(4, firstCol);
                        ps.setString(5, secondCol);
                        ps.executeUpdate();
                    }
                    break;
                default:
                    File dataFile = new File(AGradientName.getInstance().getDataFolder(), "data.yml");
                    if (!dataFile.exists()) {
                        AGradientName.getInstance().saveResource("data.yml", false);
                    }
                    YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);
                    dataConfig.set("players." + p.getName() + ".firstColor", firstCol);
                    dataConfig.set("players." + p.getName() + ".secondColor", secondCol);
                    try {
                        dataConfig.save(dataFile);
                    } catch (IOException e) {
                        AGradientName.getInstance().getLogger().severe("Failed to save data.yml: " + e.getMessage());
                    }
                    break;
            }
            load(p);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> load(@NotNull Player p) {
        List<String> data = new ArrayList<>();
        String dataType = AGradientName.getInstance().getDataType().name();
        try {
            switch (dataType.toUpperCase()) {
                case ("SQLITE"):
                    try (PreparedStatement ps = DatabaseUtils.getSqliteConnection().prepareStatement(
                            "SELECT first_color, second_color FROM player_colors WHERE player_name = ?")) {
                        ps.setString(1, p.getName());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                data.add(rs.getString("first_color"));
                                data.add(rs.getString("second_color"));
                            }
                        }
                    }
                    break;
                case ("MYSQL"):
                    try (PreparedStatement ps = DatabaseUtils.getMysqlConnection().prepareStatement(
                            "SELECT first_color, second_color FROM player_colors WHERE player_name = ?")) {
                        ps.setString(1, p.getName());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                data.add(rs.getString("first_color"));
                                data.add(rs.getString("second_color"));
                            }
                        }
                    }
                    break;
                default:
                    File dataFile = new File(AGradientName.getInstance().getDataFolder(), "data.yml");
                    if (!dataFile.exists()) {
                        AGradientName.getInstance().saveResource("data.yml", false);
                    }
                    YamlConfiguration dataConfig = YamlConfiguration.loadConfiguration(dataFile);
                    if (dataConfig.contains("players." + p.getName())) {
                        data.add(dataConfig.getString("players." + p.getName() + ".firstColor", "#FFFFFF"));
                        data.add(dataConfig.getString("players." + p.getName() + ".secondColor", "#000000"));
                    }
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
