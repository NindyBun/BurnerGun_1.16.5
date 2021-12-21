package com.nindybun.burnergun.common.capabilities.burnergunmk2;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BurnerGunMK2InfoStorage implements Capability.IStorage<BurnerGunMK2Info> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<BurnerGunMK2Info> capability, BurnerGunMK2Info instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
            tag.putFloat("Volume", instance.getVolume());

        return tag;
    }

    @Override
    public void readNBT(Capability<BurnerGunMK2Info> capability, BurnerGunMK2Info instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setVolume(tag.getFloat("Volume"));
    }
}
