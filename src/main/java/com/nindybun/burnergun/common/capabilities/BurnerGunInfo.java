package com.nindybun.burnergun.common.capabilities;

public interface BurnerGunInfo {

    void setFuelValue(int value);

    int getFuelValue();

    void setHeatValue(int value);

    int getHeatValue();

    void setCooldown(int value);

    int getCooldown();

    void setHarvestLevel(int value);

    int getHarvestLevel();

    /*void setVolume(float value);
    float getVolume();*/
}