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
            tag.putInt("Vertical", instance.getVertical());
            tag.putInt("MaxVertical", instance.getMaxVertical());
            tag.putInt("Horizontal", instance.getHorizontal());
            tag.putInt("MaxHorizontal", instance.getMaxHorizontal());
            tag.putInt("Raycast", instance.getRaycastRange());
            tag.putInt("MaxRaycast", instance.getMaxRaycastRange());
            tag.putBoolean("Trash", instance.getTrashIsWhitelist());
            tag.putBoolean("Smelt", instance.getSmeltIsWhitelist());
        return tag;
    }

    @Override
    public void readNBT(Capability<BurnerGunMK2Info> capability, BurnerGunMK2Info instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setVolume(tag.getFloat("Volume"));
        instance.setVertical(tag.getInt("Vertical"));
        instance.setMaxVertical(tag.getInt("MaxVertical"));
        instance.setHorizontal(tag.getInt("Horizontal"));
        instance.setMaxHorizontal(tag.getInt("MaxHorizontal"));
        instance.setRaycastRange(tag.getInt("Raycast"));
        instance.setMaxRaycastRange(tag.getInt("MaxRaycast"));
        instance.setTrashIsWhitelist(tag.getBoolean("Trash"));
        instance.setSmeltIsWhitelist(tag.getBoolean("Smelt"));
    }
}
