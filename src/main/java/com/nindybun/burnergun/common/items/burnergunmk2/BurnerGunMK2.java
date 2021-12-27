package com.nindybun.burnergun.common.items.burnergunmk2;

import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.blocks.Light;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2Info;
import com.nindybun.burnergun.common.capabilities.burnergunmk2.BurnerGunMK2InfoProvider;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.util.UpgradeUtil;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BurnerGunMK2 extends Item {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final IRecipeType<? extends AbstractCookingRecipe> RECIPE_TYPE = IRecipeType.SMELTING;


    public BurnerGunMK2() {
        super(new Item.Properties().stacksTo(1).setNoRepair().fireResistant().tab(BurnerGun.itemGroup));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("Was is Worth it? What did it cost?").withStyle(TextFormatting.GOLD));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
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
    private final String HANDLER_NBT_TAG = "burnergunMK2HandlerNBT";

    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT infoTag = new CompoundNBT();
        stack.getCapability(BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability, null).ifPresent((cap) -> {
            infoTag.put(INFO_NBT_TAG, BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability.writeNBT(cap, null));
        });
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent((cap)->{
            infoTag.put(HANDLER_NBT_TAG, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(cap, null));
        });
        return infoTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt != null){
            stack.getCapability(BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability, null).ifPresent((cap) -> {
                BurnerGunMK2InfoProvider.burnerGunInfoMK2Capability.readNBT(cap, null, nbt.get(INFO_NBT_TAG));
            });
            stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent((cap)->{
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(cap, null, nbt.get(HANDLER_NBT_TAG));
            });
        }
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
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.NONE;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public boolean canMine(World world, BlockPos pos, BlockState state, PlayerEntity player){
        if (state.getHarvestLevel() == -1 || state.getBlock() instanceof Light
                || !world.mayInteract(player, pos) || !player.mayBuild()
                || MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player)))
            return false;
        return true;
    }

    public List<ItemStack> trashItem(List<ItemStack> list, ItemStack item){

        return list;
    }

    public void mineBlock(World world, ItemStack gun, BurnerGunMK2Info info, List<Upgrade> upgrades, List<ItemStack> smeltFilter, List<ItemStack> trashFilter, BlockPos blockPos, BlockState blockState, PlayerEntity player){
        if (canMine(world, blockPos, blockState, player)){
            List<ItemStack> blockDrops = blockState.getDrops(new LootContext.Builder((ServerWorld) world)
                .withParameter(LootParameters.TOOL, gun)
                .withParameter(LootParameters.ORIGIN, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .withParameter(LootParameters.BLOCK_STATE, blockState)
            );
            world.destroyBlock(blockPos, false);
            if (!blockDrops.isEmpty()){
                blockDrops.forEach(drops -> {
                    if (UpgradeUtil.containsUpgradeFromList(upgrades, Upgrade.AUTO_SMELT)) {
                        IInventory inv = new Inventory(1);
                        inv.setItem(0, drops.copy());
                        Optional<? extends AbstractCookingRecipe> recipe = world.getRecipeManager().getRecipeFor(RECIPE_TYPE, inv, world);
                        drops = recipe.isPresent() ? recipe.get().getResultItem().copy() : drops.copy();
                    }
                });
            }
        }
    }

    public void mineArea(){

    }


    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack gun = player.getItemInHand(hand);
        if (!world.isClientSide){
            BurnerGunMK2Info info = getInfo(gun);
            List<Upgrade> upgrades = UpgradeUtil.getUpgradesFromNBT(info.getUpgradeNBTList());
            BlockRayTraceResult blockRayTraceResult = WorldUtil.getLookingAt(world, player, RayTraceContext.FluidMode.NONE, info.getRaycastRange());
            BlockPos blockPos = blockRayTraceResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            List<ItemStack> smeltFilter = UpgradeUtil.getItemStacksFromList(info.getSmeltNBTFilter());
            List<ItemStack> trashFilter = UpgradeUtil.getItemStacksFromList(info.getTrashNBTFilter());
            if (canMine(world, blockPos, blockState, player)){
                if (player.isCrouching())
                    mineBlock(world, gun, info, upgrades, smeltFilter, trashFilter, blockPos, blockState, player);
                else
                    mineArea();
            }

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
