package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.containers.TrashContainer;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Trash.Trash;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashHandler;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class PacketOpenTrashGui {
    public PacketOpenTrashGui(){

    }

    public static void encode(PacketOpenTrashGui msg, PacketBuffer buffer) {
    }

    public static PacketOpenTrashGui decode(PacketBuffer buffer) {
        return new PacketOpenTrashGui();
    }

    public static class Handler {
        public static void handle(PacketOpenTrashGui msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();

                if (player == null)
                    return;

                ItemStack gun = BurnerGunMK2.getGun(player);
                ItemStack trash = ItemStack.EMPTY;
                if (gun != ItemStack.EMPTY){
                    BurnerGunMK2Info info = BurnerGunMK2.getInfo(gun);
                    List<Upgrade> upgradeList = UpgradeUtil.getUpgradesFromNBT(info.getUpgradeNBTList());
                    if (upgradeList.contains(Upgrade.TRASH))
                        trash = UpgradeUtil.getStackByUpgrade(gun, Upgrade.TRASH);
                }

                if (player.getMainHandItem().getItem() instanceof Trash)
                    trash = player.getMainHandItem();

                if( trash.isEmpty() )
                    return;

                IItemHandler handler = trash.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);

                player.openMenu(new SimpleNamedContainerProvider(
                        (windowId, playerInv, playerEntity) -> new TrashContainer(windowId, playerInv, (TrashHandler) handler),
                        new StringTextComponent("")
                ));

            });

            ctx.get().setPacketHandled(true);
        }
    }




}
