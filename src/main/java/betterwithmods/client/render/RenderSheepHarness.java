package betterwithmods.client.render;

import betterwithmods.BWMod;
import betterwithmods.event.MobAIEvent;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/15/16
 */
public class RenderSheepHarness extends RenderSheep {
    private static final ResourceLocation HARNESS = new ResourceLocation(BWMod.MODID,"textures/entity/sheep_harness.png");

    public RenderSheepHarness(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSheep2(),0.7f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySheep entity) {
        if(MobAIEvent.getHarness(entity) != null)
            return HARNESS;
        return super.getEntityTexture(entity);
    }
}
