package com.nindybun.burnergun.common.entities.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nindybun.burnergun.common.BurnerGun;
import com.nindybun.burnergun.common.entities.MegaBlazeProjectileEntity;
import com.nindybun.burnergun.common.entities.models.MegaBlazeProjectileModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class MegaBlazeProjectileRenderer extends EntityRenderer<MegaBlazeProjectileEntity> {
    private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(BurnerGun.MOD_ID, "textures/entities/megablaze/mega_blaze_projectile.png");

    protected MegaBlazeProjectileRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }


    @Override
    public ResourceLocation getTextureLocation(MegaBlazeProjectileEntity p_110775_1_) {
        return RESOURCE_LOCATION;
    }
}
