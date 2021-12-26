package com.nindybun.burnergun.common.capabilities.burnergunmk1;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class BurnerGunMK1InfoProvider implements BurnerGunMK1Info, ICapabilitySerializable<INBT> {
    @CapabilityInject(BurnerGunMK1Info.class)
    public static Capability<BurnerGunMK1Info> burnerGunInfoMK1Capability = null;

    private LazyOptional<BurnerGunMK1Info> instance = LazyOptional.of(burnerGunInfoMK1Capability::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == burnerGunInfoMK1Capability ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return burnerGunInfoMK1Capability.getStorage().writeNBT(burnerGunInfoMK1Capability, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        burnerGunInfoMK1Capability.getStorage().readNBT(burnerGunInfoMK1Capability, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);

    }

    public int fuelValue;
    public int heatValue;
    public int cooldown;
    public int harvestLevel;
    public float volume = 1.0f;


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

    @Override
    public void setVolume(float value) {
        volume = value;
    }

    @Override
    public float getVolume() {
        return volume;
    }


}
