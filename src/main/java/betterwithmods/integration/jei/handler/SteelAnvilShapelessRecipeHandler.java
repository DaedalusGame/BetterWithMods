package betterwithmods.integration.jei.handler;

import betterwithmods.common.registry.steelanvil.ShapelessSteelAnvilRecipe;
import betterwithmods.integration.jei.BWMJEIPlugin;
import betterwithmods.integration.jei.wrapper.SteelAnvilShapelessRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by blueyu2 on 11/25/16.
 */
public class SteelAnvilShapelessRecipeHandler implements IRecipeHandler<ShapelessSteelAnvilRecipe> {
    @Override
    public Class<ShapelessSteelAnvilRecipe> getRecipeClass() {
        return ShapelessSteelAnvilRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(ShapelessSteelAnvilRecipe recipe) {
        return "bwm.steel_anvil";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(ShapelessSteelAnvilRecipe recipe) {
        return new SteelAnvilShapelessRecipeWrapper(BWMJEIPlugin.helper, recipe);
    }

    @Override
    public boolean isRecipeValid(ShapelessSteelAnvilRecipe recipe) {
        return true;
    }
}
