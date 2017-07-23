package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by tyler on 5/16/17.
 */
public class CraftingRecipes extends Feature {
    public CraftingRecipes() {
        canDisable = false;
    }

    @Override
    public void init(FMLInitializationEvent event) {

        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.PADDING), "XXX", "XXX", "XXX", 'X', "padding");
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.SOAP), "XXX", "XXX", "XXX", 'X', "soap");
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.FLINT), "XXX", "XXX", "XXX", 'X', new ItemStack(Items.FLINT));
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.ROPE), "XXX", "XXX", "XXX", 'X', new ItemStack(BWMBlocks.ROPE));
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.HELLFIRE), "XXX", "XXX", "XXX", 'X', "ingotConcentratedHellfire");
        BWMRecipes.addOreRecipe(BlockAesthetic.getStack(BlockAesthetic.EnumType.ENDERBLOCK), "XXX", "XXX", "XXX", 'X', "enderpearl");

        GameRegistry.addSmelting(BWMItems.RAW_EGG, new ItemStack(BWMItems.COOKED_EGG), 0.1F);
        GameRegistry.addSmelting(BWMItems.RAW_SCRAMBLED_EGG, new ItemStack(BWMItems.COOKED_SCRAMBLED_EGG), 0.1F);
        GameRegistry.addSmelting(BWMItems.RAW_OMELET, new ItemStack(BWMItems.COOKED_OMELET), 0.1F);
        GameRegistry.addSmelting(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE), new ItemStack(Items.NETHERBRICK), 0.2F);
        GameRegistry.addSmelting(new ItemStack(BWMBlocks.AESTHETIC, 1, 7), new ItemStack(BWMBlocks.AESTHETIC, 1, 6), 0.1F);
        GameRegistry.addSmelting(BWMItems.WOLF_CHOP, new ItemStack(BWMItems.COOKED_WOLF_CHOP), 0.5f);
        GameRegistry.addSmelting(BWMItems.MYSTERY_MEAT, new ItemStack(BWMItems.COOKED_MYSTERY_MEAT), 0.5f);
        GameRegistry.addSmelting(BWMItems.RAW_KEBAB, new ItemStack(BWMItems.COOKED_KEBAB), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.CAKE), new ItemStack(Items.CAKE), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD), new ItemStack(Items.BREAD), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.APPLE), new ItemStack(BWMItems.APPLE_PIE), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.PUMPKIN), new ItemStack(Items.PUMPKIN_PIE), 0.1F);
        GameRegistry.addSmelting(BlockRawPastry.getStack(BlockRawPastry.EnumType.COOKIE), new ItemStack(Items.COOKIE, 16), 0.1F);

        BWMRecipes.removeFurnaceRecipe(new ItemStack(Items.NETHERBRICK));
    }


    @Override
    public String getFeatureDescription() {
        return "Adds basic crafting recipes";
    }
}
