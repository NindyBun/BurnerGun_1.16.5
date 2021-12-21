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

    }

    @Override
    public int getVertical() {
        return 0;
    }


}
