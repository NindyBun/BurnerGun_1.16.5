package com.nindybun.burnergun.common.items.Burner_Gun;

import com.google.common.collect.Sets;
import com.nindybun.burnergun.client.Keybinds;
import com.nindybun.burnergun.common.blocks.Light;
import com.nindybun.burnergun.common.blocks.ModBlocks;
import com.nindybun.burnergun.common.containers.BurnerGunContainer;
import com.nindybun.burnergun.common.items.upgrades.Auto_Fuel.AutoFuel;
import com.nindybun.burnergun.common.items.upgrades.Trash.Trash;
import com.nindybun.burnergun.common.items.upgrades.Upgrade;
import com.nindybun.burnergun.common.items.upgrades.UpgradeCard;
import com.nindybun.burnergun.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class BurnerGun extends ToolItem{
    private static final Set<Block> EFFECTIVE = Sets.newHashSet();
    private static final int base_use = 100;
    public static final int base_use_buffer = 100_000;
    public static final int base_heat_buffer = 100_000;

    IRecipeType<? extends AbstractCookingRecipe> recipeType = IRecipeType.SMELTING;
    private static final List<Item> smeltingFilter = new ArrayList<Item>(){
        {
            add(Items.CHARCOAL);
            add(Items.IRON_INGOT);
            add(Items.GOLD_INGOT);
        }
    };

    public BurnerGun() {
        super(0, 0, ModTier.TIER, EFFECTIVE,
                new Properties().stacksTo(1).setNoRepair().tab(com.nindybun.burnergun.common.BurnerGun.itemGroup)
                );
    }

    private enum ModTier implements IItemTier {
        TIER(0, 0, 0, 4, 0, null);

        private final int level;
        private final int uses;
        private final float speed;
        private final float attackDamageBonus;
        private final int enchantmentValue;
        private final LazyValue<Ingredient> repairIngredient;

        ModTier(int level, int uses, float speed, float attackDamageBonus, int enchantmentValue, Supplier<Ingredient> repairIngredient)
        {
            this.uses = uses;
            this.speed = speed;
            this.attackDamageBonus = attackDamageBonus;
            this.enchantmentValue = enchantmentValue;
            this.repairIngredient = new LazyValue<>(repairIngredient);
            this.level = level;
        }

        @Override
        public int getUses() {
            return uses;
        }

        @Override
        public float getSpeed() {
            return speed;
        }

        @Override
        public float getAttackDamageBonus() {
            return attackDamageBonus;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public int getEnchantmentValue() {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return null;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        IItemHandler handler = getHandler(stack);
        if (stack.getTag().contains("FuelValue") && !handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem()))
        {
            tooltip.add(new StringTextComponent("Fuel Level: " + getfuelValue(stack) + " / " + base_use_buffer).withStyle(TextFormatting.YELLOW));
        }else if (handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            tooltip.add(new StringTextComponent("Using the heat of the universe!").withStyle(TextFormatting.YELLOW));
        }
        tooltip.add(new StringTextComponent("Press " + Keybinds.burnergun_gui_key.getKey().toString().toUpperCase() + " to open GUI").withStyle(TextFormatting.GRAY));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public void setNBT(ItemStack stack){
        CompoundNBT nbt;
        if (stack.hasTag())
        {
            nbt = stack.getTag();
        }
        else
        {
            nbt = new CompoundNBT();
        }

        if (!nbt.contains("FuelValue")){
            nbt.putInt("FuelValue", 0);
        }
        if (!nbt.contains("HeatValue")){
            nbt.putInt("HeatValue", 0);
        }
        if (!nbt.contains("HarvestLevel")){
            nbt.putInt("HarvestLevel", 2);
        }
        if (!nbt.contains("CoolDown")){
            nbt.putInt("CoolDown", 0);
        }
        stack.setTag(nbt);
    }
////////////////////////////////////////////////////////////////////////
    public void refuel(ItemStack stack, PlayerEntity player){
        IItemHandler item = getHandler(stack);

        if (item.getStackInSlot(0).getCount() < 1  && getUpgradeByUpgrade(stack, Upgrade.AUTO_FUEL) != null){
            IItemHandler autoHandler = AutoFuel.getHandler(getStackByUpgrade(stack, Upgrade.AUTO_FUEL));

            for (int index = 0 ; index < autoHandler.getSlots() ; index++) {
                if (player.inventory.contains(autoHandler.getStackInSlot(index))){
                    ItemStack fuelStack = autoHandler.getStackInSlot(index);
                    int slot = player.inventory.findSlotMatchingItem(fuelStack);
                    ItemStack invStack = player.inventory.getItem(slot).getItem().getDefaultInstance();
                    int fuelValue = net.minecraftforge.common.ForgeHooks.getBurnTime(invStack);

                    while (player.inventory.contains(invStack) &&
                            !(getfuelValue(stack) + fuelValue > base_use_buffer)){

                        stack.getTag().putInt("FuelValue", getfuelValue(stack) + fuelValue);
                        ItemStack containerItem = invStack.getContainerItem();
                        player.inventory.removeItem(player.inventory.findSlotMatchingItem(fuelStack), 1);
                        if (player.inventory.add(containerItem))
                            player.drop(containerItem, true);
                    }

                }
            }
        }

        while (item.getStackInSlot(0).getCount() > 0){
            if (getfuelValue(stack) + net.minecraftforge.common.ForgeHooks.getBurnTime(item.getStackInSlot(0)) > base_use_buffer)
                break;

            stack.getTag().putInt("FuelValue", getfuelValue(stack) + net.minecraftforge.common.ForgeHooks.getBurnTime(item.getStackInSlot(0)));
            ItemStack containerItem = item.getStackInSlot(0).getContainerItem();
            item.getStackInSlot(0).shrink(1);
            if (player.inventory.add(containerItem))
                player.drop(containerItem, true);
        }

    }
    public void setFuelValue(ItemStack stack, int use, PlayerEntity player){
        IItemHandler handler = getHandler(stack);
        if (!handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            refuel(stack, player);
            stack.getTag().putInt("FuelValue", getfuelValue(stack)-(int)getUseValue(stack)*use);
        }else if (use == 1 && handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            stack.getTag().putInt("HeatValue", getheatValue(stack)+(int)getUseValue(stack));
            if (getheatValue(stack) >= base_heat_buffer){
                player.getCooldowns().addCooldown(this, 100);
                stack.getTag().putInt("CoolDown", 0);
            }else{
                stack.getTag().putInt("CoolDown", 20); //about 2 seconds
            }
        }

    }
    public int getfuelValue(ItemStack stack) {
        return stack.getTag().getInt("FuelValue");
    }
    public int getheatValue(ItemStack stack) {
        return stack.getTag().getInt("HeatValue");
    }
/////////////////////////////////////////////////////////////////////////////
    public double getFuelEfficiency(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.FUEL_EFFICIENCY_1) != null ? getUpgradeByUpgrade(stack, Upgrade.FUEL_EFFICIENCY_1).getExtraValue() : 0.00;
    }
    public double getHeatEfficiency(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.HEAT_EFFICIENCY_1) != null ? getUpgradeByUpgrade(stack, Upgrade.HEAT_EFFICIENCY_1).getExtraValue() : 0.00;
    }
    public double getCooldownMultiplier(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.COOLDOWN_MULTIPLIER_1) != null ? getUpgradeByUpgrade(stack, Upgrade.COOLDOWN_MULTIPLIER_1).getExtraValue() : 1.00;
    }
