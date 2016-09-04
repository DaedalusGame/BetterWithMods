package betterwithmods.integration.jei;

import betterwithmods.craft.bulk.BulkRecipe;
import mezz.jei.api.recipe.BlankRecipeWrapper;

import javax.annotation.Nonnull;

public class BWMRecipeWrapper extends BlankRecipeWrapper
{
    @Nonnull
    private final BulkRecipe recipe;

    public BWMRecipeWrapper(@Nonnull BulkRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Nonnull
    public BulkRecipe getRecipe()
    {
        return recipe;
    }
}
