package betterwithmods.client.model;

import betterwithmods.common.blocks.mechanical.tile.TileEntityWindmillVertical;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRVerticalWindmill extends TileEntitySpecialRenderer<TileEntityWindmillVertical> {
    private final ModelVerticalShafts modelShafts = new ModelVerticalShafts();
    private final ModelVerticalSails modelSails = new ModelVerticalSails();
    private final ModelVerticalFrame modelFrame = new ModelVerticalFrame();

    @Override
    public void render(TileEntityWindmillVertical te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        float rotation = (te.getCurrentRotation() + (te.getMechanicalOutput(EnumFacing.UP) == 0 ? 0 : partialTicks * te.getPrevRotation()));
        rotation = -rotation;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        modelShafts.setRotateAngle(modelShafts.axle, 0, (float) Math.toRadians(rotation), 0);
        modelSails.setRotateAngleForSails(0, (float) Math.toRadians(rotation), 0);
        modelFrame.setRotateAngle(modelFrame.axle, 0, (float) Math.toRadians(rotation), 0);
        this.bindTexture(new ResourceLocation("minecraft", "textures/blocks/log_oak.png"));
        this.modelShafts.render(0.0625F);
        this.bindTexture(new ResourceLocation("minecraft", "textures/blocks/planks_oak.png"));
        this.modelFrame.render(0.0625F);
        this.bindTexture(new ResourceLocation("minecraft", "textures/blocks/wool_colored_white.png"));
        this.modelSails.render(0.0625F, te);
        GlStateManager.popMatrix();
    }

}
