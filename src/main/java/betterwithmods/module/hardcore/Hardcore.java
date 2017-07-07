package betterwithmods.module.hardcore;

import betterwithmods.client.gui.GuiStatus;
import betterwithmods.module.CompatModule;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.hchunger.HCHunger;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class Hardcore extends CompatModule {
    public Hardcore() {
        priority = 1;
    }

    @Override
    public void addCompatFeatures() {
        registerCompatFeature("applecore", "betterwithmods.module.hardcore.hchunger.HCHunger");
    }

    @Override
    public void addFeatures() {
        this.addCompatFeatures();
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
        registerFeature(new HCHunting());
        registerFeature(new HCInfo());
        registerFeature(new HCInjury());
        registerFeature(new HCJumping());
        registerFeature(new HCLumber());
        registerFeature(new HCMelon());
        registerFeature(new HCNames());
        registerFeature(new HCOres());
        registerFeature(new HCPiles());
        registerFeature(new HCRedstone());
        registerFeature(new HCSeeds());
        registerFeature(new HCSaw());
        registerFeature(new HCSheep());
        registerFeature(new HCSpawn());
        registerFeature(new HCStructures());
        registerFeature(new HCStumping());
        registerFeature(new HCTools());
        registerFeature(new HCTorches());
        registerFeature(new HCVillages());
        this.load();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

    }

    @Override
    public void initClient(FMLInitializationEvent event) {
        super.initClient(event);
        GuiStatus.isGloomLoaded = ModuleLoader.isFeatureEnabled(HCGloom.class);
        GuiStatus.isHungerLoaded = ModuleLoader.isFeatureEnabled(HCHunger.class);
        GuiStatus.isInjuryLoaded = ModuleLoader.isFeatureEnabled(HCInjury.class);
    }

    @Override
    public String getModuleDescription() {
        return "Changes to the game that make it more challenging";
    }
}
