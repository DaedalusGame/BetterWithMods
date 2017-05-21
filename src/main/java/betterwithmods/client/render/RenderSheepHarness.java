package betterwithmods.client.render;

import betterwithmods.BWMod;
import betterwithmods.event.BreedingHardnessEvent;
import betterwithmods.module.gameplay.Gameplay;
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
    private static final ResourceLocation HARNESS = new ResourceLocation(BWMod.MODID, "textures/entity/sheep_harness.png");
    private static final ResourceLocation HARNESS_KF = new ResourceLocation(BWMod.MODID, "textures/entity/sheep_harness_kf.png");
    public RenderSheepHarness(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySheep entity) {
        if (!BreedingHardnessEvent.getHarness(entity).isEmpty())
            return Gameplay.kidFriendly ? HARNESS_KF : HARNESS;
        return super.getEntityTexture(entity);
    }
}
