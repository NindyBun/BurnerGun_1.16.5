package com.nindybun.burnergun.common.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class gunProperties {
    private gunProperties() {}

    private static final String V_EXPAND = "vertical_expansion";
    private static final String H_EXPAND = "horizontal_expansion";
    private static final String RAYCAST_RANGE = "focus_point";
    private static final String MAX_RAYCAST_RANGE = "max_focus_point";
    private static final String VOLUME = "volume";
    private static final String FILTER = "isWhitelist";
    private static final int MIN_RAYCAST_RANGE = 5;

    //Set or Get Vertical Expansion Radius
    public static int setVRange(ItemStack gun, int range){
        gun.getOrCreateTag().putInt(V_EXPAND, range);
        return range;
    }
    public static int getVRange(ItemStack gun){
        CompoundNBT nbt = gun.getOrCreateTag();
        return !nbt.contains(V_EXPAND) ? setVRange(gun, 0) : nbt.getInt(V_EXPAND);
    }
    //Set or Get Horizontal Expansion Radius
    public static int setHRange(ItemStack gun, int range){
        gun.getOrCreateTag().putInt(H_EXPAND, range);
        return range;
    }
    public static int getHRange(ItemStack gun){
        CompoundNBT nbt = gun.getOrCreateTag();
        return !nbt.contains(H_EXPAND) ? setHRange(gun, 0) : nbt.getInt(H_EXPAND);
    }
    //Set or Get Raycase range of Focal Point
    public static int setRaycastRange(ItemStack gun, int range){

        return range;
    }

}
