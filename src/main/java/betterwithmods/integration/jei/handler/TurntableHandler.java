package betterwithmods.integration.jei.handler;

import betterwithmods.integration.jei.wrapper.TurntableRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/11/16
 */
public class TurntableHandler implements IRecipeHandler<TurntableRecipeWrapper> {
    @Override
    public Class<TurntableRecipeWrapper> getRecipeClass() {
        return TurntableRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "bwm.turntable";
    }

    @Override
    public String getRecipeCategoryUid(TurntableRecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(TurntableRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(TurntableRecipeWrapper recipe) {
        return true;
    }
}
