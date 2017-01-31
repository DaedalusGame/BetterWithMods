package betterwithmods.integration.jei.handler;

import betterwithmods.craft.BlockMetaRecipe;
import betterwithmods.integration.jei.wrapper.KilnWrapper;
import betterwithmods.integration.jei.wrapper.SawWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class BlockMetaRecipeHandler implements IRecipeHandler<BlockMetaRecipe> {
    @Nonnull
    @Override
    public Class<BlockMetaRecipe> getRecipeClass() {
        return BlockMetaRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull BlockMetaRecipe recipe) {
        return "bwm." + recipe.getType();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull BlockMetaRecipe recipe) {
        if (recipe.getType().equals("kiln"))
            return new KilnWrapper(recipe);
        return new SawWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull BlockMetaRecipe recipe) {
        return "saw".equals(recipe.getType()) || "kiln".equals(recipe.getType());
    }
}
