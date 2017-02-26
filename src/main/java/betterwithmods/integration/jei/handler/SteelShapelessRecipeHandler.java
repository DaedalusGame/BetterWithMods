package betterwithmods.integration.jei.handler;

import betterwithmods.common.registry.steelanvil.SteelShapelessRecipe;
import betterwithmods.integration.jei.category.SteelCraftingCategory;
import betterwithmods.integration.jei.wrapper.SteelShapelessRecipeWrapper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.List;

public class SteelShapelessRecipeHandler implements IRecipeHandler<SteelShapelessRecipe> {

    private IJeiHelpers jeiHelpers;

    public SteelShapelessRecipeHandler(IJeiHelpers jeiHelpers)
    {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public Class<SteelShapelessRecipe> getRecipeClass() {
        return SteelShapelessRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(SteelShapelessRecipe recipe) {
        return SteelCraftingCategory.UID;
    }


    @Override
    public IRecipeWrapper getRecipeWrapper(SteelShapelessRecipe recipe) {
        return new SteelShapelessRecipeWrapper(jeiHelpers, recipe);
    }

    @Override
    public boolean isRecipeValid(SteelShapelessRecipe recipe) {
        if(recipe.getRecipeOutput() == null)
            return false;

        int inputCount = 0;
        for (Object input : recipe.recipeItems) {
            if(input != null) {
                if(input instanceof List && ((List) input).isEmpty())
                {
                    return false;
                }
                inputCount++;
            }
        }

        if(inputCount > 81)
            return false;

        return inputCount > 0;
    }
}