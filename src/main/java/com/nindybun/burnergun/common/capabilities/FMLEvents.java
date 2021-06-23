package com.nindybun.burnergun.common.capabilities;

import com.nindybun.burnergun.common.items.Burner_Gun.BurnerGun;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FMLEvents {

    @SubscribeEvent
    public static void onAttatchedCapabilities(AttachCapabilitiesEvent<ItemStack> event){
        if (event.getObject().getItem() instanceof BurnerGun){
            event.addCapability(new ResourceLocation(com.nindybun.burnergun.common.BurnerGun.MOD_ID, "burner_gun"), new BurnerGunInfoProvider());
        }
    }

}
