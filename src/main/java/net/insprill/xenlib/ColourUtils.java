package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColourUtils {

    public static final Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
    public static final Pattern formattedHexPattern = Pattern.compile("[?:{<&]?#[a-fA-F0-9]{6}[}>]?");

    /**
     * Replaces colour codes with actual colours, and if on 1.16+, hex colour codes.
     *
     * @param string String to insert colours on.
     * @return String with colours.
     */
    public String format(String string) {
        if (string == null || string.isEmpty()) return string;
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_R1)) {
            Matcher formattedMatcher = formattedHexPattern.matcher(string);
            while (formattedMatcher.find()) {
                String hex = formattedMatcher.group();
                Matcher hexMatcher = hexPattern.matcher(hex);
                if (hexMatcher.find()) {
                    string = string.replace(formattedMatcher.group(), ChatColor.of(hexMatcher.group()).toString());
                    formattedMatcher.reset(string);
                }
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
    public List<String> format(List<String> strings) {
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
    public List<String> stripColor(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            strings.set(i, ChatColor.stripColor(strings.get(i)));
        }
        return strings;
    }

}
