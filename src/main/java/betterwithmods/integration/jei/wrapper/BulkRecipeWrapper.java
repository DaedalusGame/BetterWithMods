package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = helpers.getStackHelper();
        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(recipe.getInputs());
        ingredients.setInputLists(ItemStack.class, inputs);
        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(getRecipe().getOutput().copy());
        if (getRecipe().getSecondary() != null)
            outputs.add(getRecipe().getSecondary().copy());
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Nonnull
    public BulkRecipe getRecipe() {
        return recipe;
    }
}
