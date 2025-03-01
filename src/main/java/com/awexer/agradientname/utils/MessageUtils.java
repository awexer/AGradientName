package com.awexer.agradientname.utils;

import org.bukkit.command.CommandSender;

public class MessageUtils {
    public static void sendMessage(CommandSender sender, String msg) {
        if (sender != null && msg != null && !msg.isEmpty()) {
            sender.sendMessage(buildmsg(msg));
        }
    }

    public static String buildmsg(String msg) {
        if (msg==null) {
            return "";
        }

        return ColorUtils.hex(msg);
    }
}
