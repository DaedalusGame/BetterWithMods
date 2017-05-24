package betterwithmods.integration;

import betterwithmods.module.hardcore.HCHunger;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 5/13/17.
 */
public class ActuallyAdditions extends CompatFeature {
    public ActuallyAdditions() {
        super("actuallyadditions");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        HCHunger.setDessert(getItemFood(new ResourceLocation(this.modid, "item_food")));
    }

}
