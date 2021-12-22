package com.nindybun.burnergun.common.capabilities.burnergunmk2;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class BurnerGunMK2InfoProvider implements BurnerGunMK2Info, ICapabilitySerializable<INBT> {
    @CapabilityInject(BurnerGunMK2Info.class)
    public static Capability<BurnerGunMK2Info> burnerGunInfoMK2Capability = null;

    private LazyOptional<BurnerGunMK2Info> instance = LazyOptional.of(burnerGunInfoMK2Capability::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == burnerGunInfoMK2Capability ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return burnerGunInfoMK2Capability.getStorage().writeNBT(burnerGunInfoMK2Capability, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        burnerGunInfoMK2Capability.getStorage().readNBT(burnerGunInfoMK2Capability, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);

    }

    public float volume;
    public int vertical, maxVertical;
    public int horizontal, maxHorizontal;
    public int raycast, maxRaycast;
    public boolean trash;
    public boolean smelt;

    @Override
    public void setVolume(float value) {
        volume = Math.max(0.0f, Math.min(1.0f, value));
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
    public void setMaxVertical(int value) {
        maxVertical = value;
    }

    @Override
    public int getMaxVertical() {
        return maxVertical;
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
    public void setMaxHorizontal(int value) {
        maxVertical = value;
    }

    @Override
    public int getMaxHorizontal() {
        return maxHorizontal;
    }

    @Override
    public void setTrashIsWhitelist(boolean value) {
        trash = value;
    }

    @Override
    public boolean getTrashIsWhitelist() {
        return trash;
    }

    @Override
    public void setSmeltIsWhitelist(boolean value) {
        smelt = value;
    }

    @Override
    public boolean getSmeltIsWhitelist() {
        return smelt;
    }

    @Override
    public void setRaycastRange(int value) {
        raycast = value;
    }

    @Override
    public int getRaycastRange() {
        return raycast != 0 ? raycast : 5;
    }

    @Override
    public void setMaxRaycastRange(int value) {
        maxRaycast = value;
    }

    @Override
    public int getMaxRaycastRange() {
        return maxRaycast != 0 ? maxRaycast : 5;
    }


}