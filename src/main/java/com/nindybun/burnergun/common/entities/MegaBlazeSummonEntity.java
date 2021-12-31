package com.nindybun.burnergun.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class MegaBlazeSummonEntity extends ItemEntity {

    public MegaBlazeSummonEntity(EntityType<? extends ItemEntity> p_i50217_1_, World p_i50217_2_) {
        super(p_i50217_1_, p_i50217_2_);
    }

    public MegaBlazeSummonEntity(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }

    @Override
    public boolean isInLava() {
        return super.isInLava();
    }

    @Override
    public void tick() {
        LOGGER.info(this::isInLava);
        super.tick();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
    }

    public static MegaBlazeSummonEntity copy(ItemEntity oldItemEntity){
        MegaBlazeSummonEntity newItemEntity =  new MegaBlazeSummonEntity(oldItemEntity.level, oldItemEntity.getX(), oldItemEntity.getY(), oldItemEntity.getZ(), oldItemEntity.getItem());
        newItemEntity.setDeltaMovement(oldItemEntity.getDeltaMovement());
        newItemEntity.lifespan = oldItemEntity.lifespan;
        return newItemEntity;
    }
}
