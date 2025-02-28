package com.awexer.agradientname.commands;

import com.awexer.agradientname.AGradientName;
import com.awexer.agradientname.utils.DataUtils;
import com.awexer.agradientname.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandListener implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("agradientname.admin")) {
            MessageUtils.sendMessage(sender, AGradientName.getInstance().getConfig().getString("messages.no-perm", "&4You don't have permission to execute this!"));
            return true;
        }

        if (args.length < 3) {
            MessageUtils.sendMessage(sender, AGradientName.getInstance().getConfig().getString("messages.usage", "&6Invalid usage! Try /%cmd% <name> <color1> <color2>").replace("%cmd%", label));
            return true;
        }

        String playerName = args[0];
        String color1 = args[1];
        String color2 = args[2];

        if (isValidHex(color1) || isValidHex(color2)) {
            MessageUtils.sendMessage(sender, AGradientName.getInstance().getConfig().getString("messages.invalid-color", "&4Only HEX colors are supported!"));
            return true;
        }

        if (Bukkit.getPlayer(playerName) == null || !Objects.requireNonNull(Bukkit.getPlayer(playerName)).isOnline()) {
            MessageUtils.sendMessage(sender, AGradientName.getInstance().getConfig().getString("messages.offline", "&4Player is currently offline!"));
            return true;
        }

        DataUtils.save(Objects.requireNonNull(Bukkit.getPlayer(playerName)), color1, color2);

        MessageUtils.sendMessage(sender, AGradientName.getInstance().getConfig().getString("messages.successfully", "&aSuccessfully updated colors for %player%!").replace("%player%", playerName));
        return true;
    }

    private boolean isValidHex(String hex) {
        return !hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    }
}
