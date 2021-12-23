package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketUpdateGun {
    private final int slot;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketUpdateGun(int slot) {
        this.slot = slot;
    }

    public static void encode(PacketUpdateGun msg, PacketBuffer buffer) {
        buffer.writeInt(msg.slot);
    }

    public static PacketUpdateGun decode(PacketBuffer buffer) {
        return new PacketUpdateGun(buffer.readInt());
    }

    public static class Handler {
        public static void handle(PacketUpdateGun msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack stack = BurnerGunMK2.getGun(player);
                if (stack == ItemStack.EMPTY)
                    return;
                IItemHandler handler = BurnerGunMK2.getHandler(stack);
                LOGGER.info(handler.getStackInSlot(msg.slot));
                handler.insertItem(msg.slot, handler.getStackInSlot(msg.slot), true);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}