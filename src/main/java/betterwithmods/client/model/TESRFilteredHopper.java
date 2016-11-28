package betterwithmods.client.model;

import betterwithmods.blocks.tile.TileEntityFilteredHopper;
import betterwithmods.client.model.filters.ModelTransparent;
import betterwithmods.client.model.filters.ModelWithResource;
import betterwithmods.client.model.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TESRFilteredHopper extends TileEntitySpecialRenderer<TileEntityFilteredHopper> {
    private ModelWithResource model;
    private ItemStack stack;
    private int occupiedStacks;


    @Override
    public void renderTileEntityAt(TileEntityFilteredHopper tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile != null) {
            if (tile.getSubtype() > 0) {
                model = tile.getModel();
                if (model == null && tile.filterType > 0) {
                    if (tile.getFilterStack() != null) {
                        model = new ModelTransparent(RenderUtils.getResourceLocation(tile.getFilterStack()));
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
                if (occupiedStacks != tile.filledSlots())
                    occupiedStacks = tile.filledSlots();
                double fillOffset = 0.65D * occupationMod(tile);
                if (fillOffset > 0D)
                    RenderUtils.renderFill(new ResourceLocation("minecraft", "blocks/gravel"), tile.getPos(), x, y, z, 0.125D, 0.25D, 0.125D, 0.875D, 0.25D + fillOffset, 0.875D);
            }
        }
    }

    private float occupationMod(TileEntityFilteredHopper tile) {
        float visibleSlots = (float) tile.getMaxVisibleSlots();
        return (float) occupiedStacks / visibleSlots;
    }
}
