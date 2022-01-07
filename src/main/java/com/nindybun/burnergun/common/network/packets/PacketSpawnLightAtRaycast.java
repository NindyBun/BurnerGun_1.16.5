package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Supplier;

public class PacketSpawnLightAtRaycast {
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketSpawnLightAtRaycast() {
    }

    public static void encode(PacketSpawnLightAtRaycast msg, PacketBuffer buffer) {
    }

    public static PacketSpawnLightAtRaycast decode(PacketBuffer buffer) {
        return new PacketSpawnLightAtRaycast();
    }

    public static class Handler {
        public static void handle(PacketSpawnLightAtRaycast msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack gun = !BurnerGunMK2.getGun(player).isEmpty() ? BurnerGunMK2.getGun(player) : BurnerGunMK1.getGun(player);
                if (gun.isEmpty())
                    return;
                BurnerGunMK1Info infoMK1 = BurnerGunMK1.getInfo(gun);
                BurnerGunMK2Info infoMK2 = BurnerGunMK2.getInfo(gun);
                List<Upgrade> upgrades = infoMK1 != null ? UpgradeUtil.getUpgradesFromNBT(infoMK1.getUpgradeNBTList()) : UpgradeUtil.getUpgradesFromNBT(infoMK2.getUpgradeNBTList());
                if (UpgradeUtil.containsUpgradeFromList(upgrades, Upgrade.LIGHT)){
                    BlockRayTraceResult ray = WorldUtil.getLookingAt(player.level, player, RayTraceContext.FluidMode.NONE, infoMK1 != null ? infoMK1.getRaycastRange() : infoMK2.getRaycastRange());
                    player.level.setBlockAndUpdate(player.level.getBlockState(ray.getBlockPos()) == Blocks.AIR.defaultBlockState() ? ray.getBlockPos() : ray.getBlockPos().relative(ray.getDirection()), ModBlocks.LIGHT.get().defaultBlockState());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}