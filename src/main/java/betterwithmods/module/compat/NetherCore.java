package betterwithmods.module.compat;

import betterwithmods.module.CompatFeature;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/26/16
 */
public class NetherCore extends CompatFeature {
    public NetherCore() {
        super("nethercore");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        //TODO
//        Item nether_spore = getItem(new ResourceLocation(modid, "nether_spore"));
//        BWMRecipes.removeRecipes(nether_spore, 0);
//        CauldronRecipes.addCauldronRecipe(new ItemStack(nether_spore), new Object[]{new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.MYCELIUM), new ItemStack(BWMBlocks.URN, 8, 8), new ItemStack(Items.NETHER_WART)});
    }

}
