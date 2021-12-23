package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.capabilities.burnergunmk1.BurnerGunMK1InfoProvider;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2InfoProvider;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1;
import com.nindybun.burnergun.common.items.burnergunmk1.BurnerGunMK1Provider;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.text.StringContent;

public class BurnerGunMK2 extends Item {
    private static final Logger LOGGER = LogManager.getLogger();


    public BurnerGunMK2() {
        super(new Item.Properties().stacksTo(1).setNoRepair().fireResistant().tab(BurnerGun.itemGroup));
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
        return new BurnerGunMK2Provider();
    }

    public static IItemHandler getHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    }

    private final String INFO_NBT_TAG = "burnergunMK2InfoNBT";

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT infoTag = new CompoundNBT();
        stack.getCapability(BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability, null).ifPresent((cap) -> {
            infoTag.put(INFO_NBT_TAG, BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability.writeNBT(cap, null));
        });
        return infoTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt != null)
            stack.getCapability(BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability, null).ifPresent((cap) -> {
                BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability.readNBT(cap, null, nbt.get(INFO_NBT_TAG));
            });
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
        IItemHandler handler = getHandler(gun);
        if (!world.isClientSide){
            BlockRayTraceResult blockRayTraceResult = WorldUtil.getLookingAt(world, player, RayTraceContext.FluidMode.NONE, 5);
            BlockPos blockPos = blockRayTraceResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);

        }
        return ActionResult.consume(gun);
    }

    public static ItemStack getGun(PlayerEntity player) {
        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof BurnerGunMK2)) {
            heldItem = player.getOffhandItem();
            if (!(heldItem.getItem() instanceof BurnerGunMK2)) {
                return ItemStack.EMPTY;
            }
        }
        return heldItem;
    }

    public static BurnerGunMK2Info getInfo(ItemStack gun) {
        return gun.getCapability(BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability, null).orElse(null);
    }
}
