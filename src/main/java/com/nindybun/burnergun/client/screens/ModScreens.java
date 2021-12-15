package com.nindybun.burnergun.client.screens;

import net.minecraft.client.Minecraft;

public class ModScreens {
    public static void openGadgetSettingsScreen() {
        Minecraft.getInstance().setScreen(new settingsScreen());
    }
}
