package net.insprill.xenlib.localization;

import lombok.experimental.UtilityClass;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.files.YamlFile;
import net.insprill.xenlib.files.YamlFolder;
import org.bukkit.command.CommandSender;

import java.io.InputStreamReader;

@UtilityClass
public class Lang {

    private static final String PLACEHOLDER_SEPARATOR = ";";
    private static final String DEFAULT_LOCALE = "en-us";

    private static YamlFile defaultFile;

    /**
     * Initializes the default locale YamlFile.
     */
    public void initConfig() {
        defaultFile = new YamlFile(new InputStreamReader(XenLib.getPlugin().getResource("locale/" + DEFAULT_LOCALE + ".yml")));
    }

    /**
     * Gets a locale message.
     *
     * @param node         Path to the message in the locale file.
     * @param placeholders Placeholders to replace in the message before sending.
     * @return The message from the locale file with placeholders and colours filled in.
     */
    public String get(String node, String... placeholders) {
        YamlFile config = getLocaleConfig();
        String line = config.getString(node, defaultFile.getString(node));
        if (line == null) {
            XenLib.getPlugin().getLogger().severe("Tried to send locale message '" + node + "' but it doesn't exist! " +
                    "Please contact author(s) " + XenLib.getPlugin().getDescription().getAuthors() + " of " + XenLib.getPlugin().getName() + ".");
            return null;
        }
        line = line.replace("%p%", config.getString("prefix", defaultFile.getString("prefix")));
        for (String placeholder : placeholders) {
            String[] data = placeholder.split(PLACEHOLDER_SEPARATOR);
            line = line.replace(data[0], data[1]);
        }
        return line;
    }

    /**
     * Sends a locale message to CommandSender.
     *
     * @param sender       CommandSender to send message to.
     * @param node         Path to the message in the locale file.
     * @param placeholders Placeholders to replace in the message before sending.
     */
    public void send(CommandSender sender, String node, String... placeholders) {
        String line = get(node, placeholders);
        if (line == null)
            return;
        sender.sendMessage(get(node, placeholders));
    }

    /**
     * @return The YamlFile for the selected language, or the default if selected language doesn't exist.
     */
    private YamlFile getLocaleConfig() {
        String selectedLocale = YamlFile.CONFIG.getString("language", DEFAULT_LOCALE);
        YamlFile config = YamlFolder.LOCALE.getDataFile(selectedLocale);
        if (config == null) {
            config = YamlFolder.LOCALE.getDataFile(DEFAULT_LOCALE);
            XenLib.getPlugin().getLogger().severe("Locale file '" + selectedLocale + "' could not be found! Defaulting to " + DEFAULT_LOCALE + ".");
        }
        return config;
    }

}
