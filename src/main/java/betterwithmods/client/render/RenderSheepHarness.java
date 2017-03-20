package betterwithmods.client.render;

import betterwithmods.BWMod;
import betterwithmods.config.BWConfig;
import betterwithmods.event.BreedingHardnessEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/15/16
 */
public class RenderSheepHarness extends RenderSheep {
    private static final ResourceLocation HARNESS = BWConfig.kidFriendly ? new ResourceLocation(BWMod.MODID, "textures/entity/sheep_harness_kf.png") : new ResourceLocation(BWMod.MODID, "textures/entity/sheep_harness.png");

    public RenderSheepHarness(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySheep entity) {
        if (BreedingHardnessEvent.getHarness(entity) != ItemStack.EMPTY)
            return HARNESS;
        return super.getEntityTexture(entity);
    }
}
