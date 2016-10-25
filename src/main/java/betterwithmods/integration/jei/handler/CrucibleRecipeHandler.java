package betterwithmods.integration.jei.handler;

import betterwithmods.craft.bulk.BulkRecipe;
import betterwithmods.integration.jei.wrapper.CrucibleRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class CrucibleRecipeHandler implements IRecipeHandler<CrucibleRecipeWrapper> {
    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "bwm.crucible";
    }

    @Nonnull
    @Override
    public Class<CrucibleRecipeWrapper> getRecipeClass() {
        return CrucibleRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull CrucibleRecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull CrucibleRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull CrucibleRecipeWrapper wrapper) {
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
