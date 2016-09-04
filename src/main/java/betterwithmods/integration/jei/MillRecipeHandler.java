package betterwithmods.integration.jei;

import betterwithmods.craft.bulk.BulkRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

public class MillRecipeHandler implements IRecipeHandler<MillRecipeWrapper>
{
    @Nonnull
    @Override
    public String getRecipeCategoryUid()
    {
        return "bwm.mill";
    }

    @Nonnull
    @Override
    public Class<MillRecipeWrapper> getRecipeClass()
    {
        return MillRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull MillRecipeWrapper recipe)
    {
        return getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull MillRecipeWrapper recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull MillRecipeWrapper wrapper)
    {
        BulkRecipe recipe = wrapper.getRecipe();
        if(recipe.getOutput() == null)
            return false;
        int inputCount = 0;
        for(Object input : recipe.getInput()) {
            if(input instanceof List)
            {
                if(((List)input).isEmpty())
                    return false;
            }
            inputCount++;
        }
        return inputCount > 0;
    }
}
