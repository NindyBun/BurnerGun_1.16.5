package com.nindybun.burnergun.common.items;

import com.nindybun.burnergun.common.BurnerGun;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class GlitteringDiamond extends Item {
    public GlitteringDiamond() {
        super(
                new Properties().stacksTo(1).tab(BurnerGun.itemGroup)
        );
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide){
            player.displayClientMessage(new StringTextComponent("Hey"), false);
        }
        return ActionResult.consume(stack);
    }
}