////////////////////////////////////////////////////////////////////////
    public double getUseValue(ItemStack stack){
        IItemHandler handler = getHandler(stack);
        int extraUse = 0;
        List<UpgradeCard> upgrades = getUpgrades(stack);
        if (!upgrades.isEmpty()){
            extraUse = upgrades.stream().mapToInt(upgradeCard -> upgradeCard.getUpgrade().getCost()).sum();
        }
        if (handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())) {
            return (base_use + extraUse) - ((base_use + extraUse) * getHeatEfficiency(stack));
        }
        return (base_use + extraUse) - ((base_use + extraUse) * getFuelEfficiency(stack));
    }
////////////////////////////////////////////////////////////////////////
    public int getFortune(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.FORTUNE_1) != null ? getUpgradeByUpgrade(stack, Upgrade.FORTUNE_1).getTier() : 0;
    }
////////////////////////////////////////////////////////////////////////
    public int getSilkTouch(ItemStack stack){
        return getUpgrades(stack).contains(Upgrade.SILK_TOUCH.getCard()) ? 1 : 0;
    }
////////////////////////////////////////////////////////////////////////
    public Boolean getMagnet(ItemStack stack){
        return getUpgrades(stack).contains(Upgrade.MAGNET.getCard());
    }
////////////////////////////////////////////////////////////////////////
    public double getRange(ItemStack stack){
        return getUpgradeByUpgrade(stack, Upgrade.FOCAL_POINT_1) != null ? getUpgradeByUpgrade(stack, Upgrade.FOCAL_POINT_1).getExtraValue() : 5;
    }
