package betterwithmods.module.hardcore;

import betterwithmods.module.Module;

/**
 * Created by tyler on 4/20/17.
 */
public class Hardcore extends Module {
    @Override
    public void addFeatures() {
        registerFeature(new HCArmor());
        registerFeature(new HCBeds());
        registerFeature(new HCBonemeal());
        registerFeature(new HCBuckets());
        registerFeature(new HCBuoy());
        registerFeature(new HCDiamond());
        registerFeature(new HCEndermen());
        registerFeature(new HCGloom());
        registerFeature(new HCGunpowder());
        registerFeature(new HCHardness());
        registerFeature(new HCHunger());
        registerFeature(new HCHunting());
        registerFeature(new HCInfo());
        registerFeature(new HCJumping());
        registerFeature(new HCLumber());
        registerFeature(new HCMelon());
        registerFeature(new HCNames());
        registerFeature(new HCOres());
        registerFeature(new HCPiles());
        registerFeature(new HCRedstone());
        registerFeature(new HCSpawn());
        registerFeature(new HCStructures());
        registerFeature(new HCStumping());
        registerFeature(new HCTools());
        registerFeature(new HCTorches());
    }

    @Override
    public String getModuleDescription() {
        return "Changes to the game that make it more challenging";
    }
}
