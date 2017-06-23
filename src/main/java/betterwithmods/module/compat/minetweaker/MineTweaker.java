package betterwithmods.module.compat.minetweaker;

import betterwithmods.module.CompatFeature;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 9/4/16.
 */
@SuppressWarnings("unused")
public class MineTweaker extends CompatFeature {

    public MineTweaker() {
        super("minetweaker3");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MineTweakerAPI.registerClass(Saw.class);
        MineTweakerAPI.registerClass(Kiln.class);
        MineTweakerAPI.registerClass(Cauldron.class);
        MineTweakerAPI.registerClass(StokedCauldron.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(StokedCrucible.class);
        MineTweakerAPI.registerClass(Mill.class);
        MineTweakerAPI.registerClass(Buoyancy.class);
//        MineTweakerAPI.registerClass(HopperFilter.class);
        MineTweakerAPI.registerClass(SteelAnvil.class);
        MineTweakerAPI.registerClass(ChopRecipe.class);
        MineTweakerAPI.registerClass(Turntable.class);
        MineTweakerAPI.registerClass(Piles.class);
        if(Loader.isModLoaded("applecore")) {
            MineTweakerAPI.registerClass(FoodValue.class);
        }
    }

}
