package betterwithmods.module.tweaks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by tyler on 4/20/17.
 */
public class HempSeed extends Feature {

    boolean hoeForSeeds = loadPropBool("Hoe for seeds", "Hemp Seeds drop when tilling ground with a hoe", true);
    boolean tallgrassForSeeds = loadPropBool("Tall grass Hemp Seeds", "Hemp Seeds will drop when breaking Tall Grass",true);
    @Override
    public void setupConfig() {

    }

    @Override
    public String getFeatureDescription() {
        return "Controls how Hemp Seeds are gained";
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if(tallgrassForSeeds)
            MinecraftForge.addGrassSeed(new ItemStack(BWMBlocks.HEMP, 1, 0), 5);
    }

    @Override
    public boolean hasSubscriptions() {
        return true;
    }

    @SubscribeEvent
    public void onHoe(UseHoeEvent e) {
        if(!hoeForSeeds)
            return;
        ItemStack stack = ForgeHooks.getGrassSeed(e.getWorld().rand,0);
        if(stack.isItemEqual(new ItemStack(BWMBlocks.HEMP))) {
            InvUtils.ejectStackWithOffset(e.getWorld(),e.getPos(),stack);
        }
    }
}
