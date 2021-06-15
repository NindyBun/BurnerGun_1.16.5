package com.nindybun.burnergun.client;

import com.nindybun.burnergun.common.containers.ModContainers;
import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGunScreen;
import com.nindybun.burnergun.common.items.upgrades.Auto_Fuel.AutoFuelScreen;
import com.nindybun.burnergun.common.items.upgrades.Trash.TrashScreen;
import com.nindybun.burnergun.common.items.upgrades.Upgrade_Bag.UpgradeBagScreen;
import net.minecraft.client.gui.ScreenManager;

public class ClientSetup {
    public static void setup(){
        ScreenManager.register(ModContainers.BURNERGUN_CONTAINER.get(), BurnerGunScreen::new);
        ScreenManager.register(ModContainers.AUTOFUEL_CONTAINER.get(), AutoFuelScreen::new);
        ScreenManager.register(ModContainers.TRASH_CONTAINER.get(), TrashScreen::new);
        ScreenManager.register(ModContainers.UPGRADE_BAG_CONTAINER.get(), UpgradeBagScreen::new);
    }
}
