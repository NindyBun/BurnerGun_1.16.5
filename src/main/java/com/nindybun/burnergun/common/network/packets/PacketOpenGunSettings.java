package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.common.containers.BurnerGunContainer;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGunHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.function.Supplier;

public class PacketOpenGunSettings {
    public PacketOpenGunSettings(){

    }

    public static void encode(PacketOpenGunSettings msg, PacketBuffer buffer) {
    }

    public static PacketOpenGunSettings decode(PacketBuffer buffer) {
        return new PacketOpenGunSettings();
    }

    public static class Handler {
        public static void handle(PacketOpenGunSettings msg, Supplier<NetworkEvent.Context> ctx) {
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

                ModScreens.openGadgetSettingsScreen(stack);

            });

            ctx.get().setPacketHandled(true);
        }
    }




}