////////////////////////////////////////////////////////////////////////
    public int getHarvestLevel(ItemStack stack){
        return stack.getTag().getInt("HarvestLevel");
    }
    public int getCoolDown(ItemStack stack) { return stack.getTag().getInt("CoolDown"); }
    ////////////////////////////////////////////////////////////////////////
    //Returns Upgrade cards
    public List<UpgradeCard> getUpgrades(ItemStack stack){
        List<UpgradeCard> upgrades = new ArrayList<>();
        IItemHandler handler = getHandler(stack);
        for (int index  = 1; index < handler.getSlots()-1; index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgrades.add((UpgradeCard)handler.getStackInSlot(index).getItem());
            }
        }
        return upgrades;
    }

    //Returns the upgrade card by upgrade
    public Upgrade getUpgradeByUpgrade(ItemStack stack, Upgrade upgrade){
        List<UpgradeCard> upgrades = getUpgrades(stack);
        for (UpgradeCard upgradeCard : upgrades) {
            if (upgradeCard.getUpgrade().getBaseName().equals(upgrade.getBaseName())){
                return upgradeCard.getUpgrade();
            }
        }
        return null;
    }

    //Returns upgrade stacks
    public List<ItemStack> getUpgradeStacks(ItemStack stack){
        List<ItemStack> upgradeStacks = new ArrayList<>();
        IItemHandler handler = getHandler(stack);
        for (int index  = 1; index < handler.getSlots()-1; index++){
            if (handler.getStackInSlot(index).getItem() != Items.AIR){
                upgradeStacks.add(handler.getStackInSlot(index));
            }
        }
        return upgradeStacks;
    }

    public ItemStack getStackByUpgrade(ItemStack stack, Upgrade upgrade){
        List<ItemStack> upgradeStack = getUpgradeStacks(stack);
        List<UpgradeCard> upgradeCard = getUpgrades(stack);
        for (int index = 0 ; index < upgradeCard.size() ; index++) {
            if (upgradeCard.get(index).getUpgrade().getBaseName().equals(upgrade.getBaseName())){
                return upgradeStack.get(index);
            }
        }
        return null;
    }

