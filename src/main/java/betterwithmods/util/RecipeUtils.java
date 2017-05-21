package betterwithmods.util;

import betterwithmods.BWMod;
import betterwithmods.common.registry.bulk.manager.*;
import betterwithmods.module.gameplay.CauldronRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public final class RecipeUtils {
    private RecipeUtils() {
    }

    public static void removeFurnaceRecipe(ItemStack input) {
        //for some reason mojang put fucking wildcard for their ore meta
        FurnaceRecipes.instance().getSmeltingList().entrySet().removeIf(next -> next.getKey().isItemEqual(input) || (next.getKey().getItem() == input.getItem() && next.getKey().getMetadata() == OreDictionary.WILDCARD_VALUE));
    }

    public static IBlockState getStateFromStack(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
        }
        return Blocks.AIR.getDefaultState();
    }

    public static int removeShaped(ItemStack output, ItemStack[][] ingredients) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

        int ingredientsWidth = 0;
        int ingredientsHeight = 0;

        if(ingredients != null) {
            ingredientsHeight = ingredients.length;

            for(int i = 0; i < ingredients.length; i++) {
                ingredientsWidth = Math.max(ingredientsWidth, ingredients[i].length);
            }
        }

        List<IRecipe> toRemove = new ArrayList<>();
        List<Integer> removeIndex = new ArrayList<>();
        outer:
        for(int i = 0; i < recipes.size(); i++) {
            IRecipe recipe = recipes.get(i);

            if(recipe.getRecipeOutput().isEmpty() || !output.isItemEqual(recipe.getRecipeOutput())) {
                continue;
            }

            if(ingredients != null) {
                if(recipe instanceof ShapedRecipes) {
                    ShapedRecipes srecipe = (ShapedRecipes) recipe;
                    if(ingredientsWidth != srecipe.recipeWidth || ingredientsHeight != srecipe.recipeHeight) {
                        continue;
                    }

                    for(int j = 0; j < ingredientsHeight; j++) {
                        ItemStack[] row = ingredients[j];
                        for(int k = 0; k < ingredientsWidth; k++) {
                            ItemStack ingredient = k > row.length ? null : row[k];
                            ItemStack recipeIngredient = srecipe.recipeItems[j * srecipe.recipeWidth + k];

                            if(!recipeIngredient.isItemEqual(ingredient)) {
                                continue outer;
                            }
                        }
                    }
                }
            } else {
                if(recipe instanceof ShapelessRecipes) {
                    continue;
                } else if(recipe instanceof ShapelessOreRecipe) {
                    continue;
                } else {
                }
            }

            toRemove.add(recipe);
            removeIndex.add(i);
        }

        for(int i:removeIndex)
            recipes.remove(i);
        return toRemove.size();
    }

    /**
     * Remove all recipes.
     *
     * @param item Item to remove recipes of.
     * @param meta Metavalue.
     *             If {@link OreDictionary#WILDCARD_VALUE} all recipes of the item will be removed.
     */
    public static void removeRecipes(Item item, int meta) {
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        final ListIterator<IRecipe> li = recipes.listIterator();
        boolean found = false;
        while (li.hasNext()) {
            ItemStack output = li.next().getRecipeOutput();
            if (!output.isEmpty() && output.getItem() == item) {
                if (meta == OreDictionary.WILDCARD_VALUE || output.getMetadata() == meta) {
                    li.remove();
                    found = true;
                }
            }
        }
        if (!found)
            BWMod.logger.error("No matching recipe found.");

    }

    /**
     * Remove all recipes.
     *
     * @param block Block to remove recipes of.
     */
    public static void removeRecipes(Block block) {
        removeRecipes(new ItemStack(block));
    }

    /**
     * Remove all recipes.
     *
     * @param stack ItemStack to remove recipes of.
     */
    public static void removeRecipes(ItemStack stack) {
        List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
        final ListIterator<IRecipe> li = recipeList.listIterator();
        boolean found = false;
        while (li.hasNext()) {
            ItemStack output = li.next().getRecipeOutput();
            if (OreDictionary.itemMatches(stack, output, false)) {
                li.remove();
                found = true;
            }
        }
        if (!found)
            BWMod.logger.error("No matching recipe found.");

    }

    public static void gatherCookableFood() {
        Map<ItemStack, ItemStack> furnace = FurnaceRecipes.instance().getSmeltingList();

        for (ItemStack input : furnace.keySet()) {
            if (input != null) {
                if (input.getItem() instanceof ItemFood && input.getItem() != Items.BREAD) {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input);
                    if (!output.isEmpty()) {
                        CauldronRecipes.addCauldronRecipe(output.copy(),new Object[]{input.copy()});
                    }
                }
            }
        }
    }

    public static void refreshRecipes() {
        CauldronManager.getInstance().refreshRecipes();
        StokedCauldronManager.getInstance().refreshRecipes();
        CrucibleManager.getInstance().refreshRecipes();
        StokedCrucibleManager.getInstance().refreshRecipes();
        MillManager.getInstance().refreshRecipes();
    }
}
