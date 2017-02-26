package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.steelanvil.SteelShapedOreRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SteelShapedRecipeWrapper extends BlankRecipeWrapper {

    private final SteelShapedOreRecipe recipe;
    private final IJeiHelpers jeiHelpers;

    public SteelShapedRecipeWrapper(IJeiHelpers jeiHelpers, SteelShapedOreRecipe recipe) {
        this.jeiHelpers = jeiHelpers;
        this.recipe = recipe;
        for (Object obj : this.recipe.getInput()) {
            if (obj instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) obj;
                if (itemStack != null && itemStack.getCount() != 1) {
                    itemStack.setCount(1);
                }
            }
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        ItemStack recipeOutput = recipe.getRecipeOutput();

        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));

        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, recipeOutput);

    }
}