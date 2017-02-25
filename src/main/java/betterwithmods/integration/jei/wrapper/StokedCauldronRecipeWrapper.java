package betterwithmods.integration.jei.wrapper;

import betterwithmods.common.registry.bulk.BulkRecipe;
import mezz.jei.api.IJeiHelpers;

import javax.annotation.Nonnull;

public class StokedCauldronRecipeWrapper extends BulkRecipeWrapper {
    public StokedCauldronRecipeWrapper(IJeiHelpers helper, @Nonnull BulkRecipe recipe) {
        super(helper, recipe);
    }
}
