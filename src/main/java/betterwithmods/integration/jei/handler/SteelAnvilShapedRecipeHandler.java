package betterwithmods.integration.jei.handler;

import betterwithmods.common.registry.steelanvil.ShapedSteelAnvilRecipe;
import betterwithmods.integration.jei.BWMJEIPlugin;
import betterwithmods.integration.jei.wrapper.SteelAnvilShapedRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by blueyu2 on 11/25/16.
 */
public class SteelAnvilShapedRecipeHandler implements IRecipeHandler<ShapedSteelAnvilRecipe> {
    @Override
    public Class<ShapedSteelAnvilRecipe> getRecipeClass() {
        return ShapedSteelAnvilRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(ShapedSteelAnvilRecipe recipe) {
        return "bwm.steel_anvil";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(ShapedSteelAnvilRecipe recipe) {
        return new SteelAnvilShapedRecipeWrapper(BWMJEIPlugin.helper, recipe);
    }

    @Override
    public boolean isRecipeValid(ShapedSteelAnvilRecipe recipe) {
        return true;
    }
}
