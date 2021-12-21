package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1InfoProvider;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFuelValue {

    private CompoundNBT data;

    public PacketFuelValue(CompoundNBT nbt){
        this.data = nbt;
    }

    public static void encode(PacketFuelValue msg, PacketBuffer buffer) {
        buffer.writeNbt(msg.data);
    }

    public static PacketFuelValue decode(PacketBuffer buffer) {
        return new PacketFuelValue(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(PacketFuelValue msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack stack = ItemStack.EMPTY;
                if (player.getMainHandItem().getItem() instanceof BurnerGunMK1)
                    stack = player.getMainHandItem();
                else if (player.getOffhandItem().getItem() instanceof BurnerGunMK1)
                    stack = player.getOffhandItem();

                if( stack.isEmpty() )
                    return;

                BurnerGunMK1Info info = stack.getCapability(BurnerGunMK1InfoProvider.burnerGunInfoMK1Capability, null).orElseThrow(() -> new IllegalArgumentException(("LazyOptional must not be empty!")));
                info.setFuelValue(msg.data.getInt("FuelValue"));
                info.setHeatValue(msg.data.getInt("HeatValue"));
                info.setCooldown(msg.data.getInt("CoolDown"));
                info.setHarvestLevel(msg.data.getInt("HarvestLevel"));
                //info.setVolume(msg.data.getFloat("Volume"));

            });

            ctx.get().setPacketHandled(true);
        }
    }




}
