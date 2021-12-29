package com.nindybun.burnergun.common.capabilities.burnergunmk1;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
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

    public float volume = 1.0f;
    public int fuel;
    public int vertical;
    public int horizontal;
    public int raycast = 5;
    public ListNBT upgrades = new ListNBT();
    public ListNBT trashFilter = new ListNBT();
    public ListNBT smeltFilter = new ListNBT();

    @Override
    public void setFuelValue(int value) {
        fuel = value;
    }
    @Override
    public int getFuelValue() {
        return fuel;
    }

    @Override
    public void setVolume(float value) {
        volume = value;
    }
    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public void setVertical(int value) {
        vertical = value;
    }
    @Override
    public int getVertical() {
        return vertical;
    }

    @Override
    public void setHorizontal(int value) {
        horizontal = value;
    }
    @Override
    public int getHorizontal() {
        return horizontal;
    }

    @Override
    public void setRaycastRange(int value) {
        raycast = value;
    }
    @Override
    public int getRaycastRange() {
        return raycast;
    }

    @Override
    public void setUpgradeNBTList(ListNBT upgrades) {
        this.upgrades = upgrades;
    }
    @Override
    public ListNBT getUpgradeNBTList() {
        return upgrades;
    }

    @Override
    public void setTrashNBTFilter(ListNBT items) {
        trashFilter = items;
    }
    @Override
    public ListNBT getTrashNBTFilter() {
        return trashFilter;
    }

    @Override
    public void setSmeltNBTFilter(ListNBT items) {
        smeltFilter = items;
    }
    @Override
    public ListNBT getSmeltNBTFilter() {
        return smeltFilter;
    }
}
