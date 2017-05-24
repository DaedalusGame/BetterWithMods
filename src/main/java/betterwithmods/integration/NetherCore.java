package betterwithmods.integration;

import betterwithmods.common.BWMBlocks;
import betterwithmods.module.gameplay.CauldronRecipes;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
        Item nether_spore = getItem(new ResourceLocation(modid, "nether_spore"));
        RecipeUtils.removeRecipes(nether_spore, 0);
        CauldronRecipes.addCauldronRecipe(new ItemStack(nether_spore), new Object[]{new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.MYCELIUM), new ItemStack(BWMBlocks.URN, 8, 8), new ItemStack(Items.NETHER_WART)});
    }

}
