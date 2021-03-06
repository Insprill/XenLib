package net.insprill.xenlib;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@EqualsAndHashCode
public class MinecraftVersion {

    private static final Pattern versionPattern = Pattern.compile("(?i)\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?(?: Pre-Release )?(\\d)?\\)");

    // region Versions
    public static final MinecraftVersion v1_8_0 = new MinecraftVersion(8, 0);
    public static final MinecraftVersion v1_8_1 = new MinecraftVersion(8, 1);
    public static final MinecraftVersion v1_8_2 = new MinecraftVersion(8, 2);
    public static final MinecraftVersion v1_8_3 = new MinecraftVersion(8, 3);
    public static final MinecraftVersion v1_8_4 = new MinecraftVersion(8, 4);
    public static final MinecraftVersion v1_8_5 = new MinecraftVersion(8, 5);
    public static final MinecraftVersion v1_8_6 = new MinecraftVersion(8, 6);
    public static final MinecraftVersion v1_8_7 = new MinecraftVersion(8, 7);
    public static final MinecraftVersion v1_8_8 = new MinecraftVersion(8, 8);
    public static final MinecraftVersion v1_9_0 = new MinecraftVersion(9, 0);
    public static final MinecraftVersion v1_9_1 = new MinecraftVersion(9, 1);
    public static final MinecraftVersion v1_9_2 = new MinecraftVersion(9, 2);
    public static final MinecraftVersion v1_9_3 = new MinecraftVersion(9, 3);
    public static final MinecraftVersion v1_9_4 = new MinecraftVersion(9, 4);
    public static final MinecraftVersion v1_10_0 = new MinecraftVersion(10, 0);
    public static final MinecraftVersion v1_10_1 = new MinecraftVersion(10, 1);
    public static final MinecraftVersion v1_10_2 = new MinecraftVersion(10, 2);
    public static final MinecraftVersion v1_11_0 = new MinecraftVersion(11, 0);
    public static final MinecraftVersion v1_11_1 = new MinecraftVersion(11, 1);
    public static final MinecraftVersion v1_11_2 = new MinecraftVersion(11, 2);
    public static final MinecraftVersion v1_12_0 = new MinecraftVersion(12, 0);
    public static final MinecraftVersion v1_12_1 = new MinecraftVersion(12, 1);
    public static final MinecraftVersion v1_12_2 = new MinecraftVersion(12, 2);
    public static final MinecraftVersion v1_13_0 = new MinecraftVersion(13, 0);
    public static final MinecraftVersion v1_13_1 = new MinecraftVersion(13, 1);
    public static final MinecraftVersion v1_13_2 = new MinecraftVersion(13, 2);
    public static final MinecraftVersion v1_14_0 = new MinecraftVersion(14, 0);
    public static final MinecraftVersion v1_14_1 = new MinecraftVersion(14, 1);
    public static final MinecraftVersion v1_14_2 = new MinecraftVersion(14, 2);
    public static final MinecraftVersion v1_14_3 = new MinecraftVersion(14, 3);
    public static final MinecraftVersion v1_14_4 = new MinecraftVersion(14, 4);
    public static final MinecraftVersion v1_15_0 = new MinecraftVersion(15, 0);
    public static final MinecraftVersion v1_15_1 = new MinecraftVersion(15, 1);
    public static final MinecraftVersion v1_15_2 = new MinecraftVersion(15, 2);
    public static final MinecraftVersion v1_16_0 = new MinecraftVersion(16, 0);
    public static final MinecraftVersion v1_16_1 = new MinecraftVersion(16, 1);
    public static final MinecraftVersion v1_16_2 = new MinecraftVersion(16, 2);
    public static final MinecraftVersion v1_16_3 = new MinecraftVersion(16, 3);
    public static final MinecraftVersion v1_16_4 = new MinecraftVersion(16, 4);
    public static final MinecraftVersion v1_16_5 = new MinecraftVersion(16, 5);
    public static final MinecraftVersion v1_17_0 = new MinecraftVersion(17, 0);
    public static final MinecraftVersion v1_17_1 = new MinecraftVersion(17, 1);
    public static final MinecraftVersion v1_18_0 = new MinecraftVersion(18, 0);
    public static final MinecraftVersion v1_18_1 = new MinecraftVersion(18, 1);
    public static final MinecraftVersion v1_18_2 = new MinecraftVersion(18, 2);
    public static final MinecraftVersion v1_19_0 = new MinecraftVersion(19, 0);
    public static final MinecraftVersion v1_19_1 = new MinecraftVersion(19, 1);
    // endregion

