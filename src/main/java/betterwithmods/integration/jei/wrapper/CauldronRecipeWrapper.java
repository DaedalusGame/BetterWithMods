package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.bulk.BulkRecipe;
import mezz.jei.api.IJeiHelpers;

import javax.annotation.Nonnull;

public class CauldronRecipeWrapper extends BulkRecipeWrapper {
    public CauldronRecipeWrapper(IJeiHelpers helper, @Nonnull BulkRecipe recipe) {
        super(helper, recipe);
    }
}
