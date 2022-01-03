package com.nindybun.burnergun.common.entities.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nindybun.burnergun.common.entities.MegaBlazeProjectileEntity;
import com.nindybun.burnergun.common.entities.renders.MegaBlazeProjectileRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;

public class MegaBlazeProjectileModel<T extends Entity> extends EntityModel<MegaBlazeProjectileEntity> {

    @Override
    public void setupAnim(MegaBlazeProjectileEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    }

    @Override
    public void renderToBuffer(MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {

    }
}
