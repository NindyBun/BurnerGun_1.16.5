package com.nindybun.burnergun.client;

import com.nindybun.burnergun.client.screens.ModScreens;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk2.BurnerGunMK2;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketOpenBurnerGunGui;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class KeyInputHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        if (Keybinds.burnergun_light_key.isDown() && Minecraft.getInstance().screen == null)
            LOGGER.info("PLACE LIGHT");
        if (Keybinds.burnergun_gui_key.isDown() && Minecraft.getInstance().screen == null
                && (player.getMainHandItem().getItem() instanceof BurnerGunMK1 || player.getOffhandItem().getItem() instanceof BurnerGunMK1
                || player.getMainHandItem().getItem() instanceof BurnerGunMK2 || player.getOffhandItem().getItem() instanceof BurnerGunMK2)){
            PacketHandler.sendToServer(new PacketOpenBurnerGunGui());
        }
        if (Keybinds.burnergun_screen_key.isDown() && Minecraft.getInstance().screen == null
                && (player.getMainHandItem().getItem() instanceof BurnerGunMK1 || player.getOffhandItem().getItem() instanceof BurnerGunMK1
                || player.getMainHandItem().getItem() instanceof BurnerGunMK2 || player.getOffhandItem().getItem() instanceof BurnerGunMK2)){
            ItemStack stack = BurnerGunMK1.getGun(player);
            if (stack == ItemStack.EMPTY){
                stack = BurnerGunMK2.getGun(player);
            }
            else{
                ModScreens.openGunMk1SettingsScreen(stack);
                return;
            }
            if (stack == ItemStack.EMPTY){
                return;
            }else{
                ModScreens.openGunMk2SettingsScreen(stack);
                return;
            }
        }
    }
}
