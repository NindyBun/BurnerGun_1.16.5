package com.nindybun.burnergun.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WorldUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    public static BlockRayTraceResult getLookingAt(World world, PlayerEntity player, RayTraceContext.FluidMode rayTraceFluid, double range) {
        Vector3d look = player.getLookAngle();
        Vector3d start = player.position().add(new Vector3d(0, player.getEyeHeight(), 0));

        Vector3d end = new Vector3d(player.getX() + look.x * range, player.getY() + player.getEyeHeight() + look.y * range, player.getZ() + look.z * range);
        RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, rayTraceFluid, player);
        return world.clip(context);
    }
}
