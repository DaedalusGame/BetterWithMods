package betterwithmods.integration;

import betterwithmods.integration.minetweaker.*;
import minetweaker.MineTweakerAPI;

/**
 * Created by tyler on 9/4/16.
 */
public class MineTweaker {
    public static void init() {
        MineTweakerAPI.registerClass(Saw.class);
        MineTweakerAPI.registerClass(Kiln.class);
        MineTweakerAPI.registerClass(Cauldron.class);
        MineTweakerAPI.registerClass(StokedCauldron.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(StokedCrucible.class);
        MineTweakerAPI.registerClass(Mill.class);
        MineTweakerAPI.registerClass(Bouyancy.class);
    }
}
