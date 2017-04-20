package betterwithmods.module.hardcore;

import betterwithmods.module.Module;

/**
 * Created by tyler on 4/20/17.
 */
public class Hardcore extends Module {
    @Override
    public void addFeatures() {
        registerFeature(new HCBeds());
        registerFeature(new HCBuckets());
        registerFeature(new HCBuoy());
        registerFeature(new HCDiamond());
        registerFeature(new HCEndermen());
        registerFeature(new HCGunpowder());
        registerFeature(new HCHardness());
        registerFeature(new HCHunger());
        registerFeature(new HCLumber());
        registerFeature(new HCMelon());
        registerFeature(new HCOres());
        registerFeature(new HCPiles());
        registerFeature(new HCRedstone());
        registerFeature(new HCSpawn());
        registerFeature(new HCStructures());
        registerFeature(new HCStumping());
        registerFeature(new HCTools());
    }

}
