package com.nindybun.burnergun.common.items;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.capabilities.GDProvider;
import com.nindybun.burnergun.common.capabilities.IGD;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketGDcount;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class GlitteringDiamond extends Item {
    public GlitteringDiamond() {
        super(
                new Properties().stacksTo(1).tab(BurnerGun.itemGroup)
        );
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IGD igd = stack.getCapability(GDProvider.IGD_CAPABILITY, null).orElseThrow(null);
        if (!world.isClientSide){
            PacketGDcount packet = new PacketGDcount(1);
            PacketHandler.sendToServer(packet);
            player.displayClientMessage(new StringTextComponent("Count >> " + igd.getCount()), false);
        }
        return ActionResult.consume(stack);
    }
}
