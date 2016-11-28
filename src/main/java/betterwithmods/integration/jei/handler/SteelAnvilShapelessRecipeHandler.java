package betterwithmods.integration.jei.handler;

import betterwithmods.integration.jei.wrapper.SteelAnvilShapelessRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by blueyu2 on 11/25/16.
 */
public class SteelAnvilShapelessRecipeHandler implements IRecipeHandler<SteelAnvilShapelessRecipeWrapper> {
    @Override
    public Class<SteelAnvilShapelessRecipeWrapper> getRecipeClass() {
        return SteelAnvilShapelessRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "bwm.steelanvil";
    }

    @Override
    public String getRecipeCategoryUid(SteelAnvilShapelessRecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SteelAnvilShapelessRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(SteelAnvilShapelessRecipeWrapper recipe) {
        return true;
    }
}
