package com.nindybun.burnergun.common.capabilities.burnergunmk1;

public interface BurnerGunMK1Info {

    void setFuelValue(int value);

    int getFuelValue();

    void setHeatValue(int value);

    int getHeatValue();

    void setCooldown(int value);

    int getCooldown();

    void setHarvestLevel(int value);

    int getHarvestLevel();

    void setVolume(float value);
    float getVolume();
}