    @Getter
    private static final MinecraftVersion currentVersion = MinecraftVersion.findCurrentVersion();

    @Getter
    private final int major;
    @Getter
    private final int patch;
    @Getter
    private final int preRelease;

    public MinecraftVersion(int major) {
        this(major, 0, 0);
    }

    public MinecraftVersion(int major, int patch) {
        this(major, patch, 0);
    }

    /**
     * Gets the display name of a version. E.g. "1.8.8" or "1.18". Ignores pre-release versions.
     *
     * @return The display name of the version.
     */
    public String getDisplayName() {
        return "1." + major + ((patch == 0) ? "" : "." + patch);
    }

    /**
     * @return Whether the version is at least 1.13.
     */
    public static boolean isNew() {
        return currentVersion.getMajor() >= 13;
    }

    /**
     * @param version The version to check.
     * @return Whether the current server version matches the provided version.
     */
    public static boolean is(MinecraftVersion version) {
        return currentVersion.getMajor() == version.getMajor() && currentVersion.getPatch() == version.getPatch();
    }

    /**
     * @param version The version to check.
     * @return Whether the current server major version matches the provided major version.
     */
    public static boolean isMajor(MinecraftVersion version) {
        return currentVersion.getMajor() == version.getMajor();
    }

    /**
     * @param version The version to check.
     * @return Whether the current server version is newer than the provided version.
     */
    public static boolean isNewerThan(MinecraftVersion version) {
        return currentVersion.getMajor() > version.getMajor() && currentVersion.getPatch() > version.getPatch();
    }

    /**
     * @param version The version to check.
     * @return Whether the current server version is at least the provided version.
     */
    public static boolean isAtLeast(MinecraftVersion version) {
        return currentVersion.getMajor() >= version.getMajor() && currentVersion.getPatch() >= version.getPatch();
    }

    /**
     * @param version The version to check.
     * @return Whether the current server version is older than the provided version.
     */
    public static boolean isOlderThan(MinecraftVersion version) {
        return currentVersion.getMajor() < version.getMajor() && currentVersion.getPatch() < version.getPatch();
    }

    private static MinecraftVersion findCurrentVersion() {
        int major = 0;
        int patch = 0;
        int preRelease = 0;

        Matcher matcher = versionPattern.matcher(Bukkit.getVersion());
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            if (matchResult.groupCount() >= 2) {
                try {
                    major = Integer.parseInt(matchResult.group(2));
                } catch (Exception ignored) {
                }
            }
            if (matchResult.groupCount() >= 3) {
                try {
                    patch = Integer.parseInt(matchResult.group(3));
                } catch (Exception ignored) {
                }
            }
            if (matchResult.groupCount() >= 4) {
                try {
                    preRelease = Integer.parseInt(matcher.group(4));
                } catch (Exception ignored) {
                }
            }
        }

        // Fallback to attempt to get major version.
        if (major == 0) {
            String[] pckg = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            if (pckg.length >= 4) {
                String[] version = pckg[3].split("_");
                if (version.length >= 2) {
                    try {
                        major = Integer.parseInt(version[1]);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        return new MinecraftVersion(major, patch, preRelease);
    }

    /**
     * Attempts to get the CraftBukkit version from the server implementation's path.
     * If it cannot be found, it will return 'N/A'.
     *
     * @return CraftBukkit version.
     */
    public static String getCraftBukkitVersion() {
        String[] pckg = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        if (pckg.length >= 4) {
            return pckg[3];
        } else {
            return "N/A";
        }
    }

}
