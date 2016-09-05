package betterwithmods.integration.jei.handler;

import betterwithmods.integration.jei.wrapper.KilnWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

/**
 * Created by tyler on 9/4/16.
 */
public class KilnRecipeHandler implements IRecipeHandler<KilnWrapper> {
    @Nonnull
    @Override
    public Class<KilnWrapper> getRecipeClass() {
        return KilnWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "bwm.kiln";
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull KilnWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull KilnWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull KilnWrapper recipe) {
        return true;
    }
}
