package betterwithmods.integration;

import betterwithmods.BWRegistry;
import betterwithmods.blocks.BlockImmersiveAxle;

/**
 * Created by tyler on 9/10/16.
 */
public class ImmersiveEngineering {
    public static void preInit() {
        BWRegistry.treatedAxle = new BlockImmersiveAxle();
    }
}
