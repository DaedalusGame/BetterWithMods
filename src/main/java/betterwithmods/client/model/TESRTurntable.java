package betterwithmods.client.model;

import betterwithmods.blocks.tile.TileEntityTurntable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRTurntable extends TileEntitySpecialRenderer<TileEntityTurntable> {
    private ModelTurntableKnob knob;
    private int position = 0;

    @Override
    public void renderTileEntityAt(TileEntityTurntable tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (position != tile.getTimerPos()) {
            position = tile.getTimerPos();
            knob = new ModelTurntableKnob(position);
        } else if (knob == null)
            knob = new ModelTurntableKnob(position);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
        this.bindTexture(new ResourceLocation("minecraft", "textures/blocks/planks_oak.png"));
        knob.render(0.0625F);
        GlStateManager.popMatrix();
    }
}
