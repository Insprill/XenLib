package net.insprill.xenlib;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

public enum MinecraftVersion {
    UNKNOWN(Integer.MAX_VALUE),
    v1_8_R3(1_8_3),
    v1_9_R1(1_9_1),
    v1_9_R2(1_9_2),
    v1_10_R1(1_10_1),
    v1_11_R1(1_11_1),
    v1_12_R1(1_12_1),
    v1_13_R1(1_13_1),
    v1_13_R2(1_13_2),
    v1_14_R1(1_14_1),
    v1_15_R1(1_15_0),
    v1_16_R1(1_16_0),
    v1_16_R2(1_16_1),
    v1_16_R3(1_16_3),
    v1_17_R1(1_17_0),
    v1_18_R1(1_18_0);

    @Getter
    private final int versionNumber;

    MinecraftVersion(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public static boolean is(MinecraftVersion version) {
        return currentVersion.versionNumber == version.getVersionNumber();
    }

    public static boolean isAtLeast(MinecraftVersion version) {
        return currentVersion.versionNumber >= version.getVersionNumber();
    }

    public static boolean isNew() {
        return isAtLeast(MinecraftVersion.v1_13_R1);
    }

    @Getter
    @Setter
    @SuppressWarnings("NonFinalFieldInEnum")
    private static boolean unitTest = false;

    @Getter
    private static MinecraftVersion currentVersion = MinecraftVersion.UNKNOWN;

    static {
        String[] pckg = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        if (pckg.length >= 4) {
            String versionString = pckg[3];
            try {
                currentVersion = MinecraftVersion.valueOf(versionString);
            } catch (IllegalArgumentException ex) {
                XenLib.getPlugin().getLogger().warning("Unknown minecraft version " + versionString + ".");
            }
        }
    }

}
