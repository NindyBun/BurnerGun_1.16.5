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
                List<Upgrade> currentUpgrades = new ArrayList<>();

                for (int i = 0; i < handler.getSlots(); i++) {
                    if (!handler.getStackInSlot(i).getItem().equals(Items.AIR))
                        currentUpgrades.add(((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade());
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FOCAL_POINT_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.FOCAL_POINT_1);
                    if (info.getRaycastRange() > upgrade.getExtraValue())
                        info.setRaycastRange((int)upgrade.getExtraValue());
                    info.setMaxRaycastRange((int)upgrade.getExtraValue());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FOCAL_POINT_1)){
                    if (info.getRaycastRange() > 5)
                        info.setRaycastRange(5);
                    if (info.getMaxRaycastRange() > 5)
                        info.setMaxRaycastRange(5);
                }

                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.VERTICAL_EXPANSION_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.VERTICAL_EXPANSION_1);
                    if (info.getVertical() > upgrade.getTier())
                        info.setVertical(upgrade.getTier());
                    info.setMaxVertical(upgrade.getTier());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.VERTICAL_EXPANSION_1)){
                    info.setVertical(0);
                    info.setMaxVertical(0);
                }
                LOGGER.info(info.getMaxVertical());//4
                if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1)){
                    Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1);
                    if (info.getHorizontal() > upgrade.getTier())
                        info.setHorizontal(upgrade.getTier());
                    info.setMaxHorizontal(upgrade.getTier());
                }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1)){
                    info.setHorizontal(0);
                    info.setMaxHorizontal(0);
                }
                LOGGER.info(info.getMaxVertical());//0
                currentUpgrades.forEach(upgrade -> {
                        if ((upgrade.lazyIs(Upgrade.FORTUNE_1) && upgrade.isActive() && currentUpgrades.contains(Upgrade.SILK_TOUCH))
                                || (upgrade.lazyIs(Upgrade.SILK_TOUCH) && upgrade.isActive() && UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FORTUNE_1))){
                            upgrade.setActive(!upgrade.isActive());
                        }
                    });

                LOGGER.info(info.getMaxVertical());//0
                info.setUpgradeNBTList(UpgradeUtil.setUpgradesNBT(currentUpgrades));

                /*if (!oldUpgrades.isEmpty()){
                    if (!currentUpgrades.isEmpty()){
                        oldUpgrades.forEach(upgrade -> {
                            if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, upgrade)){
                                if (upgrade.lazyIs(Upgrade.FOCAL_POINT_1)){
                                    info.setRaycastRange(5);
                                    info.setMaxRaycastRange(5);
                                }
                                if (upgrade.lazyIs(Upgrade.VERTICAL_EXPANSION_1)){
                                    info.setVertical(0);
                                    info.setMaxVertical(0);
                                }
                                if (upgrade.lazyIs(Upgrade.HORIZONTAL_EXPANSION_1)){
                                    info.setHorizontal(0);
                                    info.setMaxHorizontal(0);
                                }
                            }else if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, upgrade)){
                                if (upgrade.lazyIs(Upgrade.FOCAL_POINT_1)){
                                    if (info.getRaycastRange() > upgrade.getExtraValue())
                                        info.setRaycastRange((int)upgrade.getExtraValue());
                                    info.setMaxRaycastRange((int)upgrade.getExtraValue());
                                }
                                if (upgrade.lazyIs(Upgrade.VERTICAL_EXPANSION_1)){
                                    if (info.getVertical() > upgrade.getTier())
                                        info.setVertical(0);
                                    info.setMaxVertical(upgrade.getTier());
                                }
                                if (upgrade.lazyIs(Upgrade.HORIZONTAL_EXPANSION_1)){
                                    if (info.getHorizontal() > upgrade.getTier())
                                        info.setHorizontal(0);
                                    info.setMaxHorizontal(upgrade.getTier());
                                }
                            }
                        });
                    }else if (currentUpgrades.isEmpty()){
                        info.setRaycastRange(5);
                        info.setMaxRaycastRange(5);
                        info.setVertical(0);
                        info.setMaxVertical(0);
                        info.setHorizontal(0);
                        info.setMaxHorizontal(0);
                    }
                }else if (oldUpgrades.isEmpty()){
                    if (!currentUpgrades.isEmpty())
                        currentUpgrades.forEach(upgrade -> {
                            if (upgrade.lazyIs(Upgrade.FOCAL_POINT_1)){
                                info.setRaycastRange((int)upgrade.getExtraValue());
                                info.setMaxRaycastRange((int)upgrade.getExtraValue());
                            }
                            if (upgrade.lazyIs(Upgrade.VERTICAL_EXPANSION_1)){
                                info.setVertical(0);
                                info.setMaxVertical(upgrade.getTier());
                            }
                            if (upgrade.lazyIs(Upgrade.HORIZONTAL_EXPANSION_1)){
                                info.setHorizontal(0);
                                info.setMaxHorizontal(upgrade.getTier());
                            }
                            if ((upgrade.lazyIs(Upgrade.FORTUNE_1) && upgrade.isActive() && currentUpgrades.contains(Upgrade.SILK_TOUCH))
                                || (upgrade.lazyIs(Upgrade.SILK_TOUCH) && upgrade.isActive() && UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.FORTUNE_1))){
                                upgrade.setActive(!upgrade.isActive());
                            }
                        });
                }
                info.setUpgradeNBTList(UpgradeUtil.setUpgradesNBT(currentUpgrades));*/

                /*for (int i = 0; i < handler.getSlots(); i++) {
                    Item hItem = handler.getStackInSlot(i).getItem();
                    if (!hItem.equals(Items.AIR)){
                        Upgrade upgrade = ((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade();
                        if (!UpgradeUtil.getUpgradeFromList(oldUpgrades, Upgrade.FOCAL_POINT_1).isPresent() && upgrade.getBaseName().equals(Upgrade.FOCAL_POINT_1.getBaseName())){
                            info.setRaycastRange((int)upgrade.getExtraValue());
                            info.setMaxRaycastRange((int)upgrade.getExtraValue());
                        }
                        if (!UpgradeUtil.getUpgradeFromList(oldUpgrades, Upgrade.VERTICAL_EXPANSION_1).isPresent() && upgrade.getBaseName().equals(Upgrade.VERTICAL_EXPANSION_1.getBaseName())){
                            info.setVertical(0);
                            info.setMaxVertical(upgrade.getTier());
                        }
                        if (!UpgradeUtil.getUpgradeFromList(oldUpgrades, Upgrade.HORIZONTAL_EXPANSION_1).isPresent() && upgrade.getBaseName().equals(Upgrade.HORIZONTAL_EXPANSION_1.getBaseName())){
                            info.setHorizontal(0);
                            info.setMaxHorizontal(upgrade.getTier());
                        }
                        if ((upgrade.getBaseName().equals(Upgrade.FORTUNE_1.getBaseName()) && oldUpgrades.contains(Upgrade.SILK_TOUCH) && upgrade.isActive())
                            || (upgrade.equals(Upgrade.SILK_TOUCH) && UpgradeUtil.getUpgradeFromList(oldUpgrades, Upgrade.FORTUNE_1).isPresent() && upgrade.isActive())){
                                upgrade.setActive(!upgrade.isActive());
                        }
                        if (!UpgradeUtil.getUpgradeFromList(oldUpgrades, upgrade).isPresent())
                            newUpgrades.add(((UpgradeCard)handler.getStackInSlot(i).getItem()).getUpgrade());
                    }
                }
                info.setUpgradeNBTList(UpgradeUtil.setUpgradesNBT(oldUpgrades));*/
            });

            ctx.get().setPacketHandled(true);
        }
    }
}