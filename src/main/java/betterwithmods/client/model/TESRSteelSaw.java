package betterwithmods.client.model;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.IBlockActive;
import betterwithmods.common.blocks.mechanical.tile.TileSteelSaw;
import betterwithmods.util.DirUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class TESRSteelSaw extends TileEntitySpecialRenderer<TileSteelSaw> {

    @Override
    public void render(TileSteelSaw te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = getWorld().getBlockState(te.getPos());
        boolean active = state.getValue(IBlockActive.ACTIVE);
        EnumFacing.Axis axis = state.getValue(DirUtils.AXIS);
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);
            GlStateManager.disableRescaleNormal();
            GlStateManager.pushMatrix();
            {
                IBlockState axle = BWMBlocks.STEEL_AXLE.getDefaultState().withProperty(DirUtils.AXIS, axis).withProperty(IBlockActive.ACTIVE, active);
                renderBlock(te, axle);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(0.5, 0.5, (axis == EnumFacing.Axis.Z ? -1 : 1) * 0.5);
                if (active) {
                    long angle = (axis != EnumFacing.Axis.Z ? -1 : 1) * (System.currentTimeMillis() / 2) % 360;
                    switch (axis) {

                        case X:
                            GlStateManager.rotate(angle, 1, 0, 0);
                            break;
                        case Y:
                            GlStateManager.rotate(angle, 0, 1, 0);
                            break;
                        case Z:
                            GlStateManager.rotate(angle, 0, 0, 1);
                            break;
                    }

                }
                renderBlock(te, state);
            }
            GlStateManager.popMatrix();

        }
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void renderBlock(TileSteelSaw te, IBlockState state) {
        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();
        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, te.getPos(), bufferBuilder, true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
    }

    @Override
    public boolean isGlobalRenderer(TileSteelSaw te) {
        return true;
    }
}
