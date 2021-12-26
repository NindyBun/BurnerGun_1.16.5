package com.nindybun.burnergun.common.items.upgrades.Auto_Smelt;

import com.nindybun.burnergun.common.containers.AutoSmeltContainer;
import com.nindybun.burnergun.common.containers.TrashContainer;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import com.nindybun.burnergun.common.network.PacketHandler;
import com.nindybun.burnergun.common.network.packets.PacketOpenAutoSmeltGui;
import com.nindybun.burnergun.common.network.packets.PacketOpenTrashGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AutoSmelt extends UpgradeCard {
    Upgrade upgrade;
    public static final Logger LOGGER = LogManager.getLogger();

    public AutoSmelt(Upgrade upgrade) {
        super(upgrade);
        this.upgrade = upgrade;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            PacketHandler.sendToServer(new PacketOpenAutoSmeltGui());
        }
        return ActionResult.success(stack);
    }

    public Upgrade getUpgrade(){
        return upgrade;
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
        if (this.getClass() == AutoSmelt.class){
            return new AutoSmeltProvider();
        }else{
            return super.initCapabilities(stack, oldCapNbt);
        }

    }

    public static AutoSmeltHandler getHandler(ItemStack itemStack) {
        IItemHandler handler = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (handler == null || !(handler instanceof AutoSmeltHandler)) {
            LOGGER.error("AutoSmelt did not have the expected ITEM_HANDLER_CAPABILITY");
            return new AutoSmeltHandler(AutoSmeltContainer.MAX_EXPECTED_HANDLER_SLOT_COUNT);
        }
        return (AutoSmeltHandler) handler;
    }

    private final String BASE_NBT_TAG = "base";
    private final String CAPABILITY_NBT_TAG = "cap";


    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();
        AutoSmeltHandler handler = getHandler(stack);
        CompoundNBT capabilityTag = handler.serializeNBT();
        CompoundNBT combinedTag = new CompoundNBT();
        if (baseTag != null) {
            combinedTag.put(BASE_NBT_TAG, baseTag);
        }
        if (capabilityTag != null) {
            combinedTag.put(CAPABILITY_NBT_TAG, capabilityTag);
        }
        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound(BASE_NBT_TAG);
        CompoundNBT capabilityTag = nbt.getCompound(CAPABILITY_NBT_TAG);
        stack.setTag(baseTag);
        AutoSmeltHandler handler = getHandler(stack);
        handler.deserializeNBT(capabilityTag);
    }
}
