package com.nindybun.burnergun.common.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    //Set or Get Raycast range of Focal Point
    public static int setRaycastRange(ItemStack gun, int range){
        gun.getOrCreateTag().putInt(RAYCAST_RANGE, range);
        return range;
    }
    public static int getRaycastRange(ItemStack gun){
        CompoundNBT nbt = gun.getOrCreateTag();
        return !nbt.contains(RAYCAST_RANGE) ? setRaycastRange(gun, MIN_RAYCAST_RANGE) : nbt.getInt(RAYCAST_RANGE);
    }
    //Set or Get Max Raycast range of Focal Point
    public static int setMaxRaycastRange(ItemStack gun, int range){
        gun.getOrCreateTag().putInt(MAX_RAYCAST_RANGE, range);
        return range;
    }
    public static int getMaxRaycastRange(ItemStack gun){
        CompoundNBT nbt = gun.getOrCreateTag();
        return !nbt.contains(MAX_RAYCAST_RANGE) ? setMaxRaycastRange(gun, MIN_RAYCAST_RANGE) : gun.getOrCreateTag().getInt(MAX_RAYCAST_RANGE);
    }
    //Set or Get Whitelist toggle for Trash
    public static boolean setWhitelist(ItemStack gun, boolean isWhitelist){

    }

}
