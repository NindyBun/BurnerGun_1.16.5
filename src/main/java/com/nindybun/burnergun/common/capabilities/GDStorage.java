package com.nindybun.burnergun.common.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class GDStorage implements Capability.IStorage<IGD>{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IGD> capability, IGD instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("Count", instance.getCount());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IGD> capability, IGD instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setCount(tag.getInt("Count"));
    }
}
