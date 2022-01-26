package net.insprill.xenlib;

import org.bukkit.Chunk;

public class CoordinateUtils {

    /**
     * @param x X coordinate.
     * @param z Z coordinate.
     * @return Chunk coordinates packed into a long.
     */
    public static long getChunkKey(final int x, final int z) {
        return ((long) z << 32) | (x & 0xFFFFFFFFL);
    }

    /**
     * @param chunkKey Chunk key from {@link CoordinateUtils#getChunkKey} or {@link Chunk#getChunkKey}.
     * @return X coordinate of the chunk.
     */
    public static int getChunkX(final long chunkKey) {
        return (int) chunkKey;
    }

    /**
     * @param chunkKey Chunk key from {@link CoordinateUtils#getChunkKey} or {@link Chunk#getChunkKey}.
     * @return Z coordinate of the chunk.
     */
    public static int getChunkZ(final long chunkKey) {
        return (int) (chunkKey >>> 32);
    }

}
