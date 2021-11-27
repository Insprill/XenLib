package net.insprill.xenlib;

import net.insprill.xenlib.localization.Lang;
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
        return instance.plugin;
    }

}
