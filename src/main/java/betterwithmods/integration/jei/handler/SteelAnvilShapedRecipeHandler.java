package betterwithmods.integration.jei.handler;

import betterwithmods.integration.jei.wrapper.SteelAnvilShapedRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by blueyu2 on 11/25/16.
 */
public class SteelAnvilShapedRecipeHandler implements IRecipeHandler<SteelAnvilShapedRecipeWrapper> {
    @Override
    public Class<SteelAnvilShapedRecipeWrapper> getRecipeClass() {
        return SteelAnvilShapedRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "bwm.steelanvil";
    }

    @Override
    public String getRecipeCategoryUid(SteelAnvilShapedRecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SteelAnvilShapedRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(SteelAnvilShapedRecipeWrapper recipe) {
        return true;
    }
}
