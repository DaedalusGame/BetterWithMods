package betterwithmods.client.render;

import betterwithmods.BWMod;
import betterwithmods.event.MobAIEvent;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/15/16
 */
public class RenderCowHarness extends RenderCow {
    private static final ResourceLocation HARNESS = new ResourceLocation(BWMod.MODID,"textures/entity/cow_harness.png");

    public RenderCowHarness(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelCow(), 0.7F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCow entity) {
        if(MobAIEvent.getHarness(entity) != null)
            return HARNESS;
        return super.getEntityTexture(entity);
    }
}
