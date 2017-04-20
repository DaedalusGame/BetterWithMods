package betterwithmods.module.tweaks;

import betterwithmods.module.Feature;

/**
 * Created by tyler on 4/20/17.
 */
public class EquipmentDrop extends Feature {
    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @Override
    public String getFeatureDescription() {
        return "Mobs have a 100% chance to drop any equipment";
    }
}
