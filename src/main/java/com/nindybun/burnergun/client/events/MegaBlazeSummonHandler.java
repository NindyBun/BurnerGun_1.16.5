package com.nindybun.burnergun.client.events;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.entities.MegaBlazeSummonEntity;
import com.nindybun.burnergun.common.items.MegaBlazeSummon;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MegaBlazeSummonHandler {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){

        if (event.getWorld().isClientSide) return;
        if (!(event.getEntity() instanceof ItemEntity)) return;
        if (event.getEntity() instanceof MegaBlazeSummonEntity) return;
        ItemEntity itemEntity = (ItemEntity) event.getEntity();

        if (isMegaBlazeSummonItem(itemEntity) && !event.getWorld().isClientSide){

            System.out.println("MegaBlazeSummonItem has been dropped");

            MegaBlazeSummonEntity newEntityItem = MegaBlazeSummonEntity.copy(itemEntity);
            event.getWorld().addFreshEntity(newEntityItem);
            event.setCanceled(true);
            newEntityItem.setDefaultPickUpDelay();
        }
    }

    private static boolean isMegaBlazeSummonItem(ItemEntity itemEntity){
        return itemEntity.getItem().getItem() instanceof MegaBlazeSummon;
    }
}
