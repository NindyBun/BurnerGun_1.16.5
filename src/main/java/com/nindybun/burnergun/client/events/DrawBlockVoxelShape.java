package com.nindybun.burnergun.client.events;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.CollisionBoxDebugRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, value = Dist.CLIENT)
public class DrawBlockVoxelShape {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onDrawBlockHighlightEvent(DrawHighlightEvent event ){
        /*Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack gun = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
        if (gun.isEmpty())
            return;
        BurnerGunMK1Info infoMK1 = BurnerGunMK1.getInfo(gun);
        BurnerGunMK2Info infoMK2 = BurnerGunMK2.getInfo(gun);
        BlockRayTraceResult rayTraceResult = WorldUtil.getLookingAt(player.level, player, RayTraceContext.FluidMode.NONE, infoMK1 != null ? infoMK1.getRaycastRange() : infoMK2.getRaycastRange());

        LOGGER.info(event.getTarget());*/
    }

}
