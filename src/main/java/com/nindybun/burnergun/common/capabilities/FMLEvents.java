package com.nindybun.burnergun.common.capabilities;

import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber
public class FMLEvents {
    @CapabilityInject(BurnerGunInfo.class)
    public static Capability<BurnerGunInfo> burnerGunInfoCapability = null;

    public static void onAttatchedCapabilities(AttachCapabilitiesEvent<ItemStack> event){
        if (event.getObject().getItem() instanceof BurnerGun){
            event.addCapability(new ResourceLocation(com.nindybun.burnergun.common.BurnerGun.MOD_ID, "burnerguninfo"), new BurnerGunInfoProvider());
        }
    }

}
