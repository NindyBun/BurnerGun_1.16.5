package com.nindybun.burnergun.common.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BurnerGunInfoStorage implements Capability.IStorage<BurnerGunInfo> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<BurnerGunInfo> capability, BurnerGunInfo instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
            tag.putInt("FuelValue", instance.getFuelValue());
            tag.putInt("HeatValue", instance.getHeatValue());
            tag.putInt("HarvestLevel", instance.getHarvestLevel());
            tag.putInt("CoolDown", instance.getCooldown());

        return tag;
    }

    @Override
    public void readNBT(Capability<BurnerGunInfo> capability, BurnerGunInfo instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setFuelValue(tag.getInt("FuelValue"));
        instance.setHeatValue(tag.getInt("HeatValue"));
        instance.setHarvestLevel(tag.getInt("HarvestLevel"));
        instance.setCooldown(tag.getInt("CoolDown"));
    }
}
