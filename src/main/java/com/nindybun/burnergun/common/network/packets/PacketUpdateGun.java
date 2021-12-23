package com.nindybun.burnergun.common.network.packets;

import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import com.nindybun.burnergun.util.UpgradeUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class PacketUpdateGun {
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketUpdateGun() {
    }

    public static void encode(PacketUpdateGun msg, PacketBuffer buffer) {
    }

    public static PacketUpdateGun decode(PacketBuffer buffer) {
        return new PacketUpdateGun();
    }

    public static class Handler {
        public static void handle(PacketUpdateGun msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;
                ItemStack stack = BurnerGunMK2.getGun(player);
                if (stack == ItemStack.EMPTY)
                    return;
                IItemHandler handler = BurnerGunMK2.getHandler(stack);
                BurnerGunMK2Info info = BurnerGunMK2.getInfo(stack);
                List<Upgrade> upgrades = new ArrayList<>();
                for (int i = 0; i < handler.getSlots(); i++) {
                    Item hItem = handler.getStackInSlot(i).getItem();
                    if (!hItem.equals(Items.AIR)){
                        Upgrade upgrade = ((UpgradeCard)hItem).getUpgrade();
                        if (upgrade.getBaseName().equals(Upgrade.FOCAL_POINT_1.getBaseName())){
                            info.setRaycastRange((int)upgrade.getExtraValue());
                            info.setMaxRaycastRange((int)upgrade.getExtraValue());
                        }
                        if (upgrade.getBaseName().equals(Upgrade.VERTICAL_EXPANSION_1.getBaseName())){
                            info.setVertical(0);
                            info.setMaxVertical(upgrade.getTier());
                        }
                        if (upgrade.getBaseName().equals(Upgrade.HORIZONTAL_EXPANSION_1.getBaseName())){
                            info.setHorizontal(0);
                            info.setMaxHorizontal(upgrade.getTier());
                        }
                        if ((upgrade.getBaseName().equals(Upgrade.FORTUNE_1.getBaseName()) && upgrades.contains(Upgrade.SILK_TOUCH) && upgrade.isActive())
                            || (upgrade.equals(Upgrade.SILK_TOUCH) && UpgradeUtil.getUpgradeFromList(upgrades, Upgrade.FORTUNE_1).isPresent() && upgrade.isActive())){
                                upgrade.setActive(!upgrade.isActive());
                        }
                        upgrades.add(((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade());
                    }
                }
                info.setUpgradeNBTList(UpgradeUtil.setUpgradesNBT(upgrades).getList("upgrades", Constants.NBT.TAG_COMPOUND));
            });

            ctx.get().setPacketHandled(true);
        }
    }
}