package com.nindybun.burnergun.client.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ModScreens {
    public static void openGunMk1SettingsScreen(ItemStack gun) {
        Minecraft.getInstance().setScreen(new mk1SettingsScreen(gun));
    }
    public static void openGunMk2SettingsScreen(ItemStack gun) {
        Minecraft.getInstance().setScreen(new mk2SettingsScreen(gun));
    }
}
