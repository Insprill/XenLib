package net.insprill.xenlib.files;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.insprill.xenlib.ColourUtils;
import net.insprill.xenlib.Conversions;
import net.insprill.xenlib.XenLib;
import net.insprill.xenlib.XenMath;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class YamlFile {

    public static final YamlFile CONFIG = new YamlFile("config.yml");

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    private final File file;
    @Getter
    private YamlConfiguration cfg;
    private YamlConfiguration internalCfg = new YamlConfiguration();
    @Getter
    @Accessors(chain = true)
    @ToString.Include
    private boolean autoUpdate = true;
    @Getter
    @ToString.Include
    private boolean modifiable = true;

    @Getter
    @ToString.Include
    private boolean isLoaded = false;

    /**
     * Creates a new YamlFile.
     *
     * @param name Name of the config file. Doesn't need to end with ".yml", but can.
     */
    public YamlFile(String name) {
        Preconditions.checkArgument(name != null && !name.isEmpty(), "Name cannot be null or empty");
        name = name.endsWith(".yml") ? name : name + ".yml";
        name = name.replace("/", File.separator).replace("\\", File.separator);
        file = new File(XenLib.getPlugin().getDataFolder() + File.separator + name);
        writeFileIfNotExists();
        reload();
        if (autoUpdate) {
            initInternalCfg();
        }
    }

    /**
     * Creates a new YamlFile.
     *
     * @param file File of the config.
     */
    public YamlFile(File file) {
        this.file = file;
        file.getParentFile().mkdirs();
        writeFileIfNotExists();
        reload();
        if (autoUpdate) {
            initInternalCfg();
        }
    }

    /**
     * Creates a new YamlFile.
     *
     * @param reader InputStreamReader to the internal file.
     */
    @SneakyThrows
    public YamlFile(InputStreamReader reader) {
        this.file = null;
        cfg = new YamlConfiguration();
        cfg.load(reader);
        internalCfg = cfg;
        isLoaded = true;
    }

    /**
     * Sets default values will automatically be written to the file.
     *
     * @param autoUpdate Whether this config auto updates.
     * @return This config.
     */
    public YamlFile setAutoUpdate(boolean autoUpdate) {
        if (autoUpdate && !modifiable)
            setModifiable(true);
        this.autoUpdate = autoUpdate;
        return this;
    }

    /**
     * Sets whether this config is modifiable.
     *
     * @param modifiable Whether this config is modifiable.
     * @return This config.
     */
    public YamlFile setModifiable(boolean modifiable) {
        if (!modifiable && autoUpdate)
            setAutoUpdate(false);
        this.modifiable = modifiable;
        return this;
    }


    /**
     * Writes the internal config to disk if it doesn't exist.
     */
    private synchronized void writeFileIfNotExists() {
        if (file == null || file.exists())
            return;
        file.getParentFile().mkdirs();
        String from = getInternalName(file);
        try (InputStream is = XenLib.getPlugin().getResource(from)) {
            if (is != null) {
                writeToFile(XenLib.getPlugin().getResource(from));
            } else {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes an internal copy of the default file if existent to get default values from.
     *
     * @return True if an internal copy of the file was found and loaded, false otherwise.
     */
    private synchronized boolean initInternalCfg() {
        String from = getInternalName(file);
        try (InputStream is = XenLib.getPlugin().getResource(from)) {
            if (is != null) {
                internalCfg = new YamlConfiguration();
                internalCfg.load(new InputStreamReader(is));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Writes an InputStream to a file.
     *
     * @param is InputStream to write.
     */
    private synchronized void writeToFile(InputStream is) {
        if (is == null)
            return;
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int i;
            StringBuilder fullMessage = new StringBuilder();
            while ((i = is.read()) != -1)
                fullMessage.append((char) i);
            byte[] strToBytes = fullMessage.toString().getBytes();
            outputStream.write(strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the config to disk.
     */
    public synchronized void save() {
        if (file == null)
            return;
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the config from disk.
     */
    public synchronized void reload() {
        if (file == null)
            return;
        if (!file.exists())
            writeFileIfNotExists();
        try {
            cfg = new YamlConfiguration();
            cfg.load(file);
            isLoaded = true;
        } catch (InvalidConfigurationException e) {
            XenLib.getPlugin().getLogger().severe("Failed to load config file \"" + getInternalName(file) + "\" as it has syntax errors! " +
                    "Use this website to find them: \"https://www.yamlchecker.com/\". ");
            onLoadFailure();
        } catch (IOException e) {
            e.printStackTrace();
            XenLib.getPlugin().getLogger().severe("Failed to load config file \"" + getInternalName(file) + "\"!");
            onLoadFailure();
        }
    }

    /**
     * Utility method for {@link #reload} when an exception is thrown.
     */
    private void onLoadFailure() {
        setAutoUpdate(false);
        isLoaded = false;
        if (initInternalCfg()) {
            cfg = internalCfg;
            isLoaded = true;
        }
    }

    /**
     * Resets the config to the internal version, or an empty config if no internal version is found.
     * Does <b>not</b> write to disk.
     */
    public synchronized void reset() {
        cfg = internalCfg;
    }

    /**
     * Gets the internal file name of a file.
     *
     * @param file File to get internal name of.
     * @return Internal name of a file. (e.g. "enchantments/test.yml").
     */
    private String getInternalName(File file) {
        return file.getAbsolutePath().replace(XenLib.getPlugin().getDataFolder().getAbsolutePath() + File.separator, "").replace(File.separatorChar, '/');
    }

    /**
     * @return The name of the file without its extension.
     */
    public String getName() {
        String fileName = getFile().getName();
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    //<editor-fold desc="Getters / Setters">

    /**
     * Checks if a path exists in the config.
     *
     * @param path Path to check.
     * @return True if the path exists, false otherwise.
     */
    public boolean contains(String path) {
        return cfg.contains(path);
    }

    /**
     * Checks if a path is a {@link ConfigurationSection} in the config.
     *
     * @param path Path to check.
     * @return True if the path exists and is a ConfigurationSection, false otherwise.
     */
    public boolean isConfigSection(String path) {
        return cfg.isConfigurationSection(path);
    }

    /**
     * Gets all keys (non-nested) of a ConfigurationSection.
     *
     * @param path Path to get keys from.
     * @return List of keys.
     */
    public Set<String> getKeys(String path) {
        ConfigurationSection section = getConfigSection(path);
        if (section == null)
            return Collections.emptySet();
        return section.getKeys(false);
    }

    /**
     * Gets the Class representing a config entry.
     *
     * @param path Path to the entry.
     * @return Class representing the entry, or null if it doesn't exist.
     */
    @Nullable
    public Class<?> getDataType(String path) {
        return getDataType(path, null);
    }

    /**
     * Gets the Class representing a config entry.
     *
     * @param path Path to the entry.
     * @param def  Default class if entry isn't in config.
     * @return Class representing the entry, or the default class if it doesn't exist.
     */
    @Nullable
    public Class<?> getDataType(String path, Class<?> def) {
        Object obj = get(path);
        return (obj == null) ? def : obj.getClass();
    }

    /**
     * Gets a {@link Object} from the config.
     *
     * @param path Path of the Object.
     * @return The Object from the config.
     */
    @Nullable
    public Object get(String path) {
        return get(path, internalCfg.get(path));
    }

    /**
     * Gets a {@link Object} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the Object.
     * @param def  Object to write if it doesn't exist.
     * @return The Object from the config, or the default if it doesn't exist.
     */
    @Nullable
    public Object get(String path, Object def) {
        update(path, def);
        return cfg.get(path, def);
    }

    /**
     * Gets a {@link Integer} from the config.
     *
     * @param path Path of the Integer.
     * @return The Integer from the config.
     */
    public int getInt(String path) {
        return getInt(path, internalCfg.getInt(path));
    }

    /**
     * Gets a {@link Integer} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the Integer.
     * @param def  Integer to write if it doesn't exist.
     * @return The Integer from the config, or the default if it doesn't exist.
     */
    public int getInt(String path, int def) {
        update(path, def);
        return cfg.getInt(path, def);
    }

    /**
     * Gets a {@link Long} from the config.
     *
     * @param path Path of the Long.
     * @return The Long from the config.
     */
    public long getLong(String path) {
        return getLong(path, internalCfg.getLong(path));
    }

    /**
     * Gets a {@link Long} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the Long.
     * @param def  Long to write if it doesn't exist.
     * @return The Long from the config, or the default if it doesn't exist.
     */
    public long getLong(String path, long def) {
        update(path, def);
        return cfg.getLong(path, def);
    }

    /**
     * Gets a {@link Double} from the config.
     *
     * @param path Path of the Double.
     * @return The Double from the config.
     */
    public double getDouble(String path) {
        return getDouble(path, internalCfg.getDouble(path));
    }

    /**
     * Gets a {@link Double} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the Double.
     * @param def  Double to write if it doesn't exist.
     * @return The Double from the config, or the default if it doesn't exist.
     */
    public double getDouble(String path, double def) {
        update(path, def);
        return cfg.getDouble(path, def);
    }

    /**
     * Gets a {@link Boolean} from the config.
     *
     * @param path Path of the Boolean.
     * @return The Boolean from the config.
     */
    public boolean getBoolean(String path) {
        return getBoolean(path, internalCfg.getBoolean(path));
    }

    /**
     * Gets a {@link Boolean} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the Boolean.
     * @param def  Boolean to write if it doesn't exist.
     * @return The Boolean from the config, or the default if it doesn't exist.
     */
    public boolean getBoolean(String path, boolean def) {
        update(path, def);
        return Conversions.toBoolean(cfg.get(path, def), def);
    }

    /**
     * Gets a {@link String} from the config with formatted colors.
     *
     * @param path Path of the String.
     * @return The String from the config.
     */
    @Nullable
    public String getString(String path) {
        return ColourUtils.format(getStringRaw(path));
    }

    /**
     * Gets a {@link String} from the config with formatted colors, or writes the default if it doesn't exist.
     *
     * @param path Path of the String.
     * @param def  String to write if it doesn't exist.
     * @return The String from the config, or the default if it doesn't exist.
     */
    @Nullable
    public String getString(String path, String def) {
        return ColourUtils.format(getStringRaw(path, def));
    }

    /**
     * Gets a {@link String} from the config without formatting colours.
     *
     * @param path Path of the String.
     * @return The String from the config, or the default if it doesn't exist.
     */
    @Nullable
    public String getStringRaw(String path) {
        return getStringRaw(path, internalCfg.getString(path));
    }

    /**
     * Gets a {@link String} from the config without formatting colours, or writes the default if it doesn't exist.
     *
     * @param path Path of the String.
     * @param def  String to write if it doesn't exist.
     * @return The String from the config, or the default if it doesn't exist.
     */
    @Nullable
    public String getStringRaw(String path, String def) {
        update(path, def);
        if (cfg.get(path) instanceof List) {
            return String.join("\n", cfg.getStringList(path));
        }
        return cfg.getString(path, def);
    }

    /**
     * Gets a {@link List<String>} from the config.
     *
     * @param path Path of the List.
     * @return The List from the config.
     */
    @NotNull
    public List<String> getStringList(String path) {
        return ColourUtils.format(getStringListRaw(path));
    }

    /**
     * Gets a {@link List<String>} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the List.
     * @param def  List to write if it doesn't exist.
     * @return The List from the config, or the default if it doesn't exist.
     */
    @NotNull
    public List<String> getStringList(String path, List<String> def) {
        return ColourUtils.format(getStringListRaw(path, def));
    }

    /**
     * Gets a {@link List<String>} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the List.
     * @return The List from the config, or the default if it doesn't exist.
     */
    @NotNull
    public List<String> getStringListRaw(String path) {
        return getStringListRaw(path, internalCfg.getStringList(path));
    }

    /**
     * Gets a {@link List<String>} from the config, or writes the default if it doesn't exist.
     *
     * @param path Path of the List.
     * @param def  List to write if it doesn't exist.
     * @return The List from the config, or the default if it doesn't exist.
     */
    @NotNull
    public List<String> getStringListRaw(String path, List<String> def) {
        if (update(path, def))
            return def;
        if (cfg.get(path) instanceof String) {
            String str = cfg.getString(path);
            if (str.contains("\n")) {
                return Arrays.asList(str.split("\n"));
            } else {
                return new ArrayList<>(Collections.singletonList(cfg.getString(path)));
            }
        }
        return cfg.getStringList(path);
    }

    /**
     * Gets a {@link List} from the config. Doesn't insert colours in Strings.
     *
     * @param path Path of the List.
     * @return The List from the config, or the default if it doesn't exist.
     */
    @Nullable
    public List<?> getList(String path) {
        return getList(path, null);
    }

    /**
     * Gets a {@link List} from the config, or writes the default if it doesn't exist. Doesn't insert colours in Strings.
     *
     * @param path Path of the List.
     * @param def  List to write if it doesn't exist.
     * @return The List from the config, or the default if it doesn't exist.
     */
    @Nullable
    public List<?> getList(String path, List<?> def) {
        update(path, def);
        return cfg.getList(path, def);
    }

    /**
     * Gets a {@link Color} colour the config.
     *
     * @param path Path of the colour.
     * @return A {@link Color} from the config, or white (255, 255, 255) if none is found.
     */
    public Color getColour(String path) {
        Matcher hexMatcher = ColourUtils.hexPattern.matcher(getString(path));
        if (hexMatcher.find()) {
            return Color.fromRGB(java.awt.Color.decode(hexMatcher.group()).getRGB());
        } else {
            int red = getInt(path + ".red", 255);
            int green = getInt(path + ".green", 255);
            int blue = getInt(path + ".blue", 255);
            return Color.fromRGB(
                    XenMath.clamp(red, 0, 255),
                    XenMath.clamp(green, 0, 255),
                    XenMath.clamp(blue, 0, 255)
            );
        }
    }

    /**
     * Gets a {@link ConfigurationSection} from the config.
     *
     * @param path Path of the ConfigurationSection.
     * @return The ConfigurationSection.
     */
    @Nullable
    public ConfigurationSection getConfigSection(String path) {
        return cfg.getConfigurationSection(path);
    }

    /**
     * Clears ALL keys from the configuration.
     */
    public void clear() {
        getKeys("").forEach(key -> set(key, null));
    }

    /**
     * Writes a value to the file if it doesn't exist.
     *
     * @param path Path to put the value.
     * @param def  The value to put.
     * @return True if the value didn't exist and was set to the default, false otherwise.
     */
    private boolean update(String path, Object def) {
        if (!contains(path)) {
            set(path, def);
            if (autoUpdate) {
                save();
            }
            return true;
        }
        return false;
    }

    /**
     * Writes an {@link Object} to the config.
     *
     * @param path Path to put the Object.
     * @param obj  Object to put.
     */
    public void set(String path, Object obj) {
        Preconditions.checkArgument(modifiable, "Cannot set values in a non-modifiable config");
        cfg.set(path, obj);
    }
    //</editor-fold>

}
