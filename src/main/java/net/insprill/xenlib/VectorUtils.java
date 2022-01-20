package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

@UtilityClass
public class VectorUtils {

    /**
     * @param entity {@link Entity} to get forward direction of.
     * @return A {@link Vector} representing the forward direction of the entity.
     */
    public Vector getEntityForward(Entity entity) {
        Vector direction = getDirectionNormalized(entity);
        return new Vector(direction.getX(), 0.0, direction.getZ());
    }

    /**
     * @param entity {@link Entity} to get backward direction of.
     * @return A {@link Vector} representing the backward direction of the entity.
     */
    public Vector getEntityBackward(Entity entity) {
        Vector direction = getDirectionNormalized(entity);
        return new Vector(-direction.getX(), 0.0, -direction.getZ());
    }

    /**
     * @param entity {@link Entity} to get right direction of.
     * @return A {@link Vector} representing the right direction of the entity.
     */
    public Vector getEntityRight(Entity entity) {
        Vector direction = getDirectionNormalized(entity);
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
    }

    /**
     * @param entity {@link Entity} to get left direction of.
     * @return A {@link Vector} representing the left direction of the entity.
     */
    public Vector getEntityLeft(Entity entity) {
        Vector direction = getDirectionNormalized(entity);
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
    }

    /**
     * Gets the direction from one {@link Entity} to another.
     *
     * @param start {@link Entity} to start at.
     * @param end   {@link Entity} to end at.
     * @return Direction from starting {@link Entity} to ending {@link Entity}.
     */
    public Vector getDirectionTo(Entity start, Entity end) {
        if (start.getWorld() != end.getWorld())
            return new Vector();
        return start.getLocation().toVector().subtract(end.getLocation().toVector());
    }

    /**
     * @param entity {@link Entity} to get direction of.
     * @return A {@link Vector} representing the normalized direction the entity is facing.
     */
    private Vector getDirectionNormalized(Entity entity) {
        Location loc = entity instanceof LivingEntity
                ? ((LivingEntity) entity).getEyeLocation()
                : entity.getLocation();
        return loc.getDirection().normalize();
    }

    /**
     * Clamps all values of a {@link Vector} between 2 values.
     *
     * @param vector {@link Vector} to clamp.
     * @param min    Minimum value any axis should be.
     * @param max    Maximum value any axis should be.
     * @return A clamped {@link Vector},
     */
    public Vector clamp(Vector vector, double min, double max) {
        return new Vector(
                XenMath.clamp(vector.getX(), min, max),
                XenMath.clamp(vector.getY(), min, max),
                XenMath.clamp(vector.getZ(), min, max)
        );
    }

}
