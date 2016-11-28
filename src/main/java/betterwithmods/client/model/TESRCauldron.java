package betterwithmods.client.model;

import betterwithmods.BWMod;
import betterwithmods.blocks.tile.TileEntityCauldron;
import betterwithmods.client.model.render.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TESRCauldron extends TileEntitySpecialRenderer<TileEntityCauldron> {
    private int occupiedStacks;

    @Override
    public void renderTileEntityAt(TileEntityCauldron tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile != null) {
            if (occupiedStacks != tile.filledSlots())
                occupiedStacks = tile.filledSlots();
            float fillOffset = 0.75F * occupationMod(tile);
            RenderUtils.renderFill(new ResourceLocation(BWMod.MODID, "blocks/cauldron_contents"), tile.getPos(), x, y, z, 0.123D, 0.125D, 0.123D, 0.877D, 0.248D + fillOffset, 0.877D);
        }
    }

    private float occupationMod(TileEntityCauldron tile) {
        float visibleSlots = (float) tile.getMaxVisibleSlots();
        return (float) occupiedStacks / visibleSlots;
    }
}
