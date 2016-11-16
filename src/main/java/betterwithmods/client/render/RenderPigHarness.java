package betterwithmods.client.render;

import betterwithmods.BWMod;
import betterwithmods.event.MobAIEvent;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/15/16
 */
public class RenderPigHarness extends RenderPig {
    private static final ResourceLocation HARNESS = new ResourceLocation(BWMod.MODID,"textures/entity/pig_harness.png");


    public RenderPigHarness(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPig(),0.7f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPig entity) {
        if(MobAIEvent.getHarness(entity) != null)
            return HARNESS;
        return super.getEntityTexture(entity);
    }
}
