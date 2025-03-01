package com.awexer.agradientname.events;

import com.awexer.agradientname.utils.ColorUtils;
import com.awexer.agradientname.utils.DataUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        List<String> data = DataUtils.load(player);

        if (!data.isEmpty()) {
            String firstColor = data.get(0);
            String secondColor = data.get(1);

            String gradientName = ColorUtils.createGradientName(player.getName(), firstColor, secondColor);

            player.setDisplayName(gradientName);
            player.setPlayerListName(gradientName);
        }
    }
}
