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

}
