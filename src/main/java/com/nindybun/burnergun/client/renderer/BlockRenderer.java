package com.nindybun.burnergun.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nindybun.burnergun.common.BurnerGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;


@Mod.EventBusSubscriber(modid = BurnerGun.MOD_ID, value = Dist.CLIENT)
public class BlockRenderer {
    public static void drawBoundingBoxAtBlockPos(MatrixStack matrixStackIn, AxisAlignedBB aabbIn, float red, float green, float blue, float alpha, BlockPos pos) {
        Vector3d cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        double camX = cam.x, camY = cam.y, camZ = cam.z;

        matrixStackIn.pushPose();

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        drawShapeOutline(matrixStackIn, VoxelShapes.create(aabbIn), pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ, red, green, blue, alpha);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        matrixStackIn.popPose();
    }

    private static void drawShapeOutline(MatrixStack matrixStack, VoxelShape voxelShape, double originX, double originY, double originZ, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrixStack.last().pose();

        IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        IVertexBuilder bufferIn = renderTypeBuffer.getBuffer(RenderType.lines());

        voxelShape.forAllEdges((x0, y0, z0, x1, y1, z1) -> {
            bufferIn.vertex(matrix4f, (float) (x0 + originX), (float) (y0 + originY), (float) (z0 + originZ)).color(red, green, blue, alpha).endVertex();
            bufferIn.vertex(matrix4f, (float) (x1 + originX), (float) (y1 + originY), (float) (z1 + originZ)).color(red, green, blue, alpha).endVertex();
        });

        renderTypeBuffer.endBatch(RenderType.lines());
    }

    @SubscribeEvent
    public static void onRenderWorldEvent(RenderWorldLastEvent e) {
        final GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
        gameRenderer.resetProjectionMatrix(e.getProjectionMatrix());

        final AxisAlignedBB test = new AxisAlignedBB(0,0,0,1,1,1);
        drawBoundingBoxAtBlockPos(e.getMatrixStack(), test, 1.0F, 0.0F, 0.0F, 1.0F, new BlockPos(0,64,0));
    }

}
