package betterwithmods.module.hardcore;

import betterwithmods.module.Feature;
import net.minecraft.block.BlockCrops;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 5/14/17.
 */
public class HCBonemeal extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Removes the ability to instant-grow crops with bonemeal";
    }
    @SubscribeEvent
    public void onBonemeal(BonemealEvent e) {
        if(e.getBlock().getBlock() instanceof BlockCrops)
            e.setCanceled(true);
    }
    @Override
    public boolean hasSubscriptions() {
        return true;
    }
}
