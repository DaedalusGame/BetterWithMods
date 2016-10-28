package betterwithmods.integration.jei.handler;

import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.integration.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BulkRecipeHandler implements IRecipeHandler<BulkRecipeWrapper> {
    @Nonnull
    @Override
    public Class<BulkRecipeWrapper> getRecipeClass() {
        return BulkRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull BulkRecipeWrapper recipe) {
        return getRecipeCategoryUid(recipe);
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull BulkRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull BulkRecipeWrapper wrapper) {
        BulkRecipe recipe = wrapper.getRecipe();
        if (recipe.getOutput() == null)
            return false;
        int inputCount = 0;
        for (Object input : recipe.getInput()) {
            if (input instanceof List) {
                if (((List<?>) input).isEmpty())
                    return false;
            }
            inputCount++;
        }
        return inputCount > 0;
    }
}
