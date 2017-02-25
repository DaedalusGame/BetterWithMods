package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.bulk.BulkRecipe;
import mezz.jei.api.IJeiHelpers;

import javax.annotation.Nonnull;

public class MillRecipeWrapper extends BulkRecipeWrapper {
    public MillRecipeWrapper(IJeiHelpers helper, @Nonnull BulkRecipe recipe) {
        super(helper, recipe);
    }
}
