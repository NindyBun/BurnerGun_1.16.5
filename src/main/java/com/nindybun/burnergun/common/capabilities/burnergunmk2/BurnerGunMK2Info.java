package com.nindybun.burnergun.common.capabilities.burnergunmk2;

public interface BurnerGunMK2Info {
    void setVolume(float value);
    float getVolume();

    void setVertical(int value);
    int getVertical();

    void setMaxVertical(int value);
    int getMaxVertical();

    void setHorizontal(int value);
    int getHorizontal();

    void setMaxHorizontal(int value);
    int getMaxHorizontal();

    void setTrashIsWhitelist(boolean value);
    boolean getTrashIsWhitelist();

    void setSmeltIsWhitelist(boolean value);
    boolean getSmeltIsWhitelist();

    void setRaycastRange(int value);
    int getRaycastRange();

    void setMaxRaycastRange(int value);
    int getMaxRaycastRange();
}