package com.nindybun.burnergun.common.capabilities.burnergunmk1;

import net.minecraft.nbt.ListNBT;

public interface BurnerGunMK1Info {
    void setFuelValue(int value);
    int getFuelValue();

    void setVolume(float value);
    float getVolume();

    void setVertical(int value);
    int getVertical();

    void setHorizontal(int value);
    int getHorizontal();

    void setRaycastRange(int value);
    int getRaycastRange();

    void setUpgradeNBTList(ListNBT upgrades);
    ListNBT getUpgradeNBTList();

    void setTrashNBTFilter(ListNBT items);
    ListNBT getTrashNBTFilter();

    void setSmeltNBTFilter(ListNBT items);
    ListNBT getSmeltNBTFilter();
}