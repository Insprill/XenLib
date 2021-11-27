package net.insprill.xenlib;

import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColourUtils {

    private static final Pattern hexPattern = Pattern.compile("[?:{&]#[a-fA-F0-9]{6}}?");
    private static final Pattern normalPattern = Pattern.compile("([\u00A7&])[0-9a-fA-Fk-orK-OR]");

    /**
     * Replaces colour codes with actual colours, and if on 1.16+, hex colour codes.
     *
     * @param string String to insert colours on.
     * @return String with colours.
     */
    public static String format(String string) {
        if (string == null || string.isEmpty()) return string;
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_R1)) {
            Matcher match = hexPattern.matcher(string);
            while (match.find()) {
                String hex = match.group().substring(1); // first character is either a '&' or '{', neither of which we want.
                if (hex.endsWith("}"))
                    hex = hex.substring(0, hex.length() - 1);
                string = string.replace(match.group(), ChatColor.of(hex).toString());
                match = hexPattern.matcher(string);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Replaces colour codes with actual colours, and if on 1.16+, hex colour codes.
     *
     * @param strings List of strings to insert colours on.
     * @return List of strings with colours.
     */
    public static List<String> format(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, format(strings.get(i)));
        }
        return strings;
    }

    /**
     * Strips all colours off all elements of a list.
     *
     * @param strings List of strings to strip colours from.
     * @return List of strings with no colours.
     */
    public static List<String> stripColor(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, ChatColor.stripColor(strings.get(i)));
        }
        return strings;
    }

    /**
     * Gets the last known colour of a string.
     *
     * @param string String to get last colour from.
     * @return Regular or HEX colour code of the last known colour from the provided String.
     */
    public static String getLastColor(String string) {
        String lastKnownColor = "";
        int end = 0;
        Matcher m = hexPattern.matcher(string);
        while (m.find()) {
            end = m.end();
            lastKnownColor = string.substring(m.start(), m.end());
        }
        Matcher m2 = normalPattern.matcher(string);
        while (m2.find()) {
            if (m2.end() < end)
                continue;
            lastKnownColor = string.substring(m2.start(), m2.end());
        }
        return lastKnownColor;
    }

}
