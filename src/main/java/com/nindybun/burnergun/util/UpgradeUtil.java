package com.nindybun.burnergun.util;

import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UpgradeUtil {
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

    public static boolean containsUpgradeFromList(List<Upgrade> upgradeList, Upgrade upgrade){
        for (Upgrade index : upgradeList) {
            if (index.equals(upgrade))
                return true;
        }
        return false;
    }

    public static List<Upgrade> getToggleableUpgrades(ItemStack stack){
        return getUpgrades(stack).stream().filter(Upgrade::isToggleable).collect(Collectors.toList());
    }

    public static List<Upgrade> getActiveUpgrades(ItemStack stack){
        return getUpgrades(stack).stream().filter(Upgrade::isActive).collect(Collectors.toList());
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
        List<Upgrade> upgrades = getToggleableUpgrades(stack);
        upgrades.forEach(e -> {
            String name = e.getName();
            boolean isEnabled = e.isActive();
            if( (name.contains(Upgrade.FORTUNE_1.getBaseName()) && isEnabled && upgrade.lazyIs(Upgrade.SILK_TOUCH) )
                    || (name.equals(Upgrade.SILK_TOUCH.getBaseName()) && isEnabled && upgrade.lazyIs(Upgrade.FORTUNE_1) ))
                e.setActive(false);

            if( name.equals(upgrade.getName()) )
                e.setActive(!e.isActive());
        });
    }

    @Nullable
    public static Upgrade getUpgradeByName(String name) {
        // If the name doesn't exist then move on
        try {
            Upgrade type = Upgrade.valueOf(name.toUpperCase());
            return type;
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
