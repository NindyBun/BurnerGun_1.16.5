package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1InfoProvider;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketChangeSettings {
    private CompoundNBT nbt;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketChangeSettings(CompoundNBT nbt){
        this.nbt = nbt;
    }

    public static void encode(PacketChangeSettings msg, PacketBuffer buffer){
        buffer.writeNbt(msg.nbt);
    }

    public static PacketChangeSettings decode(PacketBuffer buffer){
        return new PacketChangeSettings(buffer.readNbt());
    }

    public static class Handler {
        public static void handle(PacketChangeSettings msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack gun = BurnerGunMK2.getGun(player);
                if (gun.isEmpty())
                    return;
                BurnerGunMK2Info info = BurnerGunMK2.getInfo(gun);
                info.setVolume(msg.nbt.getFloat("Volume"));
                info.setRaycastRange(msg.nbt.getInt("Raycast"));
                info.setVertical(msg.nbt.getInt("Vertical"));
                info.setHorizontal(msg.nbt.getInt("Horizontal"));
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
