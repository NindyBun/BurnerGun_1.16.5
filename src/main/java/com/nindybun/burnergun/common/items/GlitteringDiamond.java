package com.nindybun.burnergun.common.items;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketGlitteringDiamond;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GlitteringDiamond extends Item {
    public GlitteringDiamond() {
        super(new Item.Properties().stacksTo(64).tab(BurnerGun.itemGroup));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag p_77624_4_) {
        tooltip.add(new StringTextComponent("Uses: " + stack.getOrCreateTag().getInt("Use")));
        super.appendHoverText(stack, world, tooltip, p_77624_4_);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Use")) {
            nbt.putInt("Use", 0);
        }
        PacketHandler.sendToServer(new PacketGlitteringDiamond(nbt));
        //stack.setTag(nbt);

        return ActionResult.consume(stack);
    }
}
