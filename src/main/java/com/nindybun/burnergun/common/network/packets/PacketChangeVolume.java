package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.BurnerGunInfo;
import com.nindybun.burnergun.common.capabilities.BurnerGunInfoProvider;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import com.nindybun.burnergun.common.items.gunProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketChangeVolume {
    private float volume;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketChangeVolume(float volume){
        this.volume = volume;
    }

    public static void encode(PacketChangeVolume msg, PacketBuffer buffer){
        buffer.writeFloat(msg.volume);
    }

    public static PacketChangeVolume decode(PacketBuffer buffer){
        return new PacketChangeVolume(buffer.readFloat());
    }

    public static class Handler {
        public static void handle(PacketChangeVolume msg, Supplier<NetworkEvent.Context> ctx){
            ctx.get().enqueueWork( ()-> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack gun = BurnerGun.getGun(player);
                //BurnerGunInfo info = gun.getCapability(BurnerGunInfoProvider.burnerGunInfoCapability, null).orElseThrow(()->new IllegalArgumentException("No capability found!"));
                //gunProperties.setVolume(info, msg.volume);
                gunProperties.setVolume(gun, msg.volume);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
