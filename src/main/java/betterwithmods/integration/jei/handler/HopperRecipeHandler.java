package betterwithmods.integration.jei.handler;

import betterwithmods.integration.jei.wrapper.HopperRecipeWrapper;
import betterwithmods.integration.jei.wrapper.SoulUrnWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 11/20/16
 */
public class HopperRecipeHandler implements IRecipeHandler<HopperRecipeWrapper> {
    @Override
    public Class<HopperRecipeWrapper> getRecipeClass() {
        return HopperRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "bwm.hopper";
    }

    @Override
    public String getRecipeCategoryUid(HopperRecipeWrapper recipe) {
        if (recipe instanceof SoulUrnWrapper) {
            return "bwm.hopper.soulurn";
        }
        return getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(HopperRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(HopperRecipeWrapper recipe) {
        return true;
    }
}
