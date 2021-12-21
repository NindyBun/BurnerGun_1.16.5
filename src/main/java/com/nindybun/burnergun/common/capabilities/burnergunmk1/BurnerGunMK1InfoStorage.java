package com.nindybun.burnergun.common.capabilities.burnergunmk1;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BurnerGunMK1InfoStorage implements Capability.IStorage<BurnerGunMK1Info> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<BurnerGunMK1Info> capability, BurnerGunMK1Info instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
            tag.putInt("FuelValue", instance.getFuelValue());
            tag.putInt("HeatValue", instance.getHeatValue());
            tag.putInt("HarvestLevel", instance.getHarvestLevel());
            tag.putInt("CoolDown", instance.getCooldown());
            tag.putFloat("Volume", instance.getVolume());

        return tag;
    }

    @Override
    public void readNBT(Capability<BurnerGunMK1Info> capability, BurnerGunMK1Info instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setFuelValue(tag.getInt("FuelValue"));
        instance.setHeatValue(tag.getInt("HeatValue"));
        instance.setHarvestLevel(tag.getInt("HarvestLevel"));
        instance.setCooldown(tag.getInt("CoolDown"));
        instance.setVolume(tag.getFloat("Volume"));
    }
}
