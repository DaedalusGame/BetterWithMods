package betterwithmods.integration.minetweaker;

import betterwithmods.integration.ModIntegration;
import minetweaker.MineTweakerAPI;

/**
 * Created by tyler on 9/4/16.
 */
public class MineTweaker extends ModIntegration {


    public  void init() {
        MineTweakerAPI.registerClass(Saw.class);
        MineTweakerAPI.registerClass(Kiln.class);
        MineTweakerAPI.registerClass(Cauldron.class);
        MineTweakerAPI.registerClass(StokedCauldron.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(StokedCrucible.class);
        MineTweakerAPI.registerClass(Mill.class);
        MineTweakerAPI.registerClass(Buoyancy.class);
    }
}
