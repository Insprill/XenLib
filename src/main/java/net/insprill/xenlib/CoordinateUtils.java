package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import org.bukkit.Chunk;

@UtilityClass
public class CoordinateUtils {

    /**
     * @param x X coordinate.
     * @param z Z coordinate.
     * @return Chunk coordinates packed into a long.
     */
    public long getChunkKey(final int x, final int z) {
        return ((long) z << 32) | (x & 0xFFFFFFFFL);
    }

    /**
     * @param chunkKey Chunk key from {@link CoordinateUtils#getChunkKey} or {@link Chunk#getChunkKey}.
     * @return X coordinate of the chunk.
     */
    public int getChunkX(final long chunkKey) {
        return (int) chunkKey;
    }

    /**
     * @param chunkKey Chunk key from {@link CoordinateUtils#getChunkKey} or {@link Chunk#getChunkKey}.
     * @return Z coordinate of the chunk.
     */
    public int getChunkZ(final long chunkKey) {
        return (int) (chunkKey >>> 32);
    }

    /**
     * Converts a world coordinate to the local coordinate in a chunk.
     *
     * @param i World coordinate.
     * @return Local chunk coordinate.
     */
    public int worldToChunkLocal(int i) {
        return i & 0xF;
    }

    /**
     * Returns the specified block coordinates packed into a long.
     *
     * @return The block's x, y, and z coordinates packed into a long value.
     */
    public static long getBlockKey(int x, int y, int z) {
        return ((long) x & 0x7FFFFFF) | (((long) z & 0x7FFFFFF) << 27) | ((long) y << 54);
    }

    /**
     * Returns the x component from a packed value.
     *
     * @param packed The packed value, as computed by {@link CoordinateUtils#getBlockKey(int, int, int)}
     * @return The x component from the packed value.
     * @see CoordinateUtils#getBlockKey(int, int, int)
     */
    public static int getBlockKeyX(long packed) {
        return (int) ((packed << 37) >> 37);
    }

    /**
     * Returns the y component from a packed value.
     *
     * @param packed The packed value, as computed by {@link CoordinateUtils#getBlockKey(int, int, int)}
     * @return The y component from the packed value.
     * @see CoordinateUtils#getBlockKey(int, int, int)
     */
    public static int getBlockKeyY(long packed) {
        return (int) (packed >> 54);
    }

    /**
     * Returns the z component from a packed value.
     *
     * @param packed The packed value, as computed by {@link CoordinateUtils#getBlockKey(int, int, int)}
     * @return The z component from the packed value.
     * @see CoordinateUtils#getBlockKey(int, int, int)
     */
    public static int getBlockKeyZ(long packed) {
        return (int) ((packed << 10) >> 37);
    }

}
