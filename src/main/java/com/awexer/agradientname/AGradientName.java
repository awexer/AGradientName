package com.awexer.agradientname;

import com.awexer.agradientname.commands.CommandListener;
import com.awexer.agradientname.placeholders.GradientNameExpansion;
import com.awexer.agradientname.utils.DatabaseUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class AGradientName extends JavaPlugin {

    public enum DataType {
        SQLITE, MYSQL, YAML
    }

    private static AGradientName instance;
    private DataType dataType;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        this.dataType = DataType.valueOf(getConfig().getString("data-type", "SQLITE").toUpperCase());
        Objects.requireNonNull(getCommand("agradientname")).setExecutor(new CommandListener());
        DatabaseUtils.initDatabaseConnections();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new GradientNameExpansion().register();
        }
    }

    @Override
    public void onDisable() {
        DatabaseUtils.closeConnections();
    }

    public static AGradientName getInstance() { return instance; }

    public DataType getDataType() {
        return dataType;
    }
}
