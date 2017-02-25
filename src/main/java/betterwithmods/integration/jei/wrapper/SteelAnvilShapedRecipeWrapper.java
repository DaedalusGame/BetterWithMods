package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.steelanvil.ShapedSteelAnvilRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by blueyu2 on 11/25/16.
 */
public class SteelAnvilShapedRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    private final IJeiHelpers jeiHelpers;
    private final ShapedSteelAnvilRecipe recipe;
    private final int width;
    private final int height;

    public SteelAnvilShapedRecipeWrapper(IJeiHelpers jeiHelpers, ShapedSteelAnvilRecipe recipe) {
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;
        for (Object input : this.recipe.getInput()) {
            if (input instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) input;
                if (itemStack.getCount() != 1) {
                    itemStack.setCount(1);
                }
            }
        }
        this.width = recipe.getWidth();
        this.height = recipe.getHeight();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();

        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
        ingredients.setInputLists(ItemStack.class, inputs);

        ItemStack recipeOutput = recipe.getRecipeOutput();
        if (recipeOutput != ItemStack.EMPTY) {
            ingredients.setOutput(ItemStack.class, recipeOutput);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
