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
        if (MinecraftVersion.isAtLeast(MinecraftVersion.v1_16_0)) {
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
        strings.replaceAll(ColourUtils::format);
        return strings;
    }

    /**
     * Strips all colours off all elements of a list.
     *
     * @param strings List of strings to strip colours from.
     * @return List of strings with no colours.
     */
    public List<String> stripColor(List<String> strings) {
        strings.replaceAll(ChatColor::stripColor);
        return strings;
    }

    /**
     * Removes a coloured line from a list of strings, ignoring the actual colours.
     *
     * @param source List to remove from.
     * @param forRemoval  String to remove.
     */
    public void removeLineIgnoreColour(List<String> source, String forRemoval) {
        for (int i = 0; i < source.size(); i++) {
            String str = source.get(i);
            if (ChatColor.stripColor(str).equals(ChatColor.stripColor(forRemoval))) {
                source.remove(i);
                i--;
            }
        }
    }

    /**
     * Removes all lines from a list of strings, ignoring the actual colours.
     *
     * @param source  List to remove from.
     * @param forRemoval Strings to remove.
     */
    public void removeLinesIgnoreColour(List<String> source, List<String> forRemoval) {
        forRemoval = stripColor(forRemoval);
        for (int i = 0; i < source.size(); i++) {
            String str = source.get(i);
            if (forRemoval.contains(ChatColor.stripColor(str))) {
                source.remove(i);
                i--;
            }
        }
    }

}
