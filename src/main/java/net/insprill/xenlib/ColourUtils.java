package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColourUtils {

    public static final Pattern hexPattern = Pattern.compile("#[a-fA-F\\d]{6}");
    public static final Pattern formattedHexPattern = Pattern.compile("[?:{<&]?#[a-fA-F\\d]{6}[}>]?");

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
    public @Nullable List<String> format(@Nullable List<String> strings) {
        if (strings == null) return strings;
        strings.replaceAll(ColourUtils::format);
        return strings;
    }

    /**
     * Strips all colours off all elements of a list.
     *
     * @param strings List of strings to strip colours from.
     * @return List of strings with no colours.
     */
    public @Nullable List<String> stripColor(@Nullable List<String> strings) {
        if (strings == null) return strings;
        strings.replaceAll(ChatColor::stripColor);
        return strings;
    }

    /**
     * Removes a coloured line from a list of strings, ignoring the actual colours.
     *
     * @param source     List to remove from.
     * @param forRemoval String to remove.
     */
    public void removeLineIgnoreColour(@NotNull List<String> source, @Nullable String forRemoval) {
        if (forRemoval == null || forRemoval.isEmpty())
            return;
        source.removeIf(line -> ChatColor.stripColor(line).equals(ChatColor.stripColor(forRemoval)));
    }

    /**
     * Removes all lines from a list of strings, ignoring the actual colours.
     *
     * @param source     List to remove from.
     * @param forRemoval Strings to remove.
     */
    public void removeLinesIgnoreColour(@NotNull List<String> source, @Nullable List<String> forRemoval) {
        if (forRemoval == null)
            return;
        stripColor(forRemoval);
        source.removeIf(line -> forRemoval.contains(ChatColor.stripColor(line)));
    }

}
