package net.insprill.xenlib.logging;

import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

@UtilityClass
public class Logger {

    @Setter
    private Plugin plugin;

    public void info(String str) {
        if (plugin == null) return;
        plugin.getLogger().info(str);
    }

    public void warning(String str) {
        if (plugin == null) return;
        plugin.getLogger().warning(str);
    }

    public void severe(String str) {
        if (plugin == null) return;
        plugin.getLogger().severe(str);
    }

    public void severe(String str, Throwable e) {
        if (plugin == null) return;
        plugin.getLogger().log(Level.SEVERE, str, e);
    }

}