////////////////////////////////////////////////////////////////////////
    public Vector3d getDim(BlockRayTraceResult ray, int xRad, int yRad, PlayerEntity player){
        int xRange = xRad;
        int yRange = yRad;
        int zRange = 0;
        if (Math.abs(ray.getDirection().getNormal().getY()) == 1){
            yRange = 0;
            zRange = xRad;
            if (yRad > 0 && xRad == 0){
                yRange = yRad;
            }
            if (yRad == 0 && xRad > 0){
                int yaw = (int)player.getYHeadRot();
                if (yaw <0)
                    yaw += 360;
                int facing = yaw / 45;

                if (facing == 6 || facing == 5 || facing == 2 || facing == 1) {
                    xRange = yRad;
                }
                if (facing == 7 || facing == 8 || facing == 0 || facing == 4 || facing == 3)
                    zRange = yRad;
            }
        }
        if (Math.abs(ray.getDirection().getNormal().getX()) == 1){
            zRange = xRad;
            xRange = 0;
        }
        return new Vector3d(xRange, yRange, zRange);
    }

    public void spawnLight(World world, BlockRayTraceResult lookingAt){
        Direction side = lookingAt.getDirection();
        BlockPos thepos = lookingAt.getBlockPos().relative(side);
        if (world.getBrightness(LightType.BLOCK, thepos) <= 8){
            Material mat = world.getBlockState(thepos).getMaterial();
            if (mat == Material.AIR || mat == Material.WATER || mat == Material.LAVA)
                world.setBlockAndUpdate(thepos.relative(side, -1), ModBlocks.LIGHT.get().defaultBlockState());
            else
                world.setBlockAndUpdate(thepos, ModBlocks.LIGHT.get().defaultBlockState());
        }
    }

    public void magnetOn(List<ItemStack> list, BlockState state, World world, ItemStack stack, BlockPos pos, PlayerEntity player){
        if (!world.isClientSide) {
            if (!list.isEmpty()){
                list.forEach(loot -> {
                    if (getUpgradeByUpgrade(stack, Upgrade.AUTO_SMELT) != null) {
                        IInventory inv = new Inventory(1);
                        inv.setItem(0, loot.copy());
                        Optional<? extends AbstractCookingRecipe> recipe = world.getRecipeManager().getRecipeFor(recipeType, inv, world);
                        loot = recipe.isPresent() ? recipe.get().getResultItem().copy() : loot.copy();
                    }

                    for (int num = 0; num < getFortune(stack)+1; num++) {
                        if (getUpgradeByUpgrade(stack, Upgrade.TRASH) == null){
                            if (!player.inventory.add(loot.copy())) {
                                player.drop(loot.copy(), true);
                            }
                        }else{
                            IItemHandler trashHandler = Trash.getHandler(getStackByUpgrade(stack, Upgrade.TRASH));
                            List<ItemStack> filter = new ArrayList<>();
                            for (int index = 0; index < trashHandler.getSlots(); index++) {
                                if (trashHandler.getStackInSlot(index).getItem() != Items.AIR) {
                                    filter.add(trashHandler.getStackInSlot(index));
                                }
                            }
                            if (!filter.contains(loot.copy())) {
                                if (!player.inventory.add(loot.copy())) {
                                    player.drop(loot.copy(), true);
                                }
                                break;
                            }
                        }
                        if (!smeltingFilter.contains(loot.copy().getItem()))
                            break;
                    }
                });
            }
            player.giveExperiencePoints(state.getBlock().getExpDrop(state, world, pos, getFortune(stack), getSilkTouch(stack)));
        }
    }

    public Boolean breakBlock(ItemStack stack, BlockState state, Block block, BlockPos pos, PlayerEntity player, World world, BlockRayTraceResult ray){
        IItemHandler handler = getHandler(stack);
        List<ItemStack> list = state.getDrops(new LootContext.Builder((ServerWorld) world)
                .withParameter(LootParameters.TOOL, stack)
                .withParameter(LootParameters.ORIGIN, new Vector3d(pos.getX(), pos.getY(), pos.getZ()))
                .withParameter(LootParameters.BLOCK_STATE, state)
        );
        if ((getfuelValue(stack) >= getUseValue(stack) || handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())) && !(block instanceof Light)){
            setFuelValue(stack, 1, player);
            if (state.getDestroySpeed(world, pos) == -1.0 || state.getHarvestLevel() > getHarvestLevel(stack))
                return false;
            world.destroyBlock(pos, false);
            if (getMagnet(stack)){
                magnetOn(list, state, world, stack, pos, player);
            }else{
                if (!list.isEmpty()) {
                    list.forEach(loot -> {
                        if (getUpgradeByUpgrade(stack, Upgrade.AUTO_SMELT) != null) {
                            IInventory inv = new Inventory(1);
                            inv.setItem(0, loot.copy());
                            Optional<? extends AbstractCookingRecipe> recipe = world.getRecipeManager().getRecipeFor(recipeType, inv, world);
                            loot = recipe.isPresent() ? recipe.get().getResultItem().copy() : loot.copy();
                        }
                        for (int num = 0; num < getFortune(stack) + 1; num++) {
                            if (getUpgradeByUpgrade(stack, Upgrade.TRASH) == null){
                                world.addFreshEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, loot.copy()));
                            }else{
                                IItemHandler trashHandler = Trash.getHandler(getStackByUpgrade(stack, Upgrade.TRASH));
                                List<Item> filter = new ArrayList<>();
                                for (int index = 0; index < trashHandler.getSlots(); index++) {
                                    if (trashHandler.getStackInSlot(index).getItem() != Items.AIR) {
                                        filter.add(trashHandler.getStackInSlot(index).getItem());
                                    }
                                }
                                if (!filter.contains(loot.getItem())) {
                                    world.addFreshEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, loot.copy()));
                                    break;
                                }
                            }
                            if (!smeltingFilter.contains(loot.copy().getItem()))
                                break;
                        }
                    });
                }
            }
            if (getUpgradeByUpgrade(stack, Upgrade.LIGHT) != null)
                spawnLight(world, ray);
            return true;
        }
        return false;
    }

    public void areaMine(BlockState state, World world, ItemStack stack, BlockPos pos, PlayerEntity player, BlockRayTraceResult ray){
        int xRad = 0;
        int yRad = 0;

        if (getUpgradeByUpgrade(stack, Upgrade.HORIZONTAL_EXPANSION_1) != null){
            xRad = getUpgradeByUpgrade(stack, Upgrade.HORIZONTAL_EXPANSION_1).getTier();
        }
        if (getUpgradeByUpgrade(stack, Upgrade.VERTICAL_EXPANSION_1) != null) {
            yRad = getUpgradeByUpgrade(stack, Upgrade.VERTICAL_EXPANSION_1).getTier();
        }

        if (ray == null)
            return;
        Vector3d size = getDim(ray, xRad, yRad, player);
        BlockPos nPos = pos;
        if (yRad > 0 && xRad == 0 && ray.getDirection().getAxis().isVertical()){
            nPos = new BlockPos(pos.getX(), pos.getY() - yRad*ray.getDirection().getNormal().getY(), pos.getZ());
        }
        for (int xPos = nPos.getX() - (int)size.x(); xPos <= nPos.getX() + (int)size.x(); ++xPos){
            for (int yPos = nPos.getY() - (int)size.y(); yPos <= nPos.getY() + (int)size.y(); ++yPos){
                for (int zPos = nPos.getZ() - (int)size.z(); zPos <= nPos.getZ() + (int)size.z(); ++zPos){
                    if (!pos.equals(new BlockPos(xPos, yPos, zPos))){
                        BlockPos thePos = new BlockPos(xPos, yPos, zPos);
                        LOGGER.info(thePos);
                        BlockState theState = world.getBlockState(thePos);
                        Block theBlock = theState.getBlock();
                        setFuelValue(stack, 0, player);
                        if (world.getBlockState(thePos).getMaterial() != Material.AIR){
                            breakBlock(stack, theState, theBlock, thePos, player, world, ray);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        /*
            Harvest Level : 0 -> wood/gold
                            1 -> stone
                            2 -> iron
                            3 -> diamond
                            4 -> stronk
         */

        if (getCoolDown(stack) > 0){
            stack.getTag().putInt("CoolDown", (getCoolDown(stack) - 1) < 0 ? 0 : (getCoolDown(stack) - 1));
        }

        if (getCoolDown(stack) == 0 && !((PlayerEntity)entityIn).getCooldowns().isOnCooldown(this)){
            stack.getTag().putInt("HeatValue", (getheatValue(stack) - (int)(25*getCooldownMultiplier(stack))) < 0 ? 0 : (getheatValue(stack) - (int)(25*getCooldownMultiplier(stack))));
        }

        if (getfuelValue(stack) >= base_use_buffer *3/4 || getHandler(stack).getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            stack.getTag().putInt("HarvestLevel", 4);
        }else if (getfuelValue(stack) < base_use_buffer *3/4){
            stack.getTag().putInt("HarvestLevel", 2);
        }

        int fortune = EnchantmentHelper.getEnchantments(stack).get(Enchantments.BLOCK_FORTUNE) != null ? EnchantmentHelper.getEnchantments(stack).get(Enchantments.BLOCK_FORTUNE) : 0;
        int silk = EnchantmentHelper.getEnchantments(stack).get(Enchantments.SILK_TOUCH) != null ? EnchantmentHelper.getEnchantments(stack).get(Enchantments.SILK_TOUCH) : 0;
        stack.getEnchantmentTags().clear();

        if (EnchantmentHelper.getEnchantments(stack).get(Enchantments.FIRE_ASPECT) == null){
            if (getfuelValue(stack) >= base_use_buffer /2 && getfuelValue(stack) < base_use_buffer *3/4){
                stack.enchant(Enchantments.FIRE_ASPECT, 1);
            }else if (getfuelValue(stack) >= base_use_buffer *3/4 || getHandler(stack).getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
                stack.enchant(Enchantments.FIRE_ASPECT, 2);
            }
        }else if (EnchantmentHelper.getEnchantments(stack).get(Enchantments.FIRE_ASPECT) == 2){
            if (getfuelValue(stack) >= base_use_buffer /2 && getfuelValue(stack) < base_use_buffer *3/4)
                stack.enchant(Enchantments.FIRE_ASPECT, 1);
        }

        if (fortune != 0)
            stack.enchant(Enchantments.BLOCK_FORTUNE, fortune);
        if (silk != 0)
            stack.enchant(Enchantments.SILK_TOUCH, silk);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int fireAspect = EnchantmentHelper.getEnchantments(stack).get(Enchantments.FIRE_ASPECT) != null ? EnchantmentHelper.getEnchantments(stack).get(Enchantments.FIRE_ASPECT) : 0;
        if (!getHandler(stack).getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
            stack.getTag().putInt("FuelValue", getfuelValue(stack)-base_use*(fireAspect*2 == 0 ? 1 : fireAspect*2));
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    ////////////////////////////////////////////////////////////////////////

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        ItemStack stack = player.getItemInHand(handIn).getStack();
        IItemHandler handler = getHandler(stack);
        if (!world.isClientSide){
            if (getheatValue(stack) >= base_heat_buffer) {
                return ActionResult.consume(stack);
            }
            BlockRayTraceResult ray = WorldUtil.getLookingAt(world, player, RayTraceContext.FluidMode.NONE, getRange(stack));
            BlockPos pos = ray.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            setFuelValue(stack, 0, player);
            if (state.getMaterial() != Material.AIR){
                setNBT(stack);
                stack.enchant(Enchantments.BLOCK_FORTUNE, getFortune(stack));
                stack.enchant(Enchantments.SILK_TOUCH, getSilkTouch(stack));
                if (getfuelValue(stack) >= getUseValue(stack) || handler.getStackInSlot(0).getItem().equals(Upgrade.UNIFUEL.getCard().getItem())){
                    player.playSound(SoundEvents.FIRECHARGE_USE, 0.5f, 1.0f);
                    if (player.isCrouching()){
                        breakBlock(stack, state, block, pos, player, world, ray);
                    }else{
                        breakBlock(stack, state, block, pos, player, world, ray);
                        areaMine(state, world, stack,  pos, player, ray);
                    }

                }
                stack.getEnchantmentTags().clear();
            }
        }
        return ActionResult.consume(stack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        //return stack.getTag().getInt("CoolDown") > 0;
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        //return 1-(stack.getTag().getInt("CoolDown")/(float)base_coolDown);
        return 1;
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
        if (this.getClass() == BurnerGun.class){
            setNBT(stack);
            return new BurnerGunProvider();
        }else{
           return super.initCapabilities(stack, oldCapNbt);
        }
    }

    public static BurnerGunHandler getHandler(ItemStack itemStack) {
        IItemHandler handler = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (handler == null || !(handler instanceof BurnerGunHandler)) {
            LOGGER.error("TestGun did not have the expected ITEM_HANDLER_CAPABILITY");
            return new BurnerGunHandler(BurnerGunContainer.MAX_EXPECTED_TEST_SLOT_COUNT);
        }
        return (BurnerGunHandler) handler;
    }

    private final String BASE_NBT_TAG = "base";
    private final String CAPABILITY_NBT_TAG = "cap";


    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();
        BurnerGunHandler handler = getHandler(stack);
        CompoundNBT capabilityTag = handler.serializeNBT();
        CompoundNBT combinedTag = new CompoundNBT();
        if (baseTag != null) {
            combinedTag.put(BASE_NBT_TAG, baseTag);
        }
        if (capabilityTag != null) {
            combinedTag.put(CAPABILITY_NBT_TAG, capabilityTag);
        }
        stack.setTag(baseTag);
        setNBT(stack);
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

        BurnerGunHandler handler = getHandler(stack);
        handler.deserializeNBT(capabilityTag);
    }


    private static final Logger LOGGER = LogManager.getLogger();








}
