package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.Feature;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class HempSeed extends Feature {
    @Override
    public String getFeatureDescription() {
        return "Hemp Seeds will drop when breaking Tall Grass";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
            MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1, 0), 5);
    }
}
