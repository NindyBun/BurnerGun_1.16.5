package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.BurnerGunInfo;
import com.nindybun.burnergun.common.capabilities.BurnerGunInfoProvider;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import com.nindybun.burnergun.common.items.GlitteringDiamond;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketGlitteringDiamond {

    private CompoundNBT nbt;

    public PacketGlitteringDiamond(CompoundNBT nbt){
        this.nbt = nbt;
    }

    public static void encode(PacketGlitteringDiamond msg, PacketBuffer buffer) {
        buffer.writeNbt(msg.nbt);
    }

    public static PacketGlitteringDiamond decode(PacketBuffer buffer) {
        return new PacketGlitteringDiamond(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(PacketGlitteringDiamond msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack stack = ItemStack.EMPTY;
                if (player.getMainHandItem().getItem() instanceof GlitteringDiamond)
                    stack = player.getMainHandItem();
                else if (player.getOffhandItem().getItem() instanceof GlitteringDiamond)
                    stack = player.getOffhandItem();

                if( stack.isEmpty() )
                    return;
                CompoundNBT stackNBT = stack.getOrCreateTag();
                stackNBT.putInt("Use", msg.nbt.getInt("Use")+1);
                stack.setTag(stackNBT);
                //info.setVolume(msg.data.getFloat("Volume"));

            });

            ctx.get().setPacketHandled(true);
        }
    }




}
