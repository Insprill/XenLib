package net.insprill.xenlib.files;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.insprill.xenlib.MinecraftVersion;
import net.insprill.xenlib.XenLib;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Wrapper class that stores nested {@link YamlFile}s in a folder.
 */
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class YamlFolder {

    public static final YamlFolder LOCALE = new YamlFolder("locale");

    private final Map<String, YamlFile> dataFiles = new HashMap<>();
    private final String folderName;
    @ToString.Include
    @EqualsAndHashCode.Include
    private final File folder;
    @ToString.Include
    private final boolean autoUpdate;

    /**
     * Creates a new YamlFolder.
     *
     * @param folderName Name of the root folder.
     */
    public YamlFolder(String folderName) {
        this(folderName, true);
    }

    /**
     * Creates a new YamlFolder.
     *
     * @param folderName Name of the root folder.
     * @param autoUpdate Toggles whether defaults that don't exist in the file should be written to disk.
     */
    public YamlFolder(String folderName, boolean autoUpdate) {
        this.folderName = folderName;
        this.autoUpdate = autoUpdate;
        File dir = XenLib.getPlugin().getDataFolder();
        folder = new File(dir.getAbsolutePath() + File.separator + folderName);
        init();
    }

    /**
     * Writes all sub-files from the jar to disk.
     */
    private void writeToDisk() {
        try {
            if (MinecraftVersion.isUnitTest()) {
                File folder = new File(String.join(File.separator, XenLib.getInstance().getUnitTestCompiledResourcesPath()) + File.separator + folderName);
                for (File file : folder.listFiles()) {
                    if (file.isDirectory())
                        continue;
                    String name = file.getPath();
                    int idx = name.indexOf(folderName);
                    if (idx == -1)
                        continue;
                    initDefaultFile(name.substring(idx));
                }
            } else {
                CodeSource src = XenLib.getPlugin().getClass().getProtectionDomain().getCodeSource();
                if (src == null) {
                    XenLib.getPlugin().getLogger().warning("Failed to find plugin's jar file. Unable to load all default files.");
                    return;
                }
                try (ZipInputStream zip = new ZipInputStream(src.getLocation().openStream())) {
                    ZipEntry e;
                    while ((e = zip.getNextEntry()) != null) {
                        initDefaultFile(e.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for YamlFolder#writeToDisk()
     */
    private void initDefaultFile(String name) {
        if (!isYamlFile(name))
            return;
        if (!name.startsWith(folderName))
            return;
        if (!name.contains("/") && !name.contains("\\"))
            return;
        File file = new File(XenLib.getPlugin().getDataFolder(), name);
        if (file.isDirectory())
            return;
        name = name.replace("/", File.separator);
        dataFiles.put(name, new YamlFile(file, autoUpdate));
    }

    /**
     * Creates folder, loads existing files if applicable, and writes default files if folder didn't already exist.
     */
    private void init() {
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        if (folder.listFiles().length == 0) {
            writeToDisk();
        } else {
            initExisting();
        }
    }

    /**
     * Reinitializes all YamlFile's.
     */
    public void reload() {
        dataFiles.clear();
        init();
    }

    /**
     * Creates YamlFile's for files that already exist in the directory.
     */
    private void initExisting() {
        for (File sub : getNestedFiles(folder)) {
            if (!isYamlFile(sub.getName()))
                continue;
            String fileName = sub.getPath().replace(XenLib.getPlugin().getDataFolder().getAbsolutePath() + File.separator, "");
            dataFiles.put(fileName, new YamlFile(sub, autoUpdate));
        }
    }

    /**
     * Checks if a file's extension is ".yml".
     *
     * @param fileName Name of file to check.
     * @return True if the files' extension is ".yml", false otherwise.
     */
    private boolean isYamlFile(String fileName) {
        if (!fileName.contains("."))
            return false;
        return fileName.substring(fileName.lastIndexOf('.')).toLowerCase(Locale.ROOT).equals(".yml");
    }

    /**
     * Gets a {@link YamlFile} from a file name.
     *
     * @param name Name of config file to get (with or without .yml).
     * @return DataFile associated with that file.
     */
    @Nullable
    public YamlFile getDataFile(String name) {
        name = name.replace("/", File.separator);
        name = (name.startsWith(folderName)) ? name : folderName + File.separator + name;
        name = (name.endsWith(".yml")) ? name : name + ".yml";
        return dataFiles.get(name);
    }

    /**
     * Gets a {@link YamlFile} from a file.
     *
     * @param file File to get.
     * @return DataFile associated with that file.
     */
    @Nullable
    public YamlFile getDataFile(File file) {
        String fileName = file.getPath().replace(XenLib.getPlugin().getDataFolder().getPath() + File.separator, "");
        return getDataFile(fileName);
    }

    /**
     * @return Collection of all {@link YamlFile}s in this DataFolder.
     */
    public Collection<YamlFile> getDataFiles() {
        return dataFiles.values();
    }

    /**
     * Gets all nested files in a folder.
     *
     * @param root Root folder to get all nested files from.
     * @return List of all nested files.
     */
    private static List<File> getNestedFiles(File root) {
        List<File> fileNames = new ArrayList<>();
        for (File f : root.listFiles()) {
            if (f.isDirectory()) {
                fileNames.addAll(getNestedFiles(f));
                continue;
            }
            fileNames.add(f);
        }
        return fileNames;
    }

}
