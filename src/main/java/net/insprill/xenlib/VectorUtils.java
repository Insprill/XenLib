package net.insprill.xenlib;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

@UtilityClass
public class VectorUtils {

    /**
     * @param entity Entity to get right direction of.
     * @return A Vector representing the right direction of the entity.
     */
    public Vector getEntityRight(Entity entity) {
        Vector direction = getDirectionNormalized(entity);
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
    }

    /**
     * @param entity Entity to get left direction of.
     * @return A Vector representing the left direction of the entity.
     */
    public Vector getEntityLeft(Entity entity) {
        Vector direction = getDirectionNormalized(entity);
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
    }

    /**
     * @param entity Entity to get direction of.
     * @return A vector representing the normalized direction the entity is facing.
     */
    private Vector getDirectionNormalized(Entity entity) {
        Location loc = entity instanceof LivingEntity
                ? ((LivingEntity) entity).getEyeLocation()
                : entity.getLocation();
        return loc.getDirection().normalize();
    }

}
