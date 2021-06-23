package com.nindybun.burnergun.common.capabilities;

import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BurnerGunInfoProvider implements BurnerGunInfo, ICapabilitySerializable<INBT> {
    @CapabilityInject(BurnerGunInfo.class)
    public static Capability<BurnerGunInfo> burnerGunInfoCapability = null;

    private LazyOptional<BurnerGunInfo> instance = LazyOptional.of(burnerGunInfoCapability::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == burnerGunInfoCapability ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return burnerGunInfoCapability.getStorage().writeNBT(burnerGunInfoCapability, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        burnerGunInfoCapability.getStorage().readNBT(burnerGunInfoCapability, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);

    }

    public int fuelValue;
    public int heatValue;
    public int cooldown;
    public int harvestLevel;


    @Override
    public void setFuelValue(int value) {
        fuelValue = value;
    }

    @Override
    public int getFuelValue() {
        return fuelValue;
    }

    @Override
    public void setHeatValue(int value) {
        heatValue = value;
    }

    @Override
    public int getHeatValue() {
        return heatValue;
    }

    @Override
    public void setCooldown(int value) {
        cooldown = value;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public void setHarvestLevel(int value) {
        harvestLevel = value;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }


}
