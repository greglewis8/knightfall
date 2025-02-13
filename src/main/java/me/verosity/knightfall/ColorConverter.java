package me.verosity.knightfall;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ColorConverter {

    public static final List<String> VALID_COLORS = new ArrayList<>(Arrays.asList(
            "black",
            "dark_blue",
            "dark_green",
            "dark_aqua",
            "dark_red",
            "dark_purple",
            "gold",
            "gray",
            "dark_gray",
            "blue",
            "green",
            "aqua",
            "red",
            "light_purple",
            "yellow",
            "white"
    ));

    public static String stringToColorCode(String stringColor) {
        switch (stringColor.toLowerCase(Locale.ROOT)) {
            case "black":
                return ChatColor.BLACK.toString();
            case "dark_blue":
                return ChatColor.DARK_BLUE.toString();
            case "dark_green":
                return ChatColor.DARK_GREEN.toString();
            case "dark_aqua":
                return ChatColor.DARK_AQUA.toString();
            case "dark_red":
                return ChatColor.DARK_RED.toString();
            case "dark_purple":
                return ChatColor.DARK_PURPLE.toString();
            case "gold":
                return ChatColor.GOLD.toString();
            case "gray":
                return ChatColor.GRAY.toString();
            case "dark_gray":
                return ChatColor.DARK_GRAY.toString();
            case "blue":
                return ChatColor.BLUE.toString();
            case "green":
                return ChatColor.GREEN.toString();
            case "aqua":
                return ChatColor.AQUA.toString();
            case "red":
                return ChatColor.RED.toString();
            case "light_purple":
                return ChatColor.LIGHT_PURPLE.toString();
            case "yellow":
                return ChatColor.YELLOW.toString();
            case "white":
                return ChatColor.WHITE.toString();
            default:
                return ChatColor.WHITE.toString(); // Default to white if invalid
        }
    }

    public static Material stringToBannerColor(String stringColor) {
        switch (stringColor.toLowerCase(Locale.ROOT)) {
            case "black":
                return Material.BLACK_BANNER;
            case "dark_blue":
                return Material.BLUE_BANNER; // No dark blue, using BLUE
            case "dark_green":
                return Material.GREEN_BANNER; // No dark green, using GREEN
            case "dark_aqua":
                return Material.CYAN_BANNER; // Closest match to dark aqua
            case "dark_red":
                return Material.RED_BANNER; // No dark red banner, using RED
            case "dark_purple":
                return Material.PURPLE_BANNER; // Closest match to dark purple
            case "gold":
                return Material.YELLOW_BANNER; // Gold is closest to yellow
            case "gray":
                return Material.LIGHT_GRAY_BANNER;
            case "dark_gray":
                return Material.GRAY_BANNER;
            case "blue":
                return Material.BLUE_BANNER;
            case "green":
                return Material.GREEN_BANNER;
            case "aqua":
                return Material.CYAN_BANNER;
            case "red":
                return Material.RED_BANNER;
            case "light_purple":
                return Material.PINK_BANNER; // Closest match to light purple
            case "yellow":
                return Material.YELLOW_BANNER;
            case "white":
                return Material.WHITE_BANNER;
            default:
                return Material.WHITE_BANNER; // Default to white banner if invalid
        }
    }

    public static BarColor stringToBossBarColor(String stringColor) {
        switch (stringColor.toLowerCase()) {
            case "blue":
            case "dark_blue":
            case "aqua":
            case "dark_aqua":
                return BarColor.BLUE;
            case "green":
            case "dark_green":
                return BarColor.GREEN;
            case "red":
            case "dark_red":
                return BarColor.RED;
            case "yellow":
            case "gold":
                return BarColor.YELLOW;
            case "purple":
            case "dark_purple":
                return BarColor.PURPLE;
            case "white":
            case "gray":
            case "dark_gray":
                return BarColor.WHITE;
            case "pink":
            case "light_purple":
                return BarColor.PINK;
            default:
                return BarColor.WHITE; // Default to WHITE if invalid
        }
    }

    public static String convertToColorString(String stringColor) {
        switch (stringColor.toLowerCase(Locale.ROOT)) {
            case "black": return "§0";
            case "dark_blue": return "§1";
            case "dark_green": return "§2";
            case "dark_aqua": return "§3";
            case "dark_red": return "§4";
            case "dark_purple": return "§5";
            case "gold": return "§6";
            case "gray": return "§7";
            case "dark_gray": return "§8";
            case "blue": return "§9";
            case "green": return "§a";
            case "aqua": return "§b";
            case "red": return "§c";
            case "light_purple": return "§d";
            case "yellow": return "§e";
            case "white": return "§f";
            default: return "§f"; // Default to white if invalid
        }
    }


}
