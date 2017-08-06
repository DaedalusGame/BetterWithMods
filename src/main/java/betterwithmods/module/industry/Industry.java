package betterwithmods.module.industry;

import betterwithmods.module.Module;
import betterwithmods.module.industry.multiblocks.Multiblocks;
import betterwithmods.module.industry.pollution.Pollution;

public class Industry extends Module {
    @Override
    public void addFeatures() {
        registerFeature(new Pollution());
        registerFeature(new WeatherControl());
        registerFeature(new Multiblocks());
    }

    @Override
    public String getModuleDescription() {
        return "The consequences of heavy industry.";
    }

}
