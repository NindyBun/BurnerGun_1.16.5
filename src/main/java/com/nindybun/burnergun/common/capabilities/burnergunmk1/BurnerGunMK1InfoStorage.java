package com.nindybun.burnergun.common.capabilities.burnergunmk1;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class BurnerGunMK1InfoStorage implements Capability.IStorage<BurnerGunMK1Info> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<BurnerGunMK1Info> capability, BurnerGunMK1Info instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("FuelValue", instance.getFuelValue());
        tag.putFloat("Volume", instance.getVolume());
        tag.putInt("Vertical", instance.getVertical());
        tag.putInt("Horizontal", instance.getHorizontal());
        tag.putInt("Raycast", instance.getRaycastRange());
        tag.put("Upgrades", instance.getUpgradeNBTList());
        tag.put("TrashFilter", instance.getTrashNBTFilter());
        tag.put("SmeltFilter", instance.getSmeltNBTFilter());

        return tag;
    }

    @Override
    public void readNBT(Capability<BurnerGunMK1Info> capability, BurnerGunMK1Info instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setFuelValue(tag.getInt("FuelValue"));
        instance.setVolume(tag.getFloat("Volume"));
        instance.setVertical(tag.getInt("Vertical"));
        instance.setHorizontal(tag.getInt("Horizontal"));
        instance.setRaycastRange(tag.getInt("Raycast"));
        instance.setUpgradeNBTList(tag.getList("Upgrades", Constants.NBT.TAG_COMPOUND));
        instance.setTrashNBTFilter(tag.getList("TrashFilter", Constants.NBT.TAG_COMPOUND));
        instance.setSmeltNBTFilter(tag.getList("SmeltFilter", Constants.NBT.TAG_COMPOUND));
    }
}
