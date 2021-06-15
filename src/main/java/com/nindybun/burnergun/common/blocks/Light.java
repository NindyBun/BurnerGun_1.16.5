package com.nindybun.burnergun.common.blocks;

import com.nindybun.burnergun.client.particles.ModParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class Light extends Block {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    public Light() {
        super(
                Properties
                        .create(Material.MISCELLANEOUS)
                        .doesNotBlockMovement()
                        .hardnessAndResistance(0.0f)
                        .setLightLevel(value -> 15)
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockRenderType getRenderType(BlockState state) {
        // We still make effect blocks invisible because all effects (scaling block, transparent box) are dynamic so they has to be in the TER
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }


    //@deprecated call via {@link BlockState#getPushReaction()} whenever possible. Implementing/overriding is fine.

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext) {
        return SHAPE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.5D;
        double d2 = (double) pos.getZ() + 0.5D;

        worldIn.addParticle(ModParticles.LIGHT_PARTICLE.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }
}