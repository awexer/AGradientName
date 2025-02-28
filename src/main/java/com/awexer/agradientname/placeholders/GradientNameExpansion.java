package com.awexer.agradientname.placeholders;

import com.awexer.agradientname.utils.ColorUtils;
import com.awexer.agradientname.utils.DataUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GradientNameExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "agradientname";
    }

    @Override
    public @NotNull String getAuthor() {
        return "awexer";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "";

        if ("gradient".equals(identifier)) {
            List<String> data = DataUtils.load(player);

            if (!data.isEmpty()) {
                String firstColor = data.get(0);
                String secondColor = data.get(1);

                return ColorUtils.createGradientName(player.getName(), firstColor, secondColor);
            }
        }

        return player.getName();
    }
}