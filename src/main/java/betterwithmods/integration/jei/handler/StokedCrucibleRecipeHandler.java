package betterwithmods.integration.jei.handler;

import betterwithmods.common.registry.bulk.BulkRecipe;
import betterwithmods.integration.jei.wrapper.StokedCrucibleRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class StokedCrucibleRecipeHandler implements IRecipeHandler<StokedCrucibleRecipeWrapper> {

    @Nonnull
    @Override
    public Class<StokedCrucibleRecipeWrapper> getRecipeClass() {
        return StokedCrucibleRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull StokedCrucibleRecipeWrapper recipe) {
        return "bwm.crucible.stoked";
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull StokedCrucibleRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull StokedCrucibleRecipeWrapper wrapper) {
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
