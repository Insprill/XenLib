package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import net.insprill.xenlib.localization.Lang;
import net.insprill.xenlib.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public final class XenLib {

    private JavaPlugin plugin;

    public void init(JavaPlugin plugin) {
        XenLib.plugin = plugin;
        Lang.initConfig();
        Logger.setPlugin(plugin);
    }

    public JavaPlugin getPlugin() {
        if (plugin == null) {
            Bukkit.getLogger().severe("XenLib plugin instance is null! Did you forget to initialize it in your onEnable?");
            return null;
        }
        return plugin;
    }

    public boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

}
