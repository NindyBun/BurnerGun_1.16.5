package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.BurnerGunInfo;
import com.nindybun.burnergun.common.capabilities.BurnerGunInfoProvider;
import com.nindybun.burnergun.common.containers.AutoFuelContainer;
import com.nindybun.burnergun.common.items.upgrades.Auto_Fuel.AutoFuel;
import com.nindybun.burnergun.common.items.upgrades.Auto_Fuel.AutoFuelHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Supplier;

public class PacketFuelValue {
    public int fuelValue;

    public PacketFuelValue(){
    }

    public PacketFuelValue(int fuelValue){
        this.fuelValue = fuelValue;
    }



    public static void encode(PacketFuelValue msg, PacketBuffer buffer) {
        buffer.writeInt(msg.fuelValue);
    }

    public static PacketFuelValue decode(PacketBuffer buffer) {
        return new PacketFuelValue(buffer.readInt());
    }

    public static class Handler {
        public static void handle(final PacketFuelValue msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                int nbt = ctx.get().getSender().getMainHandItem().getCapability(BurnerGunInfoProvider.burnerGunInfoCapability, null).orElseThrow(null).getFuelValue();
                msg.fuelValue = nbt;
                    });
        }
    }
}
