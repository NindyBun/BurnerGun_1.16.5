package com.nindybun.burnergun.common.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GDProvider implements IGD, ICapabilitySerializable<INBT> {
    @CapabilityInject(IGD.class)
    public static final Capability<IGD> IGD_CAPABILITY = null;

    private LazyOptional<IGD> IGDLAZY = LazyOptional.of(IGD_CAPABILITY::getDefaultInstance);
    public int count;


    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == IGD_CAPABILITY ? IGDLAZY.cast() : null;
    }

    @Override
    public INBT serializeNBT() {
        return IGD_CAPABILITY.getStorage().writeNBT(IGD_CAPABILITY, this.IGDLAZY.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")),null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        IGD_CAPABILITY.getStorage().readNBT(IGD_CAPABILITY, this.IGDLAZY.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
