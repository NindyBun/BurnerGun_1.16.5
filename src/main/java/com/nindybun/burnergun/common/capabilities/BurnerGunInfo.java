package com.nindybun.burnergun.common.capabilities;

public interface BurnerGunInfo {

    public void setFuelValue(int value);
    public int getFuelValue();

    public void setHeatValue(int value);
    public int getHeatValue();

    public void setCooldown(int value);
    public int getCooldown();

    public void setHarvestLevel(int value);
    public int getHarvestLevel();
}
