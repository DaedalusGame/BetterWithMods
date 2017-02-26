package betterwithmods.integration.jei.handler;

import betterwithmods.common.registry.steelanvil.SteelShapedOreRecipe;
import betterwithmods.integration.jei.category.SteelCraftingCategory;
import betterwithmods.integration.jei.wrapper.SteelShapedRecipeWrapper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.List;

public class SteelShapedRecipeHandler implements IRecipeHandler<SteelShapedOreRecipe>{

    private IJeiHelpers jeiHelpers;

    public SteelShapedRecipeHandler(IJeiHelpers jeiHelpers)
    {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public Class<SteelShapedOreRecipe> getRecipeClass() {
        return SteelShapedOreRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid(SteelShapedOreRecipe recipe) {
        return SteelCraftingCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SteelShapedOreRecipe recipe) {
        return new SteelShapedRecipeWrapper(jeiHelpers, recipe);
    }

    @Override
    public boolean isRecipeValid(SteelShapedOreRecipe recipe) {
        if(recipe.getRecipeOutput() == null)
            return false;
        int inputCount = 0;
        for (Object input : recipe.getInput()) {
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