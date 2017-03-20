package betterwithmods.client.render;

import betterwithmods.BWMod;
import betterwithmods.config.BWConfig;
import betterwithmods.event.BreedingHardnessEvent;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/15/16
 */
public class RenderCowHarness extends RenderCow {
    private static final ResourceLocation HARNESS = BWConfig.kidFriendly ? new ResourceLocation(BWMod.MODID, "textures/entity/cow_harness_kf.png") : new ResourceLocation(BWMod.MODID, "textures/entity/cow_harness.png");

    public RenderCowHarness(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCow entity) {
        if (BreedingHardnessEvent.getHarness(entity) != ItemStack.EMPTY)
            return HARNESS;
        return super.getEntityTexture(entity);
    }
}
