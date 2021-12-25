package com.nindybun.burnergun.util;

import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import jdk.nashorn.internal.ir.EmptyNode;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpgradeUtil {
    private static final String KEY_UPGRADES = "upgrades";
    private static final String KEY_UPGRADE = "upgrade";
    private static final String KEY_ENABLED = "enabled";

    public static ListNBT setUpgradesNBT(List<Upgrade> upgrades) {
        ListNBT list = new ListNBT();

        upgrades.forEach(upgrade -> {
            CompoundNBT compound = new CompoundNBT();
            compound.putString(KEY_UPGRADE, upgrade.getName());
            compound.putBoolean(KEY_ENABLED, upgrade.isActive());
            list.add(compound);
        });

        return list;
    }

    public static Upgrade getUpgradeByName(String name){
        try {
            Upgrade type = Upgrade.valueOf(name.toUpperCase());
            return type;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public static Optional<Upgrade> getUpgradeFromList(List<Upgrade> upgrades, Upgrade type){
        if (upgrades.isEmpty())
            return Optional.empty();
        return upgrades.stream().filter(e -> e.lazyIs(type)).findFirst();
    }

    public static Upgrade getUpgradeFromListByUpgrade(List<Upgrade> upgrades, Upgrade type){
        for (Upgrade upgrade: upgrades) {
            if (upgrade.lazyIs(type))
                return upgrade;
        }
        return null;
    }

    public static List<Upgrade> getUpgradesFromNBT(ListNBT upgrades){
        List<Upgrade> upgradeList = new ArrayList<>();
        if (upgrades.isEmpty())
            return upgradeList;
        for (int i = 0; i < upgrades.size(); i++) {
            CompoundNBT upgradeNBT = upgrades.getCompound(i);
            Upgrade type = getUpgradeByName(upgradeNBT.getString(KEY_UPGRADE));
            if (type == null)
                continue;
            type.setActive(!upgradeNBT.contains(KEY_ENABLED) || upgradeNBT.getBoolean(KEY_ENABLED));
            upgradeList.add(type);
        }
        return upgradeList;
    }

    public static List<Upgrade> getUpgradesFromGun(ItemStack stack){
        ListNBT nbt = BurnerGunMK2.getInfo(stack).getUpgradeNBTList();
        return getUpgradesFromNBT(nbt);
    }
    public static boolean containsUpgradeFromList(List<Upgrade> upgradeList, Upgrade upgrade){
        for (Upgrade index : upgradeList) {
            if (index.getBaseName().equals(upgrade.getBaseName()))
                return true;
        }
        return false;
    }

    public static void setValues(List<Upgrade> currentUpgrades, BurnerGunMK2Info info){
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

        if (UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1)){
            Upgrade upgrade = UpgradeUtil.getUpgradeFromListByUpgrade(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1);
            if (info.getHorizontal() > upgrade.getTier())
                info.setHorizontal(upgrade.getTier());
            info.setMaxHorizontal(upgrade.getTier());
        }else if (!UpgradeUtil.containsUpgradeFromList(currentUpgrades, Upgrade.HORIZONTAL_EXPANSION_1)){
            info.setHorizontal(0);
            info.setMaxHorizontal(0);
        }
    }

    public static List<Upgrade> getToggleableUpgrades(ItemStack stack){
        return getUpgradesFromGun(stack).stream().filter(Upgrade::isToggleable).collect(Collectors.toList());
    }

    public static List<Upgrade> getActiveUpgrades(ItemStack stack){
        return getUpgradesFromGun(stack).stream().filter(Upgrade::isActive).collect(Collectors.toList());
    }

    //Returns the upgrade card by upgrade
    public static Upgrade getUpgradeByUpgrade(ItemStack stack, Upgrade upgrade){
        List<Upgrade> upgrades = getUpgrades(stack);
        for (Upgrade index : upgrades) {
            if (index.getBaseName().equals(upgrade.getBaseName())){
                return upgrade;
            }
        }
        return null;
    }

    public static List<Upgrade> getUpgrades(ItemStack stack){
        List<Upgrade> upgrades = new ArrayList<>();
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        int start = stack.getItem() instanceof BurnerGunMK1 ? 1 : 0;
        for (int index  = start; index < handler.getSlots()-start; index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgrades.add(((UpgradeCard)handler.getStackInSlot(index).getItem()).getUpgrade());
            }
        }
        return upgrades;
    }



    //Returns upgrade stacks
    public static List<ItemStack> getUpgradeStacks(ItemStack stack){
        List<ItemStack> upgradeStacks = new ArrayList<>();
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        int start = stack.getItem() instanceof BurnerGunMK1 ? 1 : 0;
        for (int index = start; index < handler.getSlots()-start; index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgradeStacks.add(handler.getStackInSlot(index));
            }
        }
        return upgradeStacks;
    }

    public static ItemStack getStackByUpgrade(ItemStack stack, Upgrade upgrade){
        List<ItemStack> upgradeStack = getUpgradeStacks(stack);
        List<Upgrade> upgradeCard = getUpgrades(stack);
        for (int index = 0 ; index < upgradeCard.size() ; index++) {
            if (upgradeCard.get(index).getBaseName().equals(upgrade.getBaseName())){
                return upgradeStack.get(index);
            }
        }
        return null;
    }

    public static void updateUpgrade(ItemStack stack, Upgrade upgrade){
        ListNBT upgrades = BurnerGunMK2.getInfo(stack).getUpgradeNBTList();
        upgrades.forEach(e -> {
            CompoundNBT compound = (CompoundNBT)e;
            String name = compound.getString(KEY_UPGRADE);
            boolean isEnabled = compound.getBoolean(KEY_ENABLED);
            if( (name.contains(Upgrade.FORTUNE_1.getBaseName()) && isEnabled && upgrade.lazyIs(Upgrade.SILK_TOUCH) )
                    || (name.equals(Upgrade.SILK_TOUCH.getBaseName()) && isEnabled && upgrade.lazyIs(Upgrade.FORTUNE_1) ))
                compound.putBoolean(KEY_ENABLED, false);

            if( name.equals(upgrade.getName()) )
                compound.putBoolean(KEY_ENABLED, !compound.getBoolean(KEY_ENABLED));
        });
    }

}