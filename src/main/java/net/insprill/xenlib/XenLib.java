package net.insprill.xenlib;

import net.insprill.xenlib.localization.Lang;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class XenLib {

    private static XenLib instance;
    private final JavaPlugin plugin;

    public XenLib(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;

        Lang.initConfig();
    }

    public static JavaPlugin getPlugin() {
        if (instance == null || instance.plugin == null) {
            Bukkit.getLogger().severe("XenLib plugin instance is null! Did you forget to initialize it in your onEnable?");
            return null;
        }
        return instance.plugin;
    }

}
