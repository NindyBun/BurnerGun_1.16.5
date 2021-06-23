package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.BurnerGunInfoProvider;
import com.nindybun.burnergun.common.capabilities.GDProvider;
import com.nindybun.burnergun.common.capabilities.IGD;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import com.nindybun.burnergun.common.items.GlitteringDiamond;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGDcount {
    public int count;

    public PacketGDcount(){
    }

    public PacketGDcount(int count){
        this.count = count;
    }



    public static void encode(PacketGDcount msg, PacketBuffer buffer) {
        buffer.writeInt(msg.count);
    }

    public static PacketGDcount decode(PacketBuffer buffer) {
        return new PacketGDcount(buffer.readInt());
    }

    public static class Handler {
        public static void handle(final PacketGDcount msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();;
                if (player == null)
                    return;

                ItemStack stack = ItemStack.EMPTY;
                if (player.getMainHandItem().getItem() instanceof GlitteringDiamond)
                    stack = player.getMainHandItem();

                if( stack.isEmpty() )
                    return;

                IGD igd = stack.getCapability(GDProvider.IGD_CAPABILITY).orElseThrow(null);
                igd.setCount(igd.getCount() + msg.count);
                    });

            ctx.get().setPacketHandled(true);
        }
    }
}
