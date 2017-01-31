package betterwithmods.integration;

import betterwithmods.BWCrafting;
import betterwithmods.BWMBlocks;
import betterwithmods.BWMod;
import betterwithmods.util.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/26/16
 */
public class NetherCore implements ICompatModule {
    public static final String MODID = "nethercore";

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {
        BWMod.logger.info("Replacing " + MODID + " nether spore recipe");
        Item nether_spore = Item.REGISTRY.getObject(new ResourceLocation(MODID, "nether_spore"));
        System.out.println(nether_spore);
        RecipeUtils.removeRecipes(nether_spore, 0);
        BWCrafting.addCauldronRecipe(new ItemStack(nether_spore),
                new ItemStack[]{new ItemStack(Blocks.BROWN_MUSHROOM), new ItemStack(Blocks.RED_MUSHROOM), new ItemStack(Blocks.MYCELIUM), new ItemStack(BWMBlocks.URN, 8, 8), new ItemStack(Items.NETHER_WART)}
        );
    }

    @Override
    public void preInitClient() {

    }

    @Override
    public void initClient() {

    }

    @Override
    public void postInitClient() {

    }
}
