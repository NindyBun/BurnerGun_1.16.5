package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.swing.text.StringContent;

public class BurnerGunMK2 extends Item {
    private static final Logger LOGGER = LogManager.getLogger();


    public BurnerGunMK2() {
        super(new Item.Properties().stacksTo(1).setNoRepair().fireResistant().tab(BurnerGun.itemGroup));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new BurnerGunMK2Provider();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack gun = player.getItemInHand(hand);
        if (!world.isClientSide){
            BlockRayTraceResult blockRayTraceResult = WorldUtil.getLookingAt(world, player, RayTraceContext.FluidMode.NONE, 5);
            BlockPos blockPos = blockRayTraceResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

        }
        return ActionResult.consume(gun);
    }
}
