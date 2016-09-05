package betterwithmods.integration.jei.handler;

import betterwithmods.integration.jei.wrapper.SawWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

/**
 * Created by tyler on 9/4/16.
 */
public class SawRecipeHandler implements IRecipeHandler<SawWrapper> {
    @Nonnull
    @Override
    public Class<SawWrapper> getRecipeClass() {
        return SawWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "bwm.saw";
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull SawWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull SawWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull SawWrapper recipe) {
        return true;
    }
}
