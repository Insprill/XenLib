package net.insprill.xenlib;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class XenUtils {

    /**
     * Checks if a String representing an Enum is valid.
     *
     * @param enumType Type of enum.
     * @param name     Name to check.
     * @return True if the String is a valid enum, false otherwise.
     */
    public static <T extends Enum<T>> boolean isValidEnum(Class<T> enumType, String name) {
        try {
            Enum.valueOf(enumType, name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Registers a permission if it's not already registered.
     *
     * @param permission Permission to register.
     */
    public static void registerPermission(String permission) {
        registerPermission(permission, null);
    }

    /**
     * Register a permission if it's not already registered.
     *
     * @param permission Permission to register.
     * @param def        PermissionDefault to specify if not already registered.
     */
    public static void registerPermission(String permission, PermissionDefault def) {
        if (permission == null)
            return;
        if (Bukkit.getPluginManager().getPermission(permission) != null)
            return;
        Bukkit.getPluginManager().addPermission(new Permission(permission, def));
    }

}
