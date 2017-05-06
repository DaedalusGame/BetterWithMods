package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * Created by tyler on 5/6/17.
 */
public class HCInfo extends Feature {
    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.getServer().getEntityWorld().getGameRules().setOrCreateGameRule("reducedDebugInfo","true");
    }

    @Override
    public String getFeatureDescription() {
        return "Enables reducedDebugInfo by default for a more authentic BWM experience";
    }
}
