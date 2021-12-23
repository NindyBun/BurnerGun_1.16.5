package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToggleSmeltFilter {
    public PacketToggleSmeltFilter() {
    }

    public static void encode(PacketToggleSmeltFilter msg, PacketBuffer buffer) {

    }

    public static PacketToggleSmeltFilter decode(PacketBuffer buffer) {
        return new PacketToggleSmeltFilter();
    }

    public static class Handler {
        public static void handle(PacketToggleSmeltFilter msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack stack = BurnerGunMK2.getGun(player);
                BurnerGunMK2Info info = BurnerGunMK2.getInfo(stack);
                // Active toggle feature
                info.setSmeltIsWhitelist(!info.getSmeltIsWhitelist());
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
