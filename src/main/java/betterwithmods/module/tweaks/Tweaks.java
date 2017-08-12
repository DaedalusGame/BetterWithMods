package betterwithmods.module.tweaks;

import betterwithmods.module.Module;

/**
 * Created by tyler on 4/20/17.
 */
public class Tweaks extends Module {
    @Override
    public void addFeatures() {
        registerFeature(new AxeLeaves());
        registerFeature(new CreeperShearing());
        registerFeature(new Dung());
        registerFeature(new EasyBreeding());
        registerFeature(new EggDrops());
        registerFeature(new EquipmentDrop());
        registerFeature(new ImprovedFlee());
        registerFeature(new HeadDrops());
        registerFeature(new HighEfficiencyRecipes());
        registerFeature(new KilnCharcoal());
        registerFeature(new KilnSmelting());
        registerFeature(new MobSpawning());
        registerFeature(new MossGeneration());
        registerFeature(new RenewableEndstone());
        registerFeature(new RSBlockGlow());
        registerFeature(new Sinkholes());
        registerFeature(new MysteryMeat());
        registerFeature(new GrassPath());
        registerFeature(new PlaceClay());
    }

    @Override
    public String getModuleDescription() {
        return "General Tweaks to the game, Vanilla or BWM itself";
    }
}
