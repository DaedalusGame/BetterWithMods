package betterwithmods.client.model;

import betterwithmods.blocks.tile.TileEntityCrucible;
import betterwithmods.client.model.render.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRCrucible extends TileEntitySpecialRenderer<TileEntityCrucible>
{
    private int occupiedStacks;

    @Override
    public void renderTileEntityAt(TileEntityCrucible tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if(tile != null) {
            if(occupiedStacks != tile.filledSlots())
                occupiedStacks = tile.filledSlots();
            if(occupiedStacks != 0) {
                float fillOffset = 0.875F * occupationMod(tile);
                RenderUtils.renderFill(new ResourceLocation("minecraft", "blocks/gravel"), tile.getPos(), x, y, z, 0.123D, 0.125D, 0.123D, 0.877D, 0.123D + fillOffset, 0.877D);
            }
        }
    }

    private float occupationMod(TileEntityCrucible tile) {
        float visibleSlots = (float)tile.getMaxVisibleSlots();
        return (float)occupiedStacks / visibleSlots;
    }
}
