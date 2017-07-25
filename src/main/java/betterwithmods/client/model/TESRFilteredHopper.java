package betterwithmods.client.model;

import betterwithmods.client.model.filters.ModelTransparent;
import betterwithmods.client.model.filters.ModelWithResource;
import betterwithmods.client.model.render.RenderUtils;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRFilteredHopper extends TileEntitySpecialRenderer<TileEntityFilteredHopper> {
    private ModelWithResource model;
    private int occupiedStacks;

    @Override
    public void render(TileEntityFilteredHopper te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te != null) {
            if (te.getSubtype() > 0) {
                model = te.getModel();
                if (model == null && te.filterType > 0) {
                    if (!te.getFilterStack().isEmpty()) {
                        model = new ModelTransparent(RenderUtils.getResourceLocation(te.getFilterStack()));
                    }
                }
                if (model != null) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
                    this.bindTexture(model.getResource());
                    model.render(0.0622F);
                    GlStateManager.popMatrix();
                }

            } else if (model != null) {
                model = null;
            }
            if (model == null || (model != null && !model.isSolid())) {
                if (occupiedStacks != te.filledSlots())
                    occupiedStacks = te.filledSlots();
                double fillOffset = 0.65D * occupationMod(te);
                if (fillOffset > 0D)
                    RenderUtils.renderFill(new ResourceLocation("minecraft", "blocks/gravel"), te.getPos(), x, y, z, 0.125D, 0.25D, 0.125D, 0.875D, 0.25D + fillOffset, 0.875D);
            }
        }
    }


    private float occupationMod(TileEntityFilteredHopper tile) {
        float visibleSlots = (float) tile.getMaxVisibleSlots();
        return (float) occupiedStacks / visibleSlots;
    }
}
