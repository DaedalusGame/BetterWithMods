package betterwithmods.module.industry;

import betterwithmods.module.Module;
import betterwithmods.module.industry.pollution.Pollution;

public class Industry extends Module {
    @Override
    public void addFeatures() {
        registerFeature(new Pollution());
    }

    @Override
    public String getModuleDescription() {
        return "The consequences of heavy industry.";
    }
}
