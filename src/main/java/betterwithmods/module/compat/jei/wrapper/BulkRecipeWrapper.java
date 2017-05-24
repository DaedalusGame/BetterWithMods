package betterwithmods.module.compat.jei.wrapper;

import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class BulkRecipeWrapper extends BlankRecipeWrapper {

    @Nonnull
    protected final BulkRecipe recipe;
    private final IJeiHelpers helpers;

    public BulkRecipeWrapper(IJeiHelpers helpers, @Nonnull BulkRecipe recipe) {
        this.recipe = recipe;
        this.helpers = helpers;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        IStackHelper stackHelper = helpers.getStackHelper();
        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(recipe.getInputs());
        ingredients.setInputLists(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, recipe.getOutputs());
    }

    @Nonnull
    public BulkRecipe getRecipe() {
        return recipe;
    }
}
