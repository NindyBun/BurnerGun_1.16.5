package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.BurnerGunInfo;
import com.nindybun.burnergun.common.capabilities.BurnerGunInfoProvider;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFuelValue {
    public PacketFuelValue(){

    }
    private int fuelValue;

    public PacketFuelValue(int value){
        fuelValue = value;
    }

    public static void encode(PacketFuelValue msg, PacketBuffer buffer) {
        buffer.writeInt(msg.fuelValue);
    }

    public static PacketFuelValue decode(PacketBuffer buffer) {
        return new PacketFuelValue(buffer.readInt());
    }

    public static class Handler {
        public static void handle(PacketFuelValue msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack stack = ItemStack.EMPTY;
                if (player.getMainHandItem().getItem() instanceof BurnerGun)
                    stack = player.getMainHandItem();
                else if (player.getOffhandItem().getItem() instanceof BurnerGun)
                    stack = player.getOffhandItem();

                if( stack.isEmpty() )
                    return;

                BurnerGunInfo info = stack.getCapability(BurnerGunInfoProvider.burnerGunInfoCapability, null).orElseThrow(() -> new IllegalArgumentException(("LazyOptional must not be empty!")));
                info.setFuelValue(msg.fuelValue);

            });

            ctx.get().setPacketHandled(true);
        }
    }




}
