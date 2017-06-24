package betterwithmods.module.gameplay;

import betterwithmods.module.Feature;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by primetoxinz on 6/6/17.
 */
public class Debarking extends Feature {
    public static boolean requiresSneaking;
    @Override
    public void setupConfig() {
        requiresSneaking = loadPropBool("Requires Sneaking", "Deprecated", true);
    }

    @Override
    public String getFeatureDescription() {
        return "Deprecated";
    }

    @SubscribeEvent
    public void debarkLog(PlayerInteractEvent.RightClickBlock evt) {
        //TODO entirely remove debarked logs...
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
