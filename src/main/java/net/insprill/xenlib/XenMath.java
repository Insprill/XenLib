package net.insprill.xenlib;

public class XenMath {

    /**
     * Clamps the given value between the minimum int and maximum int provided. Returns the given value if it is within the min and max range.
     *
     * @param val The value to clamp between min and max.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return An int between the min and max values.
     */
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Clamps the given value between the minimum long and maximum long provided. Returns the given value if it is within the min and max range.
     *
     * @param val The value to clamp between min and max.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return A long between the min and max values.
     */
    public static long clamp(long val, long min, long max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Clamps the given value between the minimum float and maximum float provided. Returns the given value if it is within the min and max range.
     *
     * @param val The value to clamp between min and max.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return A float between the min and max values.
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Clamps the given value between the minimum double and maximum double provided. Returns the given value if it is within the min and max range.
     *
     * @param val The value to clamp between min and max.
     * @param min The minimum value.
     * @param max The maximum value.
     * @return A double between the min and max values.
     */
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Checks if a String is a valid Integer.
     *
     * @param val String to check.
     * @return True if the String is a valid Integer, false otherwise.
     */
    public static boolean isInteger(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a String is a valid Long.
     *
     * @param val String to check.
     * @return True if the String is a valid Long, false otherwise.
     */
    public static boolean isLong(String val) {
        try {
            Long.parseLong(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a String is a valid Float.
     *
     * @param val String to check.
     * @return True if the String is a valid Float, false otherwise.
     */
    public static boolean isFloat(String val) {
        try {
            Float.parseFloat(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a String is a valid Double.
     *
     * @param val String to check.
     * @return True if the String is a valid Double, false otherwise.
     */
    public static boolean isDouble(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
