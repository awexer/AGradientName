package com.awexer.agradientname.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&(#[a-fA-F0-9]{6})");

    public static String hex(String msg) {
        Matcher match = HEX_PATTERN.matcher(msg);
        while (match.find()) {
            String color = match.group(1);
            StringBuilder hexColor = new StringBuilder("§x");
            for (char c : color.substring(1).toCharArray()) {
                hexColor.append('§').append(c);
            }
            msg = msg.replace(match.group(), hexColor.toString());
        }
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return msg;
    }

    public static String createGradientName(String name, String startHex, String endHex) {
        StringBuilder gradient = new StringBuilder();
        int nameLength = name.length();

        int[] startRGB = hexToRgb(startHex);
        int[] endRGB = hexToRgb(endHex);

        for (int i = 0; i < nameLength; i++) {
            int r = startRGB[0] + (endRGB[0] - startRGB[0]) * i / (nameLength - 1);
            int g = startRGB[1] + (endRGB[1] - startRGB[1]) * i / (nameLength - 1);
            int b = startRGB[2] + (endRGB[2] - startRGB[2]) * i / (nameLength - 1);

            String hex = String.format("#%02x%02x%02x", r, g, b);

            gradient.append(MessageUtils.buildmsg("&")).append(hex).append(name.charAt(i));
        }

        return gradient.toString();
    }
    public static int[] hexToRgb(String hex) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[]{r, g, b};
    }
}
