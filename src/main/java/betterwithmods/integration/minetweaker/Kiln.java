package betterwithmods.integration.minetweaker;


import betterwithmods.craft.BlockMetaRecipe;
import betterwithmods.craft.KilnInteraction;
import betterwithmods.integration.minetweaker.utils.BaseListAddition;
import betterwithmods.integration.minetweaker.utils.LogHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static betterwithmods.integration.minetweaker.utils.InputHelper.*;
import static betterwithmods.integration.minetweaker.utils.StackHelper.matches;

/**
 * Created by tyler on 9/3/16.
 */
@ZenClass("mods.betterwithmods.Kiln")
public class Kiln {
    private static final ArrayList<BlockMetaRecipe> recipes = KilnInteraction.INSTANCE.getRecipes();

    @ZenMethod
    public static void add(IItemStack input, IItemStack[] output) {
        ItemStack stack = toStack(input);
        Block block = Block.getBlockFromItem(stack.getItem());
        MineTweakerAPI.apply(new Add(block, stack.getMetadata(), toStacks(output)));
    }

    @ZenMethod
    public static void remove(IIngredient output) {
        ArrayList<BlockMetaRecipe> toRemove = new ArrayList<>();
        for (BlockMetaRecipe recipe : recipes) {
            if (recipe != null && matches(output, toIItemStack(recipe.getOutputs().get(0)))) {
                toRemove.add(recipe);
            }
        }
        if (!toRemove.isEmpty()) {
            MineTweakerAPI.apply(new Remove(toRemove));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", "kiln", output.toString()));
        }
    }

    private static class Add extends BaseListAddition<BlockMetaRecipe> {
        protected Add(Block block, int meta, ItemStack... product) {
            super("kiln", Kiln.recipes);
            recipes.add(new BlockMetaRecipe(block, meta, Arrays.asList(product)));
        }

        @Override
        protected String getRecipeInfo(BlockMetaRecipe recipe) {
            return recipe.toString();
        }
    }

    private static class Remove extends BaseListAddition<BlockMetaRecipe> {
        protected Remove(List<BlockMetaRecipe> list) {
            super("kiln", Kiln.recipes, list);
        }

        @Override
        protected String getRecipeInfo(BlockMetaRecipe recipe) {
            return recipe.toString();
        }
    }
}
