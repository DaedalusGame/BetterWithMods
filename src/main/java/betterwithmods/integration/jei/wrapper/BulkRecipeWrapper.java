package betterwithmods.integration.jei.wrapper;

import betterwithmods.craft.bulk.BulkRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BulkRecipeWrapper extends BWMRecipeWrapper {

    private final IJeiHelpers helpers;

    public BulkRecipeWrapper(IJeiHelpers helpers, @Nonnull BulkRecipe recipe) {
        super(recipe);
        this.helpers = helpers;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = helpers.getStackHelper();
        List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(recipe.getInput());
        ingredients.setInputLists(ItemStack.class, inputs);
        List<ItemStack> outputs = new ArrayList<>();
        outputs.add(getRecipe().getOutput().copy());
        if (getRecipe().getSecondary() != null)
            outputs.add(getRecipe().getSecondary().copy());
        ingredients.setOutputs(ItemStack.class, outputs);
    }
}